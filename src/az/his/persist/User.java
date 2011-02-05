package az.his.persist;

import javax.persistence.*;

/**
 * User of the system. We know two of them.
 */
@Entity(name = "user")
public class User {
    private int id;
    private String Name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
