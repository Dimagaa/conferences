package com.conferences.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;

public class AccessRule extends SimpleTagSupport {

    private String roleRules;
    private String contextRules;
    private String role;
    private String context;


    public void setRoleRules(String roleRules) {
        this.roleRules = roleRules;
    }

    public void setContextRules(String contextRules) {
        this.contextRules = contextRules;
    }


    public String getContextRules() {
        return contextRules;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public void doTag() throws JspException, IOException {

        boolean isAllowRole = roleRules == null || (role != null && Arrays.stream(roleRules.split(" ")).anyMatch(s -> s.equals(role)));
        boolean isAllowContext = contextRules == null || (context != null && Arrays.stream(contextRules.split(" ")).anyMatch(s -> s.equals(context)));
        if (isAllowRole && isAllowContext) {
            getJspBody().invoke(null);
        }
    }
}
