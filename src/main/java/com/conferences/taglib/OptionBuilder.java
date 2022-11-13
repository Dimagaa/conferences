package com.conferences.taglib;

import com.conferences.dao.parameters.SortParameter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class OptionBuilder extends SimpleTagSupport {

    private Map<String, SortParameter.Order> selectedItems;
    private Map<String, String> items;


    public Map<String, SortParameter.Order> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Map<String, SortParameter.Order> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        try {
            out.print(items.entrySet().stream().map((e) -> {
                String selected = "";
                String order = "";
                if(nonNull(selectedItems) && selectedItems.containsKey(e.getKey())) {
                    selected = "selected";
                    order = selectedItems.get(e.getKey()).name();
                }
                return getOption(e.getKey(), e.getValue(), order, selected);
            }).collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new JspException("Cannot generate options", e);
        }
    }

    private String getOption(String val, String label, String order, String selected) {
        return String.format(
                "<option value=\"%1$s\" label=\"%2$s\" data-data='{\"value\":\"%1$s\",\"label\":\"%2$s\",\"order\":\"%3$s\"}' %4$s></option>\n",
                val, label, order, selected);
    }
}
