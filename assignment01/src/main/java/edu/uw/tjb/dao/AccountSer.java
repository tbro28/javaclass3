package edu.uw.tjb.dao;

import edu.uw.ext.framework.account.*;
import edu.uw.ext.framework.order.Order;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

public class AccountSer {

    private static Account account;

    /**
     * Reads an Account object from an input stream.
     * @param in
     * @return
     */
    public static edu.uw.ext.framework.account.Account read(InputStream in) {

        int passwordHashLength = 0;

        DataInputStream dataIn = new DataInputStream(in);

        try (ClassPathXmlApplicationContext classPathXmlApplicationContext =
                     new ClassPathXmlApplicationContext("context.xml")) {

            Account account = classPathXmlApplicationContext.getBean(Account.class);

            account.setName(dataIn.readUTF());
            account.setBalance(dataIn.readInt());

            passwordHashLength = dataIn.readInt();
            account.setPasswordHash(dataIn.readNBytes(passwordHashLength));

            account.setFullName(dataIn.readUTF());
            account.setPhone(dataIn.readUTF());
            account.setEmail(dataIn.readUTF());

        } catch (AccountException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return account;
    }

    /**
     * Writes an Account object to an output stream.
     * @param out
     * @param acct
     */
    public static void write(OutputStream out, edu.uw.ext.framework.account.Account acct) {

        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeUTF(acct.getName());
            dataOut.writeInt(acct.getBalance());  //table has password length? but no balance?
            dataOut.writeInt(acct.getPasswordHash().length);  //table has password length? but no balance?

            dataOut.write(acct.getPasswordHash());

            //if(acct.getFullName() == "")
            //set the default to "", an empty string.
            //tests may set it to null.
            dataOut.writeUTF(acct.getFullName());

            dataOut.writeUTF(acct.getPhone());

            dataOut.writeUTF(acct.getEmail());

            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
