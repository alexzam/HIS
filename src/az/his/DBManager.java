package az.his;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

public class DBManager {
    private Session session;

    public DBManager(Session sess) {
        session = sess;
    }

    @SuppressWarnings({"unchecked"})
    public <E> List<E> findAll(Class<E> entityClass) {
        Criteria query = session.createCriteria(entityClass);
        return query.setCacheable(true).list();
    }

    @SuppressWarnings({"unchecked"})
    public <E> E findByNaturalKey(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = session.createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return (E) query.setCacheable(true).uniqueResult();
    }

    @SuppressWarnings({"unchecked"})
    public  <E> E get(Class<E> entityClass, Serializable id) {
        return (E) session.get(entityClass, id);
    }

    @SuppressWarnings({"unchecked"})
    public  <E> E merge(E object) {
        E result = (E) session.merge(object);
        session.flush();
        return result;
    }

    public  void persist(Object object) {
        session.persist(object);
        session.flush();
    }

    public  void update(Object object) {
        session.saveOrUpdate(object);
        session.flush();
    }

    @SuppressWarnings({"unchecked"})
    public  <E> List<E> findByProperty(Class<E> entityClass, String propertyName, Object propertyValue) {
        Criteria query = session.createCriteria(entityClass);
        query.add(Restrictions.eq(propertyName, propertyValue));
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public  void delete(Class cls, int id) {
        session.delete(get(cls, id));
    }

    public Session getSession() {
        return session;
    }
}
