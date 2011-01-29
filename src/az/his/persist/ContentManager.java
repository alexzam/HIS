package az.his.persist;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.List;

/**
 * Manages persistency
 */
public class ContentManager {
    /**
     * Session factory, fetched from JNDI.
     */
    protected SessionFactory factory;

    protected SessionContext sessionContext;

    @SuppressWarnings({"unchecked"})
    public <E> List<E> findAll(Class<E> entityClass) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        return query.setCacheable(true).list();
    }

    @SuppressWarnings({"unchecked"})
    public <E> E findByNaturalKey(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return (E) query.setCacheable(true).uniqueResult();
    }

    @SuppressWarnings({"unchecked"})
    public <E> E get(Class<E> entityClass, Serializable id) {
        return (E) factory.getCurrentSession().get(entityClass, id);
    }

    public void remove(IdEntity object) {
        IdEntity merged = get(object.getClass(), object.getId());
        factory.getCurrentSession().delete(merged);
        factory.getCurrentSession().flush();
    }

    @SuppressWarnings({"unchecked"})
    public <E> E merge(E object) {
        E result = (E) factory.getCurrentSession().merge(object);
        factory.getCurrentSession().flush();
        return result;
    }

    public void persist(Object object) {
        factory.getCurrentSession().persist(object);
        factory.getCurrentSession().flush();
    }

    public void update(Object object) {
        factory.getCurrentSession().saveOrUpdate(object);
        factory.getCurrentSession().flush();
    }

    @SuppressWarnings({"unchecked"})
    public <E> List<E> findByProperty(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return query.list();
    }

    public void init() throws CreateException {
        try {
            InitialContext ctx = new InitialContext();
            String jndi = (String) ctx.lookup("java:comp/env/HibernateFactoryJNDI");
            factory = (SessionFactory) ctx.lookup(jndi);
        } catch (NamingException e) {
            throw new CreateException(e.getMessage());
        }
    }
}
