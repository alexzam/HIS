package az.his.dsmanager;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class DataSchemaManagerFilter implements Filter {
    private String dbVersionStr;
    private int dbVersion;
    private int targetVersion;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String jndiName = filterConfig.getInitParameter("datasource");

        try {
            DataSchemaManager.init(jndiName);
            dbVersionStr = DataSchemaManager.getDBVersion();
            dbVersion = DataSchemaManager.getDBVersionId();
            targetVersion = Integer.parseInt(filterConfig.getInitParameter("target-version"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (targetVersion > dbVersion) {
            PrintWriter writer = servletResponse.getWriter();
            writer.println("<html><body><h1>");
            writer.println("Incorrect DB version.");
            writer.println("</h1><h2>");
            writer.print("Detected " + dbVersionStr + " (" + dbVersion + ") but expected " + targetVersion);
            writer.println("</h2></body></html>");
            writer.close();

            return;
        }
        servletRequest.setAttribute("dbVersion", dbVersionStr);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
