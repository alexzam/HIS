package az.his;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Hibernate set up
 */
public class DBUtil {
    private static SessionFactory factory;

    public static Session getSession() throws HibernateException {
        if (factory == null) {
            try {
                factory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
        return factory.getCurrentSession();
    }

    @SuppressWarnings({"unchecked"})
    public static <E> List<E> findAll(Class<E> entityClass) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        return query.setCacheable(true).list();
    }

    @SuppressWarnings({"unchecked"})
    public static <E> E findByNaturalKey(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return (E) query.setCacheable(true).uniqueResult();
    }

    @SuppressWarnings({"unchecked"})
    public static <E> E get(Class<E> entityClass, Serializable id) {
        return (E) factory.getCurrentSession().get(entityClass, id);
    }

    @SuppressWarnings({"unchecked"})
    public static <E> E merge(E object) {
        E result = (E) factory.getCurrentSession().merge(object);
        factory.getCurrentSession().flush();
        return result;
    }

    public static void persist(Object object) {
        factory.getCurrentSession().persist(object);
        factory.getCurrentSession().flush();
    }

    public static void update(Object object) {
        factory.getCurrentSession().saveOrUpdate(object);
        factory.getCurrentSession().flush();
    }

    @SuppressWarnings({"unchecked"})
    public static <E> List<E> findByProperty(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public static void delete(Class cls, int id) {
        Session sess = getSession();
        sess.delete(get(cls, id));
    }

    public static Session openSession() {
        return factory.openSession();
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
}
