package edu.uw.tjb.dao;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.tjb.account.SimpleAccount;
import edu.uw.tjb.account.SimpleAccountFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileAccountDao implements AccountDao {

    private Logger log = LoggerFactory.getLogger(FileAccountDao.class);

    //SimpleAccountFactory account = new SimpleAccountFactory();
    //SimpleAccount account;


/*    public Account createAccount(String accountName, String password, int balance) throws AccountException {

        SimpleAccount account = new SimpleAccount(accountName, password.getBytes(StandardCharsets.UTF_8), balance);
        //account.newAccount(accountName, password.getBytes(StandardCharsets.UTF_8), balance);
        return account;
    }*/


    private File targetDir = new File("target", "accounts");


    /**
     * Lookup an account in the HashMap based on username.
     *
     * @param s
     * @return
     */
    @Override
    public Account getAccount(String s) {

        Account retrievedAccount = null;

        //target/accounts/s
        File accountDir = new File(targetDir, s);

        if (!accountDir.exists()) {

            File in = new File(accountDir, "accountFile");

            if(in.exists()) {

                try (FileInputStream fileIn = new FileInputStream(in)) {
                    retrievedAccount = AccountSer.read(fileIn);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //Intellij suggested assert for some reason versus an if statement.
            in = new File(accountDir, "addressFile");

            if(in.exists()) {

                try (FileInputStream fileIn = new FileInputStream(in)) {
                    if (in != null)
                        retrievedAccount.setAddress(AddressSer.read(fileIn));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            in = new File(accountDir, "creditCardFile");

            if(in.exists()) {
                try (FileInputStream fileIn = new FileInputStream(in)) {
                    if (in != null)
                        retrievedAccount.setCreditCard(CreditCardSer.read(fileIn));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


return retrievedAccount;



        /**
         * Test code
         */

/*
        try {
            //this needs to pass the hash in order to test:
            return new SimpleAccount("TimAccount", new byte[] {1, 6, 3, 6, 3, 6, 3, 8},
            800000) ;
        } catch (AccountException e) {
            e.printStackTrace();
        }

        return new SimpleAccount();
*/











    }

    /**
     * Adds or updates an account.
     *
     * @param account
     * @throws AccountException
     */
    @Override
    public void setAccount(Account account) throws AccountException {

        File accountDir = new File(targetDir, account.getName());

        Address accountAddress = account.getAddress();
        CreditCard accountCC = account.getCreditCard();

        if (! accountDir.exists()) {
            accountDir.mkdir();
        } else
            throw new AccountException("Unable to create account directory.");


        File out = new File(accountDir, "accountFile");
        try (FileOutputStream fileOut = new FileOutputStream(out)) {
            AccountSer.write(fileOut, account);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //may need to check for nulls for these:


        out = new File(accountDir, "addressFile");
        try (FileOutputStream fileOut = new FileOutputStream(out)) {
            AddressSer.write(fileOut, accountAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out = new File(accountDir, "creditCardFile");
        try (FileOutputStream fileOut = new FileOutputStream(out)) {
            CreditCardSer.write(fileOut, accountCC);
        } catch (IOException e) {
            e.printStackTrace();
        }


/*
        if (accountAddress != null) {
        }
*/





    }


    /**
     * Remove the account.
     *
     * @param s
     * @throws AccountException
     */
    @Override
    public void deleteAccount(String s) throws AccountException {
        File accountDir = new File(targetDir, s);

        if (accountDir.exists()) {
            accountDir.delete();
        } else
            throw new AccountException("Unable to delete account directory.");
    }


    /**
     * Remove all accounts.
     *
     * @throws AccountException
     */
    @Override
    public void reset() throws AccountException {
        if(targetDir.exists())
            targetDir.delete();
    }


    /**
     * Close the DAO.
     *
     * @throws AccountException
     */
    @Override
    public void close() throws AccountException {

    }

/*
    */
/**
     * Used to persist an account.
     * @param account
     *//*

    public void persist(edu.uw.ext.framework.account.Account account) {

    }

    */
/**
     * Check whether a login is valid.
     * @param accountName
     * @param password
     * @return
     *//*

    public boolean validateLogin(String accountName, String password) {
        return true;
    }


*/

}
