package az.his.persist;

import javax.persistence.*;

@Entity
@Table(name = "tent")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String name;
}
