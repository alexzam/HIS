package az.his;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletRequest;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Hibernate set up
 */
public class DBUtil {
    private static SessionFactory factory;

    private SessionFactory springSessionFactory;

    public DBUtil(SessionFactory springSessionFactory) {
        this.springSessionFactory = springSessionFactory;
    }

    public DBManager getDbManager() {
        return new DBManager(springSessionFactory.getCurrentSession());
    }

    public static Session getSession() throws HibernateException {
        initFactory();
        return factory.getCurrentSession();
    }

    private static void initFactory() {
        if (factory == null) {
            try {
                factory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    public static Session openSession() {
        initFactory();
        return factory.openSession();
    }

    public static DBManager getDBManFromReq(ServletRequest req){
        return (DBManager) req.getAttribute("DBM");
    }

    private static java.text.NumberFormat dblFormatter = new DecimalFormat("#,###.##");

    public static String formatCurrency(double sum) {
        return dblFormatter.format(sum);
    }

    private static java.text.NumberFormat intFormatter = new DecimalFormat("0");
    private static java.text.NumberFormat int2Formatter = new DecimalFormat("0.00");

    public static String formatCurrency(long sum) {
        if (sum % 100 != 0) {
            return int2Formatter.format((double) sum / 100);
        } else return intFormatter.format((double) sum / 100);
    }

    // Service methods

    @SuppressWarnings({"unchecked"})
    public <E> List<E> findAll(Class<E> entityClass) {
        Criteria query = springSessionFactory.getCurrentSession().createCriteria(entityClass);
        return query.setCacheable(true).list();
    }
}
