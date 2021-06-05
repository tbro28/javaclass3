package edu.uw.tjb.account;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactoryException;
import edu.uw.tjb.dao.FileAccountDao;
import edu.uw.tjb.dao.FileDaoFactory;

import java.nio.charset.StandardCharsets;

public class Running {

    public static void main(String args[]) throws DaoFactoryException, AccountException {

        System.out.println("Yo");

        FileDaoFactory fileDaoFactory = new FileDaoFactory();
        SimpleAccountManagerFactory simpleAccountManagerFactory = new SimpleAccountManagerFactory();
        AccountDao accountDao;
        accountDao = fileDaoFactory.getAccountDao();
        AccountManager accountManager = simpleAccountManagerFactory.newAccountManager(accountDao);
        accountManager.createAccount("TimAccount", "16363638", 100000);





/*        SimpleAccountFactory simpleAccountFactory = new SimpleAccountFactory();
        byte arr[] = new byte[] {1, 6, 3, 6, 3, 6, 3, 8};*/
        //AccountManager accountManager = simpleAccountFactory.newAccount("TimAccount", arr, 100000);

/*
        SimpleAccountFactory simpleAccountFactory = new SimpleAccountFactory();

        byte arr[] = new byte[] {1, 6, 3, 6, 3, 6, 3, 8};

        Account account = simpleAccountFactory.newAccount("TimAccount", arr, 100000);
        Account newAccount = new SimpleAccount();
        SimpleAccountManagerFactory simpleAccountManagerFactory = new SimpleAccountManagerFactory();

        FileDaoFactory fileDaoFactory = new FileDaoFactory();

        AccountDao accountDao;
        accountDao = fileDaoFactory.getAccountDao();
        AccountManager accountManager = simpleAccountManagerFactory.newAccountManager(accountDao);
        accountManager.createAccount("TimAccount", "16363638", 100000);


*/
/*        try {
            newAccount = accountManager.getAccount("TimAccount");
        } catch (AccountException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*//*


        System.out.println(account.getPasswordHash());
        System.out.println(newAccount.getPasswordHash());



        String s = new String(arr, StandardCharsets.UTF_8);

        //System.out.println(accountManager.validateLogin("TimAccount" , s));

*/


    }

}
