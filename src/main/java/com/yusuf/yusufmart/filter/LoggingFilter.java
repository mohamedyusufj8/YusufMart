package com.yusuf.yusufmart.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Attaches a unique request ID to SLF4J MDC for structured request tracing.
 * Fulfills Section 18 requirement: Structured logging via Logback.
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {

    private static final String REQUEST_ID_KEY = "requestId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String requestId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put(REQUEST_ID_KEY, requestId);

            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                httpRequest.setAttribute(REQUEST_ID_KEY, requestId);
            }

            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}
