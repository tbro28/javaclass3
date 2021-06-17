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




    public SecretKey symKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128, rand);
        //keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        System.out.println("AES key: " + secretKey);
        System.out.println("Rand: " + rand);

        return secretKey;
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

        SecretKey symKey;

        //------------------------------------------------------
        //Generate a one-time use shared symmetric secret key
        //Generate the sym key.
        symKey = this.symKey();


        //------------------------------------------------------
        //Encipher the order data using the one-time use shared symmetric secret key
        //Encode the data.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, symKey);
        byte[] encryptedData = cipher.doFinal(plaintext);

        //------------------------------------------------------
        //Obtain the bytes representing the one-time use shared symmetric secret key
        //Store the key (needed for decrypting).
        byte[] keyBytes = symKey.getEncoded();

        //Test code
        String message = new String(plaintext, StandardCharsets.UTF_8);
        System.out.println(message);
        System.out.println(encryptedData);
        System.out.println(keyBytes);

        //------------------------------------------------------
        //Retrieve the (broker's) public key from the provided truststore
        //KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyStore trustStore = KeyStore.getInstance("JCEKS");
        trustStore.load(new FileInputStream("src/main/resources/clientTrust.jck"), senderKeyStorePasswd);
        //String clientTrustAlias = "clientPubKey";

        //Test code
        System.out.println("True??? " + trustStore.containsAlias(recipientCertFile));
        System.out.println("isCertificateEntry??? " + trustStore.isCertificateEntry(recipientCertFile));
        System.out.println("isKeyEntry??? " + trustStore.isKeyEntry(recipientCertFile));

        //Key shouldWork??? = trustStore.getKey(clientTrustAlias, clientStorePasswd);
        //PublicKey oxo = (PublicKey) maybe;
        //System.out.println(maybe.getEncoded());
        Certificate publicBrokerCertificate = trustStore.getCertificate(recipientCertFile);
        System.out.println(publicBrokerCertificate);
        //Key publicBrokerKey = certificate.getPublicKey();


        //------------------------------------------------------
        //Encipher the shared symmetric secret key's bytes using the public key from the certificate file
        // Encipher symkey with public broker key.

        //Cipher cipherPublicBroker = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //Cipher cipherPublicBroker = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
        //cipher.init(Cipher.ENCRYPT_MODE, certificate);
        //cipher.init(Cipher.ENCRYPT_MODE, maybe);
        byte[] encryptedSym = cipher.doFinal(symKey.getEncoded());

        Cipher cipherForSymKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //Cipher cipherSym = Cipher.getInstance("RSA");
        //cipher.init(Cipher.ENCRYPT_MODE, publicBrokerCertificate.getEncoded().\);
        //cipher.init(Cipher.ENCRYPT_MODE, publicBrokerCertificate.getPublicKey());
        cipherForSymKey.init(Cipher.ENCRYPT_MODE, publicBrokerCertificate);
        //cipher.init(Cipher.ENCRYPT_MODE, publicBrokerKey);

        byte[] encryptedSymKey = cipher.doFinal(symKey.getEncoded());


        //------------------------------------------------------
        // Retrieve the (client's) private key from the the provided keystore
        KeyStore clientKeyStore = KeyStore.getInstance("JCEKS");
        //KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //KeyStore clientTrustStore = KeyStore.getInstance("JCEKS");
        clientKeyStore.load(new FileInputStream("src/main/resources/" + senderKeyStoreName), senderKeyStorePasswd);

        String value = "clientStorePass";
        char[] clientStorePasswd = value.toCharArray();
        //Key clientPrivateKey = clientKeyStore.getKey("clientPrivKey", clientStorePasswd);
        PrivateKey clientPrivateKey = (PrivateKey) clientKeyStore.getKey(senderKeyName, clientStorePasswd);


        //------------------------------------------------------
        // Sign the plaintext order data using the private key from the the provided keystore
        String SIGNING_ALG = "SHA256withRSA";

        Signature signature = Signature.getInstance(SIGNING_ALG);
        signature.initSign(clientPrivateKey);
        signature.update(plaintext);

        byte[] clientSignature = signature.sign();


        //------------------------------------------------------
        // Construct and return a PrivateMessageTriple containing the ciphertext, key bytes and signature
        PrivateMessageTriple privateMessageTriple1 = new PrivateMessageTriple(encryptedSymKey, encryptedData, clientSignature);

        return privateMessageTriple1;

        //Key pubKey = null;
        //String alias and password for clientTrust?

        /*        pubKey = trustStore.getKey("clientPubKey", senderKeyStorePasswd);
                System.out.println(pubKey);
                System.out.println(trustStore.isCertificateEntry(clientTrustAlias));

                pubKey = trustStore.getKey(clientTrustAlias, senderKeyStorePasswd);
                System.out.println("Here" + pubKey);

                System.out.println(pubKey);*/

        /*        Certificate cert = (Certificate) trustStore.getCertificate(clientTrustAlias);
                try {
                    pubKey = cert.getPublicKey();
                } catch (NullPointerException e) {
                    logger.info("Null exception: " + e.getMessage());
                }*/

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


        //------------------------------------------------------
        //Obtain the shared secret key, order data ciphertext and signature from the provided PrivateMessageTriple
        byte[] cipherText = triple.getCiphertext();
        byte[] encipheredSharedKey = triple.getEncipheredSharedKey();
        byte[] signature = triple.getSignature();


        //------------------------------------------------------
        //Retrieve the (brokers's) private key from the the provided keystore
        KeyStore brokerKeyStore = KeyStore.getInstance("JCEKS");
        //KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //KeyStore clientTrustStore = KeyStore.getInstance("JCEKS");
        brokerKeyStore.load(new FileInputStream("src/main/resources/"+recipientKeyStoreName), recipientKeyStorePasswd);

        //String value = "clientStorePass";
        char[] brokerStorePasswd = recipientKeyStorePasswd;
        //Key clientPrivateKey = clientKeyStore.getKey("clientPrivKey", clientStorePasswd);
        PrivateKey brokerPrivateKey = (PrivateKey) brokerKeyStore.getKey(recipientKeyName, brokerStorePasswd);

        System.out.println(brokerPrivateKey);
        System.out.println("Ciphered sym key length: " + encipheredSharedKey.length);


        //------------------------------------------------------
        //Use the private key from the keystore to decipher the shared secret key's bytes

        Cipher cipherForDecryptSymKey = Cipher.getInstance("RSA");
        //Cipher cipherForDecryptSymKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipherForDecryptSymKey.init(Cipher.DECRYPT_MODE, brokerPrivateKey);
        //cipher.init(Cipher.ENCRYPT_MODE, publicBrokerKey);

        int encipheredSharedKeyLength = encipheredSharedKey.length * 8;

        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, encipheredSharedKeyLength-1);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, encipheredSharedKeyLength);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, 16);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, encipheredSharedKey.length-1);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, encipheredSharedKey.length);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 1, encipheredSharedKey.length);
        //byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey, 0, encipheredSharedKey.length);
        byte[] encryptedSymKey = cipherForDecryptSymKey.doFinal(encipheredSharedKey);


        //------------------------------------------------------
        //Reconstruct the shared secret key from shared secret key's bytes


        //------------------------------------------------------
        //Use the shared secret key to decipher the order data ciphertext


        //------------------------------------------------------
        //Retrieve the (client's) public key from the provided certificate file
        KeyStore trustStore = KeyStore.getInstance("JCEKS");
        trustStore.load(new FileInputStream("src/main/resources/brokerTrust.jck"), recipientKeyStorePasswd);
        Certificate publicClientCertificate = trustStore.getCertificate("brokerPubKey");
        System.out.println(publicClientCertificate);


        //------------------------------------------------------
        //Verify the order data plaintext and signature using the public key from the truststore


        //Cipher cipherForSymKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //cipherForSymKey.init(Cipher.ENCRYPT_MODE, publicClientCertificate);


        //------------------------------------------------------
        //Return the order data plaintext


        return new byte[0];
    }
}
