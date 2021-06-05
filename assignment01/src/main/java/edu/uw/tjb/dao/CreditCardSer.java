package edu.uw.tjb.dao;

import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

public class CreditCardSer {

    private static CreditCard creditCard;

    /**
     * Reads a CreditCard object from an input stream.
     * @param in
     * @return
     */
    public static edu.uw.ext.framework.account.CreditCard read(InputStream in) {

        DataInputStream dataIn = new DataInputStream(in);

        try (ClassPathXmlApplicationContext classPathXmlApplicationContext =
                     new ClassPathXmlApplicationContext("context.xml")) {

            CreditCard creditCard = classPathXmlApplicationContext.getBean(CreditCard.class);

            creditCard.setIssuer(dataIn.readUTF());
            creditCard.setHolder(dataIn.readUTF());
            creditCard.setAccountNumber(dataIn.readUTF());
            creditCard.setAccountNumber(dataIn.readUTF());
            creditCard.setExpirationDate(dataIn.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return creditCard;
    }


    /**
     * Writes a CreditCard object to an output stream.
     * @param out
     * @param cc
     */
    public static void	write(OutputStream out, edu.uw.ext.framework.account.CreditCard cc) {

        DataOutputStream dataOut = new DataOutputStream(out);

        try {
            dataOut.writeUTF(cc.getIssuer());
            dataOut.writeUTF(cc.getType());
            dataOut.writeUTF(cc.getHolder());
            dataOut.writeUTF(cc.getAccountNumber());
            dataOut.writeUTF(cc.getExpirationDate());

            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
