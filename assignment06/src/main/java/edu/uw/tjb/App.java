package edu.uw.tjb;

import edu.uw.ext.framework.crypto.PrivateMessageTriple;

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
     * plaintext - the data to be encrypted
     * senderKeyStoreName - the name of the sender's key store resource
     * senderKeyStorePasswd - the sender's key store password
     * senderKeyName - the alias of the sender's private key
???  * recipientCertFile - the file containing the recipient's public key certificate, as a resource path
     *
     *
     *      * decipher(
     *      * edu.uw.ext.framework.crypto.PrivateMessageTriple triple,
     *      * String recipientKeyStoreName,
     *      * char[] recipientKeyStorePasswd,
     *      * String recipientKeyName,
     *      * String signerCertFile)
     *
     * triple - the message containing the ciphertext, key and signature
     * recipientKeyStoreName - the name of the recipient's key store resource
     * recipientKeyStorePasswd - the recipient's key store password
     * recipientKeyName - the alias of the recipient's private key
     * signerCertFile - the name of the signer's certificate
     *
     *
     *
     * @param args
     */

    public static void main( String[] args ) throws NoSuchAlgorithmException {

        //System.out.println( "Hello World!" );


        PrivateMessageCodecImpl pmc = new PrivateMessageCodecImpl();

        //pmc.symKey();


        String value = "clientStorePass";
        char[] senderKeyStorePasswd = value.toCharArray();
        PrivateMessageTriple privateMessageTriple;

        try {
            privateMessageTriple = pmc.encipher("Hello World.".getBytes(StandardCharsets.UTF_8),
                    "clientKey.jck",
                    senderKeyStorePasswd,
                    "clientPrivKey",
                    "clientPubKey");


            value = "brokerStorePass";
            char[] receiverKeyStorePasswd = value.toCharArray();
            pmc.decipher(privateMessageTriple,
                    "brokerKey.jck",
                    receiverKeyStorePasswd,
                    "brokerPrivKey",
                    "clientCert.pem");





        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
