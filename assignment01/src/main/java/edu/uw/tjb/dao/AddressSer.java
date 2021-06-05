package edu.uw.tjb.dao;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.Address;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class AddressSer {

    private static Address address;

    /**
     * Reads an Address object from an input stream.
     * @param in
     * @return
     */
    public static edu.uw.ext.framework.account.Address read(InputStream in) {

        DataInputStream dataIn = new DataInputStream(in);

        try (ClassPathXmlApplicationContext classPathXmlApplicationContext =
                     new ClassPathXmlApplicationContext("context.xml")) {

            Address address = classPathXmlApplicationContext.getBean(Address.class);

            address.setStreetAddress(dataIn.readUTF());
            address.setCity(dataIn.readUTF());
            address.setState(dataIn.readUTF());
            address.setZipCode(dataIn.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    /**
     * Writes an Address object to an output stream.
     * @param out
     * @param addr
     */
    public static void	write(OutputStream out, edu.uw.ext.framework.account.Address addr) {

        DataOutputStream dataOut = new DataOutputStream(out);

        try {
            dataOut.writeUTF(addr.getStreetAddress());
            dataOut.writeUTF(addr.getCity());
            dataOut.writeUTF(addr.getState());
            dataOut.writeUTF(addr.getZipCode());

            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
