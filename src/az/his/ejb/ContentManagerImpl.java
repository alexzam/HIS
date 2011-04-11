package az.his.ejb;

import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import az.his.persist.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Manages persistency
 */
@Stateless(name = "ContMan")
public class ContentManagerImpl implements ContentManager {
    @PersistenceContext(unitName = "main")
    private EntityManager em;

    @Override
    @SuppressWarnings({"unchecked"})
    public <E> List<E> findAll(Class<E> entityClass) {
        CriteriaQuery query = em.getCriteriaBuilder().createQuery(entityClass);
        query.from(entityClass);
        return em.createQuery(query).getResultList();
    }

    //
//    @SuppressWarnings({"unchecked"})
//    public <E> E findByNaturalKey(Class<E> entityClass, String propertyName, Object propertyValue) {
//        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
//        query.add(Restrictions.eq(propertyName, propertyValue));
//        return (E) query.setCacheable(true).uniqueResult();
//    }
//
    public <E> E get(Class<E> entityClass, Object id) {
        return em.find(entityClass, id);
    }

    //    public void remove(IdEntity object) {
//        IdEntity merged = get(object.getClass(), object.getId());
//        factory.getCurrentSession().delete(merged);
//        factory.getCurrentSession().flush();
//    }
//
    @Override
    public <E> E merge(E object) {
        E result = em.merge(object);
        em.flush();
        return result;
    }

    @Override
    public String getAccountAmountPrintable(int id) {
        float val = get(Account.class, id).getValue();
        java.text.NumberFormat f = new DecimalFormat("#,###.##");
        return f.format(val);
    }

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
        TypedQuery<Transaction> q = em.createQuery("from az.his.persist.Transaction", Transaction.class);
        return q.getResultList();
    }

    @Override
    public List<Transaction> getTransactionsFiltered(Date from, Date to, int category) {
        String q = "from az.his.persist.Transaction where timestmp >= :from and timestmp <= :to"
                + ((category > 0) ? " and category = :cat" : "");
        TypedQuery<Transaction> query = em.createQuery(q, Transaction.class)
                .setParameter("from", from, TemporalType.DATE)
                .setParameter("to", to, TemporalType.DATE);
        if (category > 0) {
            query.setParameter("cat", em.find(TransactionCategory.class, category));
        }
        return query.getResultList();
    }

    @Override
    public List<TransactionCategory> getTransactionCategories(TransactionCategory.CatType type) {
        return em.createQuery("from az.his.persist.TransactionCategory where type = :type", TransactionCategory.class)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public Account getAccountById(int id) {
        return em.find(Account.class, id);
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public TransactionCategory getTransCategory(int id) {
        return em.find(TransactionCategory.class, id);
    }

//
//    @SuppressWarnings({"unchecked"})
//    public <E> List<E> findByProperty(Class<E> entityClass, String propertyName, Object propertyValue) {
//        Criteria query = factory.getCurrentSession().createCriteria(entityClass);
//        query.add(Restrictions.eq(propertyName, propertyValue));
//        return query.list();
//    }
}
