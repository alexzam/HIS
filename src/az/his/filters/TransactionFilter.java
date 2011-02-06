package az.his.filters;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.*;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.IOException;

/**
 * Manages transaction. Not annotated because processing order is significant and set in web.xml
 */
public class TransactionFilter implements Filter {
    @PersistenceContext(unitName = "main")
    private EntityManager em;
    @Resource
    public UserTransaction utx;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (Boolean.TRUE.equals(req.getAttribute(StaticFilter.STATIC_CONTENT))) {
            chain.doFilter(req, resp);
        } else {
            try {
                if (utx.getStatus() == Status.STATUS_NO_TRANSACTION)
                    utx.begin();
                chain.doFilter(req, resp);

            } catch (Exception e) {
                try {
                    utx.setRollbackOnly();
                    throw new ServletException(e);
                } catch (SystemException e1) {
                    throw new ServletException(e1);
                }
            } finally {
                try {
                    if (utx.getStatus() == Status.STATUS_ACTIVE) {
                        utx.commit();
                    } else {
                        utx.rollback();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
