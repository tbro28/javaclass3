package edu.uw.tjb.account;

import edu.uw.ext.framework.account.*;
import edu.uw.ext.framework.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


/**
 * Implementation of the Account interface as a JavaBean.
 *
 * @author Tim
 */
public final class SimpleAccount implements Account {

    private Logger log = LoggerFactory.getLogger(SimpleAccount.class);
    private String name = "";
    private int balance;
    private String fullName = "";
    private Address address;
    private String phone = "";
    private String email = "";
    private CreditCard creditCard;

    private byte[] passwordHash;

    /**
     * Russ implemented:
     * transient - to not serialize out.
     * used optional??? like:
     * private transient Optional<AccountManager> accountManager = Optional.empty();
     *
     * Responsible for managing the account.
     */
    private AccountManager accountManager;
    //private transient Optional<AccountManager> accountManager = Optional.empty();

    /**
     * No parameter constructor, required by JavaBeans.
     */
    public SimpleAccount() {
    }

    /**
     * Constructor, validates length of account name and the initial balance.
     *
     * @param acctName
     * @param passwordHash
     * @param balance
     * @throws AccountException
     */
    public SimpleAccount(String acctName, byte[] passwordHash, int balance) throws AccountException {

        if(acctName.length() < 8 || balance < 100000)
            throw new edu.uw.ext.framework.account.AccountException("Account or Balance values were invalid.");

        this.name = acctName;
        this.balance = balance;
        setPasswordHash(passwordHash);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String s) throws AccountException {
        if(s.length() < 8 || s == null)
            throw new edu.uw.ext.framework.account.AccountException("Account is less than 8 characters.");
        name = s;
        if(accountManager != null)
            accountManager.persist(this);
    }

    /**
     * Russ returned a copy, so that it couldn't directly be changed in the object.
     * That way it can't directly be changed.
     *
     * @return
     */
    @Override
    public byte[] getPasswordHash() {
        return passwordHash;
    }

    /**
     * This has to create the hash right?
     *
     * @param bytes
     */
    @Override
    public void setPasswordHash(byte[] bytes) {
        passwordHash = bytes;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setBalance(int i) {
        balance = i;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String s) {
        fullName = s;
        try {
            accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String s) {
        phone = s;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String s) {
        email = s;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CreditCard getCreditCard() {
        return creditCard;
    }

    @Override
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the account manager responsible for persisting/managing this account. This may
     * be invoked exactly once on any given account, any subsequent invocations should be
     * ignored.
     *
     * This is at least one place where using Optional helps.  The isPresent().
     *
     * Russ used the consumer interface.
     * Page 711 in Murach programming book.
     *
     * @param accountManager
     */
    @Override
    public void registerAccountManager(AccountManager accountManager) {
        //if(!this.accountManager.equals(null))
        if( this.accountManager != null && accountManager != null )
        //if(!this.accountManager.isPresent()) {
            //this.accountManager = Optional.ofNullable(accountManager);
        {
            this.accountManager = accountManager;
            try {
                this.accountManager.persist(this);
            } catch (AccountException e) {
                e.printStackTrace();
            }
        }
        else {
            log.info("Account manager has already been set.");
        }
    }

    /**
     * Incorporates the effect of an order in the balance. Increments or decrements the
     * account balance by the execution price * number of shares in the order and then
     * persists the account, using the account manager.
     *
     * @param order
     * @param i
     */
    @Override
    public void reflectOrder(Order order, int i) {
        balance += order.valueOfOrder(i);
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }
}

