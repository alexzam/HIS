package az.his.ejb;

import az.his.persist.Transaction;
import az.his.persist.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Manages persistency
 */
@Stateless(name = "ContMan")
public class ContentManagerImpl implements ContentManager {
    @PersistenceContext(unitName = "main")
    private EntityManager em;

//    @SuppressWarnings({"unchecked"})
//    public <E> List<E> findAll(Class<E> entityClass) {
//        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
//        return query.setCacheable(true).list();
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public <E> E findByNaturalKey(Class<E> entityClass, String propertyName, Object propertyValue) {
//        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
//        query.add(Restrictions.eq(propertyName, propertyValue));
//        return (E) query.setCacheable(true).uniqueResult();
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public <E> E get(Class<E> entityClass, Serializable id) {
//        return (E) factory.getCurrentSession().get(entityClass, id);
//    }

//    public void remove(IdEntity object) {
//        IdEntity merged = get(object.getClass(), object.getId());
//        factory.getCurrentSession().delete(merged);
//        factory.getCurrentSession().flush();
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public <E> E merge(E object) {
//        E result = (E) factory.getCurrentSession().merge(object);
//        factory.getCurrentSession().flush();
//        return result;
//    }

    public void persist(Object object) {
        em.persist(object);
        em.flush();
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> q = em.createQuery("from az.his.persist.User", User.class);
        return q.getResultList();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        TypedQuery<Transaction> q = em.createQuery("from az.his.persist.Transaction ", Transaction.class);
        return q.getResultList();
    }

    //    public void update(Object object) {
//        factory.getCurrentSession().saveOrUpdate(object);
//        factory.getCurrentSession().flush();
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public <E> List<E> findByProperty(Class<E> entityClass, String propertyName, Object propertyValue) {
//        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
//        query.add(Restrictions.eq(propertyName, propertyValue));
//        return query.list();
//    }
//
//    public static void init() {
//        if (factory == null) factory = new AnnotationConfiguration().configure().buildSessionFactory();
//    }
}
