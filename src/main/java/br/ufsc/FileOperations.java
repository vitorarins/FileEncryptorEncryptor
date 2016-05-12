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

    public static void encryptFiles(String dirName, String password) throws IOException {

        File tableFile = new File(dirName + File.separator + TABLE_FILE_NAME);
        if (!tableFile.exists()) {
            tableFile.createNewFile();
        }

        FETable feTable = new FETable(password);
        feTable.loadTableFromDir(dirName);
        feTable.createNewStoredIv();

        List<File> list = getAllFilenamesFromDir(dirName);

        for (File theFile : list) {
            if (!TABLE_FILE_NAME.equals(theFile.getName())) {
                String fileName = theFile.getPath();
                String key = feTable.getKeyFromFile(fileName);
                if (key == null) {
                    key = feTable.setKeyForFile(fileName);
                }
                EncryptFile.encryptFile(key, fileName);
                theFile.delete();
            }
        }

        feTable.writeTableToDir(dirName);
    }

    public static void decryptFiles(String dirName, String password) throws IOException {

        List<File> list = getAllFilenamesFromDir(dirName);

        FETable feTable = new FETable(password);
        feTable.loadTableFromDir(dirName);

        for (File theFile : list) {
            if (!TABLE_FILE_NAME.equals(theFile.getName())) {
                String fileName = cleanName(theFile.getPath());
                String key = feTable.getKeyFromFile(fileName);
                if (key != null) {
                    EncryptFile.decryptFile(key, theFile.getPath());
                    theFile.delete();
                }
            }
        }
    }

    public static String cleanName(String name) {
        int startOfWord = name.indexOf("encrypted");
        return name.substring(0, startOfWord) + name.substring(startOfWord + 10, name.length());
    }
}
