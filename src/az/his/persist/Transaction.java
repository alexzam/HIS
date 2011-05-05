package az.his.persist;

import az.his.DBUtil;
import org.hibernate.Query;
import org.hibernate.type.DateType;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Transaction to track.
 */
@Entity(name = "transaction")
public class Transaction {
    private int id;
    private float amount;
    private Date timestmp;
    private String comment;
    private User actor;
    private Account account;
    private TransactionCategory category;
    private boolean common;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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
        this.common = common;
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

    @Transient
    public JSONObject getJson() throws JSONException {
        JSONObject ret = new JSONObject();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        ret.put("id", getId());
        ret.put("amount", getAmount());
        ret.put("timestamp", df.format(getTimestmp()));
        ret.put("actor_id", getActor().getId());
        ret.put("actor_name", getActor().getName());
        ret.put("category_name", getCategory().getName());
        ret.put("comment", getComment());

        return ret;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static List<Transaction> getFiltered(Date from, Date to, int category) {
        String q = "from az.his.persist.Transaction where timestmp >= :from and timestmp <= :to"
                + ((category > 0) ? " and category = :cat" : "");
        Query query = DBUtil.getSession().createQuery(q)
                .setParameter("from", from, DateType.INSTANCE)
                .setParameter("to", to, DateType.INSTANCE);
        if (category > 0) {
            query.setParameter("cat", DBUtil.get(TransactionCategory.class, category));
        }
        return (List<Transaction>) query.list();
    }
}
