package az.his.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Checks authorization
 */
public class AuthFilter implements Filter {
    public static String SESS_UID = "AuthFilter-UID";

    public static int getUid(HttpSession session) {
        Integer uid = (Integer) session.getAttribute(SESS_UID);
        return (uid == null) ? 0 : uid;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (Boolean.TRUE.equals(req.getAttribute(StaticFilter.STATIC_CONTENT))) {
            chain.doFilter(req, resp);
            return;
        }

        HttpServletRequest htReq = (HttpServletRequest) req;
        HttpServletResponse htResp = (HttpServletResponse) resp;

        HttpSession session = htReq.getSession(true);
        boolean isAuth = getUid(session) != 0;

        ServletContext context = htReq.getServletContext();
        String pgLogin = context.getInitParameter("page.login");
        String pgMain = context.getInitParameter("page.main");
        String path = context.getInitParameter("path.root");

        String uri = htReq.getServletPath();
        boolean isPublic = uri.endsWith("/" + pgLogin);
        boolean isLoginReq = uri.endsWith("/login");

        if (isPublic && isAuth) {
            htResp.sendRedirect(path + pgMain);
//            htResp.getWriter().append("Redirect: "+path + pgMain);
        } else if (!isAuth && !isPublic && !isLoginReq) {
            htResp.sendRedirect(path);
//            htResp.getWriter().append("Redirect: "+path);
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
