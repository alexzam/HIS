package az.his.filters;

import javax.servlet.*;
import java.io.IOException;

/**
 * Flags request for static content.
 */
public class StaticFilter implements Filter {
    public static final String STATIC_CONTENT = "STATIC_CONTENT";

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        req.setAttribute(STATIC_CONTENT, true);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {}

}
