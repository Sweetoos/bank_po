package model;

import java.util.List;

public class Client {
    public String clientId;
    public String name;
    public String address;
    public List<Account> accounts;

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account getAccount(String accountId) {
        if(accountId==null||accountId.trim().isEmpty()){
            return null;
        }

        for(Account account: this.accounts){
            if(account.getAccountId().equals(accountId)){
                return account;
            }
        }

        return null;
    }
}
