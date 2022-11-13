package com.conferences.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class filterPath extends SimpleTagSupport {

    private String servletPath;

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public void doTag() throws JspException {
         JspWriter out = getJspContext().getOut();
         String contextPath = ((PageContext) getJspContext()).getServletContext().getContextPath();
         String filter = servletPath.endsWith("/filter") ? "" : "/filter";
        try {
            out.print(contextPath + servletPath + filter);
        } catch (IOException e) {
            throw new JspException(e);
        }
    }


}
