package az.his.clientdto;

import az.his.persist.Account;

@SuppressWarnings("UnusedDeclaration")
public class AccountDto {
    private final String name;
    private final int id;
    private final long val;
    private final String privacy;

    public AccountDto(Account account) {
        id = account.getId();
        name = account.getName();
        val = account.getValue();

        if(account.isPublic()) privacy = "C";
        else if(account.isHidden()) privacy = "H";
        else privacy = "P";
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public long getVal() {
        return val;
    }

    public String getPrivacy() {
        return privacy;
    }
}
