package edu.uw.tjb.account;

import edu.uw.ext.framework.account.*;
import edu.uw.ext.framework.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * Implementation of the Account interface as a JavaBean.
 *
 * @author Russ Moul
 */
public final class SimpleAccount implements Account {

    /** Version id */
    private static final long serialVersionUID = 9028406835809215441L;

    /** The logger to be used by this class */
    private static final Logger logger = LoggerFactory.getLogger(SimpleAccount.class);

    /** The minimum allowed account length */
    private static final int MIN_ACCT_LEN = 8;

    /** The minimum allowed initial account balance */
    private static final int MIN_ACCT_BALANCE = 100_000;

    /** The account name */
    private String name;

    /** The hashed account password */
    private byte[] passwordHash;

    /** The account balance, in cents. */
    private int balance = Integer.MIN_VALUE;

    /** The account holders full name */
    private String fullName;

    /** The account holders address */
    private Address address;

    /** The account holders phone number */
    private String phone;

    /** The account holders email address */
    private String email;

    /** The account holders credit card */
    private CreditCard creditCard;

    /** Account manager responsible for managing this account */
    private AccountManager accountManager;
    //private transient Optional<AccountManager> acctMngr = Optional.empty();

    /**
     * No parameter constructor, required by JavaBeans.
     */
    public SimpleAccount() {
    }

    /**
     * Constructor, validates length of account name and the initial balance.
     *
     * @param acctName the account name
     * @param passwordHash the password hash
     * @param balance the balance
     *
     * @throws AccountException if the account name is too short or balance
     *                          too low
     */
    public SimpleAccount(final String acctName, final byte[] passwordHash,
                         final int balance)
        throws AccountException {

        if (balance < MIN_ACCT_BALANCE) {
            final String msg = String.format("Account creation failed for , account '%s' minimum balance not met, %d",
                                             acctName, balance);
            logger.warn(msg);
            throw new AccountException(msg);
        }

        setName(acctName);
        setPasswordHash(passwordHash);
        this.balance = balance;
    }

    /**
     * Sets the account manager responsible for persisting/managing this
     * account. This may be invoked exactly once on any given account, any
     * subsequent invocations should be ignored.
     *
     * @param accountManager the account manager
     */
	@Override
    public void registerAccountManager(final AccountManager accountManager) {
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
            logger.info("Account manager has already been set.");
        }
    }

	/**
	 * Consumer method use to persist the account.
	 * @param accountManager the account manager used to persist the account
	 */
	private void persistConsumer(final AccountManager accountManager) {
		try {
			accountManager.persist(this);
		} catch (AccountException ex) {
            logger.error(String.format("Failed to persist account %s.", name), ex);
		}
	}

    /**
     * Get the account name.
     *
     * @return the name of the account
     */
	@Override
    public String getName() {
        return name;
    }

    /**
     * Sets the account name.  The name will be checked for minimum length MIN_ACCT_LEN..
     *
     * This operation is not generally used but is provided for JavaBean
     * conformance.
     *
     * @param s the value to be set for the account name
     *
     * @throws AccountException if the account name is to short
     */
	@Override
    public void setName(final String s) throws AccountException {
        if(s.length() < 8 || s == null)
            throw new edu.uw.ext.framework.account.AccountException("Account is less than 8 characters.");
        name = s;
        if(accountManager != null)
            accountManager.persist(this);
    }

    /**
     * Gets the hashed password.
     *
     * @return the hashed password
     */
	@Override
    public byte[] getPasswordHash() {
        byte[] copy = null;
        if (passwordHash != null) {
            copy = new byte[passwordHash.length];
            System.arraycopy(passwordHash, 0, copy, 0, passwordHash.length);
        }
        return copy;
    }

    /**
     * Sets the hashed password.
     *
     * @param bytes the value to be set for the password hash
     */
	@Override
    public void setPasswordHash(final byte[] bytes) {
        passwordHash = bytes;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the account balance.
     *
     * @return the current balance of the account
     */
	@Override
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the account balance.
     *
     * @param i the value to set the balance to
     */
	@Override
    public void setBalance(final int i) {
        balance = i;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the full name of the account holder.
     *
     * @return the account holders full name
     */
	@Override
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the account holder.
     *
     * @param s the account holders full name
     */
	@Override
    public void setFullName(final String s) {
        fullName = s;
        try {
            accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the account address.
     *
     *  @return the accounts address
     */
	@Override
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the account address.
     *
     *  @param address the address for the account
     */
	@Override
    public void setAddress(final Address address) {
        this.address = address;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the account phone number.
     *
     * @param s value for the account phone number
     */
	@Override
    public void setPhone(final String s) {
        phone = s;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
	@Override
    public String getEmail() {
        return email;
    }

    /**
     * Sets the account email address.
     *
     * @param s the email address
     */
	@Override
    public void setEmail(final String s) {
        email = s;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the account credit card.
     *
     * @return the credit card
     */
	@Override
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Sets the account credit card.
     *
     * @param card the value to be set for the credit card
     */
	@Override
    public void setCreditCard(final CreditCard card) {
        this.creditCard = creditCard;
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }

    /**
     * Incorporates the effect of an order in the balance.  Increments or
     * decrements the account balance by the execution price * number of shares
     * in the order and then persists the account, using the account manager.
     *
     * @param order the order to be reflected in the account
     * @param i the price the order was executed at
     */
	@Override
    public void reflectOrder(final Order order, final int i) {
        balance += order.valueOfOrder(i);
        try {
            if(accountManager != null)
                accountManager.persist(this);
        } catch (AccountException e) {
            e.printStackTrace();
        }
    }
}

