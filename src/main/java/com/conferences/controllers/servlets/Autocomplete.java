package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.model.User;
import com.conferences.services.EventService;
import com.conferences.services.UserService;
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
        dataSuppliers.put("search", this::search);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String target = Optional.ofNullable(req.getParameter("target")).orElse("").trim();
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            out.print(dataSuppliers.get(target).execute(req));
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

    private JsonArray getPlaceJson(HttpServletRequest req) throws DaoException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        String prefix = req.getParameter("data");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        eventService.getPlaces(String.format("%%%s%%", prefix)).stream()
                .map(s -> Json.createObjectBuilder().add("value", s))
                .forEach(builder::add);
        return builder.build();
    }

    private JsonArray getSpeakerJson(HttpServletRequest req) throws DaoException {
        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        userService.getAllSpeakers().stream()
                .map(user -> Json.createObjectBuilder().add("id", user.getId())
                        .add("email", user.getLogin())
                        .add("name", user.getFirstName() + " " + user.getLastName()))
                .forEach(builder::add);
        return builder.build();
    }

    private JsonArray search(HttpServletRequest req) throws DaoException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        String prefix = Optional.ofNullable(req.getParameter("data")).orElse("").trim();
        User.Role role = (User.Role) req.getSession().getAttribute("role");
        eventService.getEventHeaders(String.format("%%%s%%", prefix), role).stream()
                .map(s -> Json.createObjectBuilder().add("value", s))
                .forEach(builder::add);
        return builder.build();
    }

    @FunctionalInterface
    private interface DataSupplier {
        JsonArray execute(HttpServletRequest req) throws DaoException;
    }
}
