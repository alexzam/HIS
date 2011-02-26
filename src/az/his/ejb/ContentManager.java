package az.his.ejb;

import az.his.persist.Transaction;
import az.his.persist.User;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ContentManager {
    public void persist(Object object);

    public List<User> getAllUsers();

    List<Transaction> getAllTransactions();
}
