package br.ufsc;

import java.io.File;
import java.io.IOException;
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

    public FETable(String password) {
        this.password = password;
    }

    public HashMap<String, String> getKeyTable() {
        return keyTable;
    }

    public String getKeyFromFile(String fileName) {
        String fileNameMac = null;
        try {
            fileNameMac = CryptoUtils.hMac(password, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keyTable.containsKey(fileNameMac)) {
            return keyTable.get(fileNameMac);
        }

        return null;
    }

    public String setKeyForFile(String fileName) {
        String key = CryptoUtils.generateKey(password);
        String fileNameMac = null;
        try {
            fileNameMac= CryptoUtils.hMac(password, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        keyTable.put(fileNameMac, key);
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

    public void writeTableToDir(String dirName, String password) {
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

        return contentLines;
    }

    public HashMap<String,String> readFromLines(List<String> contentLines) {

        HashMap<String,String> contentTable = new LinkedHashMap<>();
        for (String contentLine : contentLines) {
            String[] content = contentLine.split(";");
            contentTable.put(content[0],content[1]);
        }

        return contentTable;
    }
}
