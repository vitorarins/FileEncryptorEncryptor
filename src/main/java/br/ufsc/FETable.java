package br.ufsc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FETable {

    private static final String TABLE_FILE_NAME = ".FETable";
    private static HashMap<String,String> keyTable;

    public static void loadTableFromFile(String dirName) {
        Path table = Paths.get(dirName + File.separator + TABLE_FILE_NAME);
        try {
            List<String> tableContent = Files.readAllLines(table, Charset.forName("UTF-8"));
            keyTable = readFromLines(tableContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> readFromLines(List<String> contentLines) {

        HashMap<String,String> contentTable = new LinkedHashMap<>();
        for (String contentLine : contentLines) {
            String[] content = contentLine.split(";");
            contentTable.put(content[0],content[1]);
        }

        return contentTable;
    }
}
