package edu.uw.tjb;

import edu.uw.ext.framework.crypto.PrivateMessageCodec;
import edu.uw.ext.framework.crypto.PrivateMessageTriple;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Implementation of PrivateMessageCodec that uses
 * a 128 bit AES key to encrypt the data.
 *
 * All keystores are of the PKCS12 type.
 */
public class PrivateMessageCodecImpl implements PrivateMessageCodec {


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
    public PrivateMessageTriple encipher(byte[] plaintext, String senderKeyStoreName, char[] senderKeyStorePasswd, String senderKeyName, String recipientCertFile) throws GeneralSecurityException, IOException {

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
    public byte[] decipher(PrivateMessageTriple triple, String recipientKeyStoreName, char[] recipientKeyStorePasswd, String recipientKeyName, String signerCertFile) throws GeneralSecurityException, IOException {






        return new byte[0];


    }






}
