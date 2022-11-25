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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <code>HttpServlet</code> support <code>doGet</code> and <code>doPut</code> methods.
 * Allows get data as <code>JsonArray</code> for autocomplete function on web page.
 */
public class Autocomplete extends HttpServlet {
    private final Logger logger = LogManager.getLogger("Global");

    /**
     * <code>HashMap</code> contains  keys as target and values
     * as method references of {@link DataSupplier}.
     */
    private final Map<String, DataSupplier> dataSuppliers = new HashMap<>();

    {
        dataSuppliers.put("place", this::getPlaceJson);
        dataSuppliers.put("speaker", this::getSpeakerJson);
        dataSuppliers.put("search", this::search);

    }

    /**
     * Get request parameter "target" as key in {@link #dataSuppliers}.
     * Result executing will return Json Response to web client.
     * When exception occurred will send Internal Server Error (500) to web client.
     *
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws IOException if input/output exceptions occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String target = Optional.ofNullable(req.getParameter("target")).orElse("").trim();
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            out.print(dataSuppliers.get(target).execute(req));
        } catch (DaoException e) {
            logger.error("Autocomplete problem, cannot get data from Data Base", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method getting Json representation of speaker and set it as
     * <code>HttpSession</code> attribute when speaker was selected
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client
     *
     * @throws IOException if input/output exception occurs
     */

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject speaker = Json.createReader(req.getReader()).readObject();
        req.getSession().setAttribute("selectedSpeaker", speaker);
    }


    /**
     * Allows get places that matches clients query string
     * form Events entity.
     *
     * @param req the {@link  HttpServletRequest} object that
     *            contains parameter "query" - input data from client
     * @return {@link  JsonArray} that contains places that match query string
     * @throws  DaoException - if the error occurs while executing {@link EventService} methods
     */
    private JsonArray getPlaceJson(HttpServletRequest req) throws DaoException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        String prefix = req.getParameter("query");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        eventService.getPlaces(String.format("%%%s%%", prefix)).stream()
                .map(s -> Json.createObjectBuilder().add("value", s))
                .forEach(builder::add);
        return builder.build();
    }

    /**
     * Allows get speakers from Events entity that matches clients query string
     *
     * @param req - the {@link HttpServletRequest} object
     * @return {@link JsonArray} that contains speakers that match clients query string
     * @throws DaoException - if the error occur while executing {@link UserService}
     */
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

    /**
     * Allows get events headers that matches clients query string
     *
     * @param req = {@link HttpServletRequest} object that contains request parameter
     *            "data" that is representation clients query string
     * @return {@link JsonArray} that contains events headers that match clients query string
     * @throws DaoException - if the error occur while executing {@link EventService}
     */
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

    /**
     * Represents an operation that accepts {@link HttpServletRequest} and returns
     * {@link JsonArray}. Unlike {@link java.util.function.Supplier}
     * functional method is {@link #execute(HttpServletRequest)} that can throw {@link DaoException}
     */
    @FunctionalInterface
    private interface DataSupplier {
        JsonArray execute(HttpServletRequest req) throws DaoException;
    }
}
