package az.his.persist;

import az.his.DBUtil;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Account to track. In v.0.1 there is one.
 */
@Entity(name = "account")
public class Account {
    private int id;
    private long value;
    private String name;
    private Set<Transaction> transactions;
    private User owner;
    private boolean isPublic;

    public static final Serializable COMMON_ACC = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "account")
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Transient
    public String getAmountPrintable() {
        return DBUtil.formatCurrency(getValue());
    }

    @Transient
    public static Account getCommon(ApplicationContext context) {
        DBUtil dbUtil = DBUtil.getInstance(context);
        return dbUtil.get(Account.class, Account.COMMON_ACC);
    }

    @Transient
    public long getTotalExp(ApplicationContext context) {
        Session session = DBUtil.getCurrentSession(context);
        Long out = (Long) session.createQuery("select sum(amount) from transaction where common = true").uniqueResult();
        if (out == null) out = 0l;
        else out = -out;
        return out;
    }
}
