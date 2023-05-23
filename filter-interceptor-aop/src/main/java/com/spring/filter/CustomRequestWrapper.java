package com.spring.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> modifiedParameters;

    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
        this.modifiedParameters = new HashMap<>(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] values = modifiedParameters.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(modifiedParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(modifiedParameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return modifiedParameters.get(name);
    }

    public void setParameter(String name, String value) {
        modifiedParameters.put(name, new String[]{value});
    }
}
