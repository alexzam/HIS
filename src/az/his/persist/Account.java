package az.his.persist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

/**
 * Account to track. In v.0.1 there is one.
 */
@Entity(name = "account")
public class Account {
    public static final int ACC_COMMON = 1;
    private int id;
    private float value;
    private String name;
    private Set<Transaction> transactions;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

//    @OneToMany(mappedBy = "account")
//    public Set<Transaction> getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(Set<Transaction> transactions) {
//        this.transactions = transactions;
//    }

    public void setName(String name) {
        this.name = name;
    }
}
