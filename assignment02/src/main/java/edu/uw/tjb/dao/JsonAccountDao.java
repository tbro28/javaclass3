package edu.uw.tjb.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * An AccountDao that persists the account information in a file.
 *
 * https://www.baeldung.com/jackson
 * https://www.baeldung.com/jackson-object-mapper-tutorial
 *
 *
 * @author Russ Moul
 */
public final class JsonAccountDao implements AccountDao {

    /** The logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(JsonAccountDao.class);

    /** The accounts directory. */
    private final File accountsDir = new File("target", "accounts");

    /**
     * Lookup an account in the HashMap based on username.
     *
     * @param accountName the name of the desired account
     *
     * @return the account if located otherwise null
     */
	@Override
    public Account getAccount(final String accountName) {
        Account account = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //final File accountDir = new File(accountsDir, accountName);

        logger.info("HERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRE");

        if (accountsDir.exists() && accountsDir.isDirectory()) {
        	
            try {


                account = objectMapper.readValue("target/accounts/"+accountName+".json", Account.class);

            } catch (final IOException ex) {
                logger.warn(String.format("Unable to access or read account data, '%s'", accountName),
                            ex);
            }
        }
        return account;
    }

    /**
     * Adds or updates an account.
     *
     * @param account the account to add/update
     *
     * @exception AccountException if operation fails
     */
	@Override
    public void setAccount(final Account account) throws AccountException {
        try {
            final File outFile = new File(accountsDir, account.getName()+".json");
            final Address address = account.getAddress();
            final CreditCard card = account.getCreditCard();

            System.out.println(outFile.getPath()+"<----------------------------------------------");

            deleteFile(accountsDir);
            if (!accountsDir.exists()) {
                final boolean success = accountsDir.mkdirs();
                if (!success) {
                    throw new AccountException(
                            String.format("Unable to create account diretory, %s", accountsDir.getAbsolutePath()));
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("target/accounts/"+account.getName()+".json"), account);

        } catch (final IOException ex) {
            throw new AccountException("Unable to store account(s).", ex);
        }
    }

    /**
     * Remove the account.
     *
     * @param accountName the name of the account to remove
     *
     * @exception AccountException if operation fails
     */
	@Override
    public void deleteAccount(final String accountName)
        throws AccountException {
        deleteFile(new File(accountsDir, accountName+".json"));
    }

    /**
     * Remove all accounts.  This is primarily available to facilitate testing.
     *
     * @exception AccountException if operation fails
     */
	@Override
    public void reset() throws AccountException {
        deleteFile(accountsDir);
    }

    /**
     * Utility method to delete a file or directory.  Recursively delete
     * directory contents and then the directory itself.
     *
     * @param file the account file or directory to delete
     */
    private void deleteFile(final File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                final File[] files = file.listFiles();
                if (files != null) {
                    for (File currFile : files) {
                        deleteFile(currFile);
                    }
                } else {
                    logger.warn(String.format("File inexplicably not a directory, %s", file.getAbsolutePath()));
                }
            }

            try {
				Files.delete(file.toPath());
			} catch (IOException e) {
                logger.warn(String.format("File deletion failed, %s", file.getAbsolutePath()), e);
			}
        }
    }


    /**
     * Close the DAO.
     */
	@Override
    public void close() {
        // no-op
    }
}

