package az.his.persist;

import javax.persistence.*;
import java.util.Date;

/**
 * Personal Budget Limit
 */
@Entity(name = "personallimit")
public class PersonalBudgetLimit {
    private int id;
    private long value;
    private Date timestmp;
    private User user;

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

    @Temporal(TemporalType.DATE)
    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
