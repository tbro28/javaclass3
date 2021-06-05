package edu.uw.tjb.account;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;
import edu.uw.ext.framework.account.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class SimpleAccountManager implements AccountManager {

    private Logger log = LoggerFactory.getLogger(SimpleAccountManager.class);

    private edu.uw.ext.framework.dao.AccountDao accountDao;  //????????????????????????This is the class right now, probably should
    //be the interface????Changed for now.

    //Russ made an account factory.
    private AccountFactory accountFactory;

    //private Account account = null;

    //private AccountManager accountManager;

    /**
     * Creates a new Simple account manager using the specified FileAccountDao for persistence.
     *
     * The dao will write the stuff out for persistence.
     *
     * @param dao
     */
    public SimpleAccountManager(edu.uw.ext.framework.dao.AccountDao dao) {

        accountDao = dao;
//        accountManager = new SimpleAccountManagerFactory().newAccountManager(dao);
        //account = new SimpleAccount();
    }

    /**
     * Used to persist an account.
     *
     * @param account
     * @throws AccountException
     */
    @Override
    public void persist(Account account) throws AccountException {
        accountDao.setAccount(account);
    }

    /**
     * Lookup an account based on accountName.
     *
     * @param accountName
     * @return
     * @throws AccountException
     */
    @Override
    public Account getAccount(String accountName) throws AccountException {
        Account account = accountDao.getAccount(accountName);

        if (account == null)
            throw new NullPointerException();

        account.registerAccountManager(this);

        return account;
    }

    /**
     * DeleteAccount in interface edu.uw.ext.framework.account.AccountManager
     *
     * @param accountName
     * @throws AccountException
     */
    @Override
    public void deleteAccount(String accountName) throws AccountException {
        accountDao.deleteAccount(accountName);
    }

    /**
     * createAccount in interface edu.uw.ext.framework.account.AccountManager
     *
     * @param accountName
     * @param password
     * @param balance
     * @return
     * @throws AccountException
     */
    @Override
    public Account createAccount(String accountName, String password, int balance) throws AccountException {

        //System.out.println(accountDao.getAccount(accountName));

        if(accountDao.getAccount(accountName) == null) {

            Account account = accountFactory.newAccount(accountName, this.createPasswordHash(password), balance);
/*            account.setName(accountName);
            account.setPasswordHash(this.createPasswordHash(password));
            account.setBalance(balance);*/

            account.registerAccountManager(this);

            return account;
        } else
            throw new AccountException("This account already exists.");
    }

    /**
     * Check whether a login is valid. AccountName must exist and password must match.
     *
     * @param accountName
     * @param password
     * @return
     * @throws AccountException
     */
    @Override
    public boolean validateLogin(String accountName, String password) throws AccountException {

        Account account = null;
        boolean valid = false;
        //byte[] bytePassword = password.getBytes();

        System.out.println("User attempted password: " + password);
        System.out.println("Current password: " + Arrays.toString(account.getPasswordHash()));
        System.out.println("Current password (what it will be): " + createPasswordHash(Arrays.toString(account.getPasswordHash())));

        System.out.println(account.getName());
        System.out.println(accountName);

        if( password.equals(accountDao.getAccount(accountName).getPasswordHash()) && validatePassword(createPasswordHash(password)) ) {
            valid = true;
        }

        return valid;
    }

    private byte[] createPasswordHash(String password){
        byte[] bytePassword = password.getBytes();
        MessageDigest md;
        byte[] passwordHash = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bytePassword);
            passwordHash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return passwordHash;

    }

    private boolean validatePassword(byte[] providedPassword){

        Account account = null;
        byte[] pPassword = null;
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(providedPassword);
            pPassword = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Arrays.equals(pPassword, account.getPasswordHash());
    }

    @Override
    public void close() throws AccountException {
        accountDao.close();
    }
}
