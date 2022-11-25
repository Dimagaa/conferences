package com.conferences.controllers.servlets;

import com.conferences.dto.EventDto;
import com.conferences.exceptions.DaoException;
import com.conferences.model.User;
import com.conferences.services.EventService;
import com.conferences.services.pagination.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.conferences.model.Event.Status;
import static com.conferences.model.Event.Status.*;
import static com.conferences.model.User.Role.SPEAKER;

/**
 *  <code>HttpServlet</code> support <code>doGet</code> and <code>doPost</code> methods.
 *  Allows get configured web page with events
 */
public class Events extends HttpServlet {

    /**
     * {@link HashMap} contains servlet path as a key and corresponding {@link BiFunction} as a value
     */
    private final Map<String, BiFunction<HttpServletRequest, String, Page>> targetContent = new HashMap<>();
    {
        targetContent.put("/events/developing", (req, s) -> eventsByStatus(req, Status.DEVELOPING.name()));
        targetContent.put("/events/canceled", (req, s) -> eventsByStatus(req, Status.CANCELED.name()));
        targetContent.put("/events/joined", this::joinedEvents);
        targetContent.put("/events/passed", this::passedEvents);
    }

    final private Logger logger = LogManager.getLogger("Global");


    /**
     * Allows get events that corresponds to the preset parameters.
     * Forwards a request from a servlet to {@code ../event-main.jsp}.
     * If {@link DaoException} was occurred send internal server error
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException if an input or output error occurs
     * @throws IOException if the request for the HEAD could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = (long) req.getSession().getAttribute("userId");
        Page page = targetContent.getOrDefault(req.getServletPath(),
                        ((r, s) -> eventsByStatus(r, String.format("%s %s %s", ACTIVE, COMPLETED, CANCELED))))
                .apply(req, String.valueOf(userId));
        try {
            setEventAttribute(req, page);
        } catch (DaoException e) {
            logger.error("Can't execute get request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/view/event-main.jsp").forward(req, resp);
    }

    /**
     * Gets prepared events and set it as request parameter.
     * @param req the request object that is passed to the servlet
     * @param page an {@link Page} object that store all filters, sorters params
     *      *             and then will handle in Dao layers.
     * @throws DaoException if executing {@link EventService} methods error occurs
     */
    private void setEventAttribute(HttpServletRequest req, Page page) throws DaoException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long userId = (long) req.getSession().getAttribute("userId");
        User.Role role = (User.Role) req.getSession().getAttribute("role");
        List<EventDto> events = eventService.getPreparedEvents(page, role, userId);
        req.setAttribute("events", events);
        req.getSession().setAttribute("page", page);
        if(role == SPEAKER) {
            req.setAttribute("countProposed", eventService.countProposedReport(userId));
        }
    }

    /**
     * Gets {@link Page} from {@link javax.servlet.http.HttpSession} or create new if not present.
     * New instance of {@link Page} will create with default page size 4 and default page number 1;
     * @param req the request object that is passed to the servlet
     * @return {@link Page}
     */
    private Page getPage(HttpServletRequest req) {
        int pageSize = 4;
        Optional<Page> oPage = Optional.ofNullable((Page) req.getSession().getAttribute("page"));
        Optional<String> pageNum = Optional.ofNullable(req.getParameter("page"));
        int pageNumber = Integer.parseInt(pageNum.orElse("1"));
        if (oPage.isPresent()) {
            Page page = oPage.get();
            page.setPageNumber(pageNumber);
            return page;
        }
        return new Page(pageNumber, pageSize);
    }

    private Page eventsByStatus(HttpServletRequest req, String status) {
        Page page = getPage(req);
        page.setTargetFilters("eventsByStatus", status);
        return page;
    }

    private Page joinedEvents(HttpServletRequest req, String userId) {
        User.Role role = (User.Role) req.getSession().getAttribute("role");
        Page page = getPage(req);
        if (role == User.Role.USER) {
            page.setTargetFilters("joinedEvents", userId);
            return page;
        }
        if (role == SPEAKER) {
            page.setTargetFilters("eventsBySpeaker", userId);
            return page;
        }
        return getPage(req);
    }

    private Page passedEvents(HttpServletRequest req, String userId) {
        Page page = joinedEvents(req, userId);
        page.puttTargetFilter("expiredEvents", "true");
        return page;
    }
}
