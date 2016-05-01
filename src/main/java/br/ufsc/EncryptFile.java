package br.ufsc;

import java.io.File;
import java.util.Scanner;

public class EncryptFile {

    public static void encryptFile(String key, String fileName) {

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        File inputFile = new File(fileName);
        File encryptedFile = new File(fileNameWithoutExtension + ".encrypted" + fileExtension);

        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void decryptFile(String key, String fileName) {

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        int firstDotIndex = fileName.indexOf('.');
        String fileNameWithoutExtension = fileName.substring(0, fileName.indexOf('.',firstDotIndex+1));
        File inputFile = new File(fileName);
        File decryptedFile = new File(fileNameWithoutExtension + fileExtension);

        try {
            CryptoUtils.decrypt(key, inputFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
