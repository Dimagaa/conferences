package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.services.EventService;
import com.webapp.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet
public class Autocomplete extends HttpServlet {
    private final Logger logger = LogManager.getLogger("Global");

    private final Map<String, DataSupplier> dataSuppliers = new HashMap<>();
    {
        dataSuppliers.put("place", this::getPlaceJson);
        dataSuppliers.put("speaker", this::getSpeakerJson);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String target = Optional.ofNullable(req.getParameter("target")).orElse("").trim();
        String input = Optional.ofNullable(req.getParameter("data")).orElse("").trim();
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            Optional<DataSupplier> supplier = Optional.ofNullable(dataSuppliers.get(target));
            if(supplier.isPresent()) {
                out.print(supplier.get().execute(input));
                return;
            }
            out.print(getUnknown(target));
        } catch (DaoException e) {
            logger.error("Autocomplete problem, cannot get data from Data Base", e);
            req.getRequestDispatcher(req.getContextPath() + "/error").forward(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject speaker = Json.createReader(req.getReader()).readObject();
        req.getSession().setAttribute("selectedSpeaker", speaker);
    }

    private JsonArray getPlaceJson(String prefix) throws DaoException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        eventService.getPlaces(String.format("%%%s%%", prefix)).stream()
                .map(s -> Json.createObjectBuilder().add("value", s))
                .forEach(builder::add);
        return builder.build();
    }

    private JsonArray getSpeakerJson(String prefix) throws DaoException {

        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        userService.getAllSpeakers().stream()
                .map(user -> Json.createObjectBuilder().add("id", user.getId())
                        .add("email", user.getLogin())
                        .add("name", user.getFirstName() + " " + user.getLastName()))
                .forEach(builder::add);
        return builder.build();
    }

    private JsonArray getUnknown(String target) {
        logger.error("Target \"" + target + "\" is not defined in " + this.getClass().getSimpleName());
        throw new IllegalArgumentException("Target \"" + target + "\" is not defined in " + this.getClass().getName());
    }

    @FunctionalInterface
    private interface DataSupplier {
        JsonArray execute(String prefix) throws DaoException;
    }
}
