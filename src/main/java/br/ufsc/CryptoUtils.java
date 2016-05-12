package br.ufsc;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int MAC_SIZE = 128;

    public static String generateTableDPK(String password) {
        PBKDF2Util pbkdf2Util = new PBKDF2Util();
        String salt = "51d3a303e3e25f8aa143b0f7782550fe";
        int it = 10000;

        return pbkdf2Util.generateDerivedKey(password, salt, it);
    }

    public static String generateKey(String password) {
        PBKDF2Util pbkdf2Util = new PBKDF2Util();
        String salt = null;
        int it = 10000;

        try {
            salt = pbkdf2Util.getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pbkdf2Util.generateDerivedKey(password, salt, it);
    }

    public static String hMac(String password, String fileName) throws Exception {
        String keyString = generateTableDPK(password);
        Key key = new SecretKeySpec(keyString.getBytes(),0,keyString.getBytes().length, "DES");
        Mac hMac = Mac.getInstance("HMacSHA256", "BC");
        Key hMacKey = new SecretKeySpec(key.getEncoded(), "HMacSHA256");
        hMac.init(hMacKey);
        byte[] macText = hMac.doFinal(toByteArray(fileName));

        return toHex(macText);
    }

    public static String byteArrayToString(byte[] bytes) {
        char[] chars = new char[bytes.length];

        for (int i = 0; i != chars.length; i++) {
            chars[i] = (char)(bytes[i] & 0xff);
        }

        return new String(chars);
    }

    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++) {
            bytes[i] = (byte)chars[i];
        }

        return bytes;
    }

    public static String toHex(byte[] data) {
        String digits = "0123456789abcdef";
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i != data.length; i++) {
            int	v = data[i] & 0xff;

            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }

        return buf.toString();
    }

    public static IvParameterSpec generateIv() throws NoSuchProviderException, NoSuchAlgorithmException {

        byte iv[] = new byte[16];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, File inputFile,
                                 File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivSpec = generateIv();
            cipher.init(cipherMode, secretKey, ivSpec);

            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            if (cipherMode == Cipher.DECRYPT_MODE) {
                byte[] outputBytes = cipher.doFinal(inputBytes);

                // remove the iv from the start of the message
                byte[] plainOutputBytes = new byte[outputBytes.length - ivSpec.getIV().length];

                System.arraycopy(outputBytes, ivSpec.getIV().length, plainOutputBytes, 0, plainOutputBytes.length);
                outputStream.write(plainOutputBytes);
            } else {
                byte[] iv = ivSpec.getIV();
                byte[] outputBytes = new byte[cipher.getOutputSize(iv.length + inputBytes.length)];
                int ctLength = cipher.update(iv, 0, iv.length, outputBytes, 0);
                ctLength += cipher.update(inputBytes, 0, inputBytes.length, outputBytes, ctLength);
                cipher.doFinal(outputBytes, ctLength);

                outputStream.write(outputBytes);
            }

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException
                | InvalidAlgorithmParameterException | NoSuchProviderException
                | ShortBufferException ex) {
            throw new CryptoException("Erro cifrando/decifrando o arquivo", ex);
        }
    }

    public static String doCryptoWithGCM(boolean cryptMode, String password, String text, String storedIv) {
        String key = generateTableDPK(password);
        String result = "";
        try {
            GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
            byte[] iv = org.apache.commons.codec.binary.Hex.decodeHex(storedIv.toCharArray());;
            byte[] input;
            if (cryptMode) {
                input = text.getBytes();
            } else {
                input = Hex.decode(text);
            }
            byte[] keyBytes = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
            KeyParameter keyParameter = new KeyParameter(keyBytes);
            AEADParameters params = new AEADParameters(keyParameter, MAC_SIZE, iv);
            gcm.init(cryptMode, params);
            int outputSize = gcm.getOutputSize(input.length);
            byte[] output = new byte[outputSize];
            int lengthProcessedOutput = gcm.processBytes(input, 0, input.length, output, 0);
            gcm.doFinal(output, lengthProcessedOutput);
            if (cryptMode) {
                result = Hex.toHexString(output);
            } else {
                result = byteArrayToString(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
