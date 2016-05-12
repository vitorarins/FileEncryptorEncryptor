package br.ufsc;

import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FETable {

    private final String TABLE_FILE_NAME = ".FETable";
    private final String password;
    private HashMap<String,String> keyTable;
    private String storedIv;

    public FETable(String password) {
        this.password = password;
    }

    public HashMap<String, String> getKeyTable() {
        return keyTable;
    }

    public void createNewStoredIv() {
        try {
            IvParameterSpec ivSpec = CryptoUtils.generateIv();
            storedIv = CryptoUtils.toHex(ivSpec.getIV());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKeyFromFile(String fileName) {
        String fileNameMac = null;
        try {
            fileNameMac = CryptoUtils.hMac(password, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keyTable.containsKey(fileNameMac)) {
            String encryptedKey = keyTable.get(fileNameMac);
            return CryptoUtils.doCryptoWithGCM(false,password,encryptedKey,storedIv);
        }

        return null;
    }

    public String setKeyForFile(String fileName) {
        String key = CryptoUtils.generateKey(password);
        String encryptedKey = null;
        String fileNameMac = null;
        try {
            fileNameMac= CryptoUtils.hMac(password, fileName);
            encryptedKey = CryptoUtils.doCryptoWithGCM(true,password,key,storedIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        keyTable.put(fileNameMac, encryptedKey);
        return key;
    }

    public void loadTableFromDir(String dirName) {
        Path table = Paths.get(dirName + File.separator + TABLE_FILE_NAME);
        try {
            List<String> tableContent = Files.readAllLines(table, Charset.forName("UTF-8"));
            keyTable = readFromLines(tableContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeTableToDir(String dirName) {
        Path table = Paths.get(dirName + File.separator + TABLE_FILE_NAME);
        try {
            List<String> tableContent = writeFromTable(keyTable);
            Files.write(table, tableContent, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> writeFromTable(HashMap<String,String> table) {
        List<String> contentLines = new ArrayList<>();
        for (String fileName : table.keySet()) {
            contentLines.add(fileName + ";" + table.get(fileName));
        }
        contentLines.add(storedIv);

        return contentLines;
    }

    public HashMap<String,String> readFromLines(List<String> contentLines) {

        HashMap<String,String> contentTable = new LinkedHashMap<>();
        for (String contentLine : contentLines) {
            if (contentLine.contains(";")) {
                String[] content = contentLine.split(";");
                contentTable.put(content[0], content[1]);
            } else {
                storedIv = contentLine;
            }
        }

        return contentTable;
    }
}
