package edu.uw.tjb;

import edu.uw.ext.framework.crypto.PrivateMessageCodec;
import edu.uw.ext.framework.crypto.PrivateMessageTriple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;


/**
 * Implementation of PrivateMessageCodec that uses
 * a 128 bit AES key to encrypt the data.
 *
 * All keystores are of the PKCS12 type.
 */
public class PrivateMessageCodecImpl implements PrivateMessageCodec {


    private static final Logger logger =
            LoggerFactory.getLogger(PrivateMessageCodecImpl.class);

    //private static String symAlgorithm = "AES";
    //private static byte[] symKeyValue = "0123456789012345".getBytes(StandardCharsets.UTF_8);

    private static final String ALGORITHM = "AES";
    private SecureRandom rand = new SecureRandom();
    private SecretKey symKey;



    public SecretKey symKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128, rand); // for example
        SecretKey secretKey = keyGen.generateKey();

        System.out.println(secretKey);

        return secretKey;
    }


    public void generateSymKey() {

    }



    /**
     * Enciphers the provided data.
     *
     * encipher(
     * byte[] plaintext,
     * String senderKeyStoreName,
     * char[] senderKeyStorePasswd,
     * String senderKeyName,
     * String recipientCertFile)
     *
     * @param plaintext
     * @param senderKeyStoreName
     * @param senderKeyStorePasswd
     * @param senderKeyName
     * @param recipientCertFile
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Override
    public PrivateMessageTriple encipher(byte[] plaintext,
                                         String senderKeyStoreName,
                                         char[] senderKeyStorePasswd,
                                         String senderKeyName,
                                         String recipientCertFile) throws GeneralSecurityException, IOException {

        PrivateMessageTriple privateMessageTriple = null;

        //Generate the sym key.
        symKey = this.symKey();

        //Encode the data.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, symKey);
        byte[] encryptedData = cipher.doFinal(plaintext);

        //Store the key (needed for decrypting).
        byte[] keyBytes = symKey.getEncoded();

        //Test code
        String message = new String(plaintext, StandardCharsets.UTF_8);
        System.out.println(message);
        System.out.println(encryptedData);
        System.out.println(keyBytes);


        //Retrieve the (broker's) public key from the provided truststore
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyStore trustStore = KeyStore.getInstance("JCEKS");
        trustStore.load(new FileInputStream("src/main/resources/clientTrust.jck"), senderKeyStorePasswd);

        String clientTrustAlias = "clientPubKey";
        System.out.println("True??? " + trustStore.containsAlias(clientTrustAlias));

        Certificate certificate = trustStore.getCertificate(clientTrustAlias);
        //System.out.println(certificate);
        Key publicBrokerKey = certificate.getPublicKey();


        String value = "clientStorePass";
        char[] clientStorePasswd = value.toCharArray();
        Key maybe = trustStore.getKey(clientTrustAlias, clientStorePasswd);





        //Encipher the shared symmetric secret key's bytes using the public key from the certificate file
        // Encipher symkey with public broker key.
        Cipher cipherPublicBroker = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
        //cipher.init(Cipher.ENCRYPT_MODE, certificate);
        cipher.init(Cipher.ENCRYPT_MODE, maybe);
        byte[] encryptedSym = cipher.doFinal(symKey.getEncoded());










        Cipher cipherSym = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //Cipher cipherSym = Cipher.getInstance("RSA");

        //cipher.init(Cipher.ENCRYPT_MODE, certificate.getEncoded().\);
        cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
        cipher.init(Cipher.ENCRYPT_MODE, certificate);
        cipher.init(Cipher.ENCRYPT_MODE, publicBrokerKey);

        byte[] encryptedSymKey = cipher.doFinal(symKey.getEncoded());



        // Retrieve the (client's) private key from the the provided keystore
        KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyStore clientTrustStore = KeyStore.getInstance("JCEKS");
        trustStore.load(new FileInputStream("src/main/resources/clientKey.jck"), senderKeyStorePasswd);





        // Sign the plaintext order data using the private key from the the provided keystore

        // Construct and return a PrivateMessageTriple containing the ciphertext, key bytes and signature




        Key pubKey = null;



        //String alias and password for clientTrust?

        pubKey = trustStore.getKey("clientPubKey", senderKeyStorePasswd);
        System.out.println(pubKey);
        System.out.println(trustStore.isCertificateEntry(clientTrustAlias));

        pubKey = trustStore.getKey(clientTrustAlias, senderKeyStorePasswd);
        System.out.println("Here" + pubKey);




/*        Certificate cert = (Certificate) trustStore.getCertificate(clientTrustAlias);
        try {
            pubKey = cert.getPublicKey();
        } catch (NullPointerException e) {
            logger.info("Null exception: " + e.getMessage());
        }*/

        System.out.println(pubKey);









/*
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/clientTrust.jck")){  //keystore.ks       //is the file from where we want to load the file
            keyStore.load(fileInputStream, senderKeyStorePasswd);
        }

        try(InputStream keyStoreData = new FileInputStream("src/main/resources/clientTrust.jck")){  //keystore.ks       //is the file from where we want to load the file
            keyStore.load(keyStoreData, senderKeyStorePasswd);
        }
*/

/*
        byte[] brokerPublicKey;
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/clientTrust.jck"))
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

        KeyStore trustStore = KeyStoreUtil.loadKeyStoreResource("servertruststore.jck", "JCEKS",
                        SERVER_TRUSTSTORE_PASSWD);
        Key pubKey = null;
        String keyAlias = "client";
        if (trustStore.isCertificateEntry(keyAlias)) {
            Certificate cert = trustStore.getCertificate(keyAlias);
            pubKey = cert.getPublicKey()
*/









        return privateMessageTriple;
    }


    /**
     * Decipher the provided message.
     *
     * decipher(
     * edu.uw.ext.framework.crypto.PrivateMessageTriple triple,
     * String recipientKeyStoreName,
     * char[] recipientKeyStorePasswd,
     * String recipientKeyName,
     * String signerCertFile)
     *
     * @param triple
     * @param recipientKeyStoreName
     * @param recipientKeyStorePasswd
     * @param recipientKeyName
     * @param signerCertFile
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Override
    public byte[] decipher(PrivateMessageTriple triple,
                           String recipientKeyStoreName,
                           char[] recipientKeyStorePasswd,
                           String recipientKeyName,
                           String signerCertFile) throws GeneralSecurityException, IOException {






        return new byte[0];


    }






}
