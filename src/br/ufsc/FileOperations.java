package br.ufsc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vitor on 4/29/16.
 */
public class FileOperations {

    public static List<File> getAllFilesFromDir(String dirName) {

        File directory = new File(dirName);
        List<File> listOfFiles = new ArrayList<>(Arrays.asList(directory.listFiles()));

        for (int i = 0; i < listOfFiles.size(); i++) {
            if (listOfFiles.get(i).isDirectory()) {
                listOfFiles.addAll(getAllFilesFromDir(dirName + "/" + listOfFiles.get(i).getName()));
                listOfFiles.remove(i);
            }
        }

        return listOfFiles;
    }

    public static void initTable(String dirName) {

        List<File> list = getAllFilesFromDir(dirName);

        for (File theFile : list) {
            System.out.println(theFile.getName());
        }
    }
}
