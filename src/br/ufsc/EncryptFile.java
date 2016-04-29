package br.ufsc;

import java.io.File;
import java.util.Scanner;

public class EncryptFile {
    public static void encryptFile(String key, String fileName) {

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        File inputFile = new File(fileName);
        File encryptedFile = new File(fileNameWithoutExtension + ".encrypted" + fileExtension);
        File decryptedFile = new File(fileNameWithoutExtension + ".decrypted" + fileExtension);

        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
