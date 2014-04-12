package az.his.persist;

import az.his.DBUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.DateType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Transaction to track.
 */
@Entity(name = "transaction")
public class Transaction implements DBListener {
    private int id;
    private int amount;
    private Date timestmp;
    private String comment;
    private User actor;
    private Account account;
    private TransactionCategory category;
    private boolean common;
    private boolean commonReady = false;
    private boolean amountReady = false;
    private Transaction pair;
    private Set<Transaction> pairs;
    private boolean transfer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amountReady && isPersistent()) {
        if (!common) {
            long val = getAccount().getValue() - this.amount + amount;
            getAccount().setValue(val);
        }
        }
        if (!amountReady && isPersistent()) amountReady = true;
        this.amount = amount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        // TODO WHAT?
        if (commonReady && isPersistent()) {
            if (!this.common && common) {
                // Do not include in acc sum
                getAccount().setValue(getAccount().getValue() - amount);
            } else if (this.common && !common) {
                getAccount().setValue(getAccount().getValue() + amount);
            }
        }
        if (!commonReady && isPersistent()) commonReady = true;
        this.common = common;
    }

    @Transient
    private boolean isPersistent() {
        return (getId() != 0);
    }

    @ManyToOne
    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    @ManyToOne
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @ManyToOne
    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "pair_id")
    public Transaction getPair() {
        return pair;
    }

    public void setPair(Transaction pair) {
        this.pair = pair;
    }

    @OneToMany(mappedBy = "pair")
    public Set<Transaction> getPairs(){
        return pairs;
    }

    public void setPairs(Set<Transaction> pairs) {
        this.pairs = pairs;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static List<Transaction> getFiltered(int accountId, Date from, Date to, Integer[] categories) {
        boolean catFilter = categories.length > 0;
        String q = "from az.his.persist.Transaction where account.id = :accId and timestmp >= :from and timestmp <= :to"
                + (catFilter ? " and category.id in (:cat)" : "");
        Query query = DBUtil.getCurrentSession().createQuery(q)
                .setParameter("from", from, DateType.INSTANCE)
                .setParameter("to", to, DateType.INSTANCE)
                .setParameter("accId", accountId);
        if (catFilter) {
            query.setParameterList("cat", categories);
        }
        return (List<Transaction>) query.list();
    }

    public void setAmount(double amount) {
        setAmount((int) Math.round(amount * 100));
    }

    @Override
    public void beforeDelete(Session session) {
        // Fix account sum
        if (!common)
            getAccount().setValue(getAccount().getValue() - amount);

        // Delete empty category
        if (getCategory().getType() != TransactionCategory.CatType.NONE) {
            Long res = (Long) session.createQuery("select count(id) from transaction where category = ?")
                    .setEntity(0, getCategory())
                    .uniqueResult();
            if (res <= 1) session.delete(getCategory());
        }
    }

    @Override
    public void beforeInsert(Session session) {
        if (!common){
            account.setValue(account.getValue() + amount);
        }
    }
}
