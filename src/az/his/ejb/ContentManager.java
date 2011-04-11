package az.his.ejb;

import az.his.persist.Account;
import az.his.persist.Transaction;
import az.his.persist.TransactionCategory;
import az.his.persist.User;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface ContentManager {
    public void persist(Object object);

    public List<User> getAllUsers();

    List<Transaction> getAllTransactions();

    List<TransactionCategory> getTransactionCategories(TransactionCategory.CatType type);

    Account getAccountById(int id);

    User getUserById(int id);

    TransactionCategory getTransCategory(int id);

    <E> E merge(E object);

    String getAccountAmountPrintable(int id);

    @SuppressWarnings({"unchecked"})
    <E> List<E> findAll(Class<E> entityClass);

    List<Transaction> getTransactionsFiltered(Date from, Date to, int category);
}
