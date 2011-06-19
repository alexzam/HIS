package az.his;

import az.his.filters.AuthFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Process login and logout
 */
public class AuthServlet extends HttpServlet {
    private static String redirectRootPath;
    private static String redirectMainPage;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");

        if (redirectRootPath == null) {
            ServletContext context = request.getServletContext();
            redirectRootPath = context.getInitParameter("path.root");
            redirectMainPage = redirectRootPath + context.getInitParameter("page.main");
        }

        if (mode.equals("in")) doLogin(request, response);
        else if (mode.equals("out")) doLogout(request, response);
        else throw new ServletException("Login: Incorrect mode");
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(true).removeAttribute(AuthFilter.SESS_UID);
        response.sendRedirect(redirectRootPath);
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uid = request.getParameter("uid");
        if (uid == null) throw new ServletException("Missing uid");

        request.getSession(true).setAttribute(AuthFilter.SESS_UID, Integer.parseInt(uid));
        response.sendRedirect(redirectMainPage);
    }
}
