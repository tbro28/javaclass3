package edu.uw.tjb.account;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;

/**
 * An implementation of AccountFactory that creates SimpleAccount instances.
 *
 * @author Tim
 */
public class SimpleAccountFactory implements AccountFactory {

/*    public SimpleAccountFactory() {
    }*/

    @Override
    public Account newAccount(String s, byte[] bytes, int i) {
        try {
            return new SimpleAccount(s, bytes, i);
        } catch (AccountException e) {
            e.printStackTrace();
        }

        return null;
    }
}
