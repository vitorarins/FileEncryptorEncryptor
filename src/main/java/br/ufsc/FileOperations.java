package br.ufsc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileOperations {

    private static final String TABLE_FILE_NAME = ".FETable";

    public static List<File> getAllFilenamesFromDir(String dirName) {

        File directory = new File(dirName);
        List<File> listOfFiles = new ArrayList<>(Arrays.asList(directory.listFiles()));
        List<File> filesToRemove = new ArrayList<>();
        List<File> filesToAdd = new ArrayList<>();
        for (File file: listOfFiles) {
            if (file.isDirectory()) {
                filesToAdd.addAll(getAllFilenamesFromDir(dirName + File.separator + file.getName()));
                filesToRemove.add(file);
            }
            if (file.getName().equals(TABLE_FILE_NAME)) {
                filesToRemove.add(file);
            }
        }
        listOfFiles.addAll(filesToAdd);
        listOfFiles.removeAll(filesToRemove);

        return listOfFiles;
    }

    public static void encryptFiles(String dirName) throws IOException {

        File tableFile = new File(dirName + File.separator + TABLE_FILE_NAME);
        if (!tableFile.exists()) {
            tableFile.createNewFile();
        }

        List<File> list = getAllFilenamesFromDir(dirName);
        String key = "1234567890123456";

        for (File theFile : list) {
            if (!TABLE_FILE_NAME.equals(theFile.getName())) {
                EncryptFile.encryptFile(key, theFile.getAbsolutePath());
                theFile.delete();
            }
        }
    }

    public static void decryptFiles(String dirName) throws IOException {

        List<File> list = getAllFilenamesFromDir(dirName);
        String key = "1234567890123456";

        for (File theFile : list) {
            if (!TABLE_FILE_NAME.equals(theFile.getName())) {
                EncryptFile.decryptFile(key, theFile.getAbsolutePath());
                theFile.delete();
            }
        }
    }
}
