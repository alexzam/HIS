package az.his;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * Hibernate set up
 */
public class DBUtil {
    private static final SessionFactory factory;

    static {
        try {
            factory = new Configuration().configure().buildSessionFactory();
//            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
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

    public static Session openSession() {
        return factory.openSession();
    }
}
