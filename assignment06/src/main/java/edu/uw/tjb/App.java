package edu.uw.tjb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Hello world!
 *
 */
public class App 
{


    /**
     *      * encipher(
     *      * byte[] plaintext,
     *      * String senderKeyStoreName,
     *      * char[] senderKeyStorePasswd,
     *      * String senderKeyName,
     *      * String recipientCertFile)
     *
     *      * decipher(
     *      * edu.uw.ext.framework.crypto.PrivateMessageTriple triple,
     *      * String recipientKeyStoreName,
     *      * char[] recipientKeyStorePasswd,
     *      * String recipientKeyName,
     *      * String signerCertFile)
     *
     *
     * @param args
     */

    public static void main( String[] args ) throws NoSuchAlgorithmException {

        //System.out.println( "Hello World!" );


        PrivateMessageCodecImpl pmc = new PrivateMessageCodecImpl();

        pmc.symKey();


        String value = "clientStorePass";
        char[] senderKeyStorePasswd = value.toCharArray();

        try {
            pmc.encipher("Hello World.".getBytes(StandardCharsets.UTF_8), "clientKey.jck", senderKeyStorePasswd, "clientPrivKey", "brokerCert.pem");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
