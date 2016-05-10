import br.ufsc.FileOperations;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileOperationsTest {

    private static final String ROOT_ENCRYPT_DIR = "dir2Encrypt" + File.separator;

    @Before
    public void setUpFiles() {
        try {
            // Instanciar um novo Security provider
            int addProvider = Security.addProvider(new BouncyCastleProvider());

            String treeOfDirs = ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions"
                    + File.separator + "lalalal" + File.separator + "bizarro";
            File dirToEncrypt = new File(treeOfDirs);
            dirToEncrypt.mkdirs();
            File otherDir = new File(ROOT_ENCRYPT_DIR + "otherDir");
            otherDir.mkdir();
            Path document = Paths.get(ROOT_ENCRYPT_DIR + "document.txt");
            Path otherTest = Paths.get(ROOT_ENCRYPT_DIR + "otherTest.txt");
            Path docCeption = Paths.get(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "docCeption.txt");
            Path lalala = Paths.get(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "lalala.txt");
            Path opa = Paths.get(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "opa.txt");
            Path testingText = Paths.get(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "testingText.txt");
            Path bem = Paths.get(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" +
                    File.separator + "bem.txt");
            Path otherFile = Paths.get(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" +
                    File.separator + "otherFile.txt");
            Path bizarr = Paths.get(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" +
                    File.separator + "lalalal" + File.separator + "bizarro" + File.separator + "bizarr.txt");
            List<String> linesOfDocument = Arrays.asList("abobrinha", "abobrinha");
            List<String> linesOfOtherDocuments = Arrays.asList("bananada lalal", "macaco", "martelos");
            Files.write(document, linesOfDocument, Charset.forName("UTF-8"));
            Files.write(otherTest, linesOfDocument, Charset.forName("UTF-8"));
            Files.write(docCeption, linesOfOtherDocuments, Charset.forName("UTF-8"));
            Files.write(lalala, linesOfDocument, Charset.forName("UTF-8"));
            Files.write(opa, linesOfOtherDocuments, Charset.forName("UTF-8"));
            Files.write(testingText, linesOfOtherDocuments, Charset.forName("UTF-8"));
            Files.write(bem, linesOfDocument, Charset.forName("UTF-8"));
            Files.write(otherFile, linesOfOtherDocuments, Charset.forName("UTF-8"));
            Files.write(bizarr, linesOfDocument, Charset.forName("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroyFiles() {
        try {
            FileUtils.deleteDirectory(new File(ROOT_ENCRYPT_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllFileNamesFromDir() {

        File[] expectedFileNames = {
                new File(ROOT_ENCRYPT_DIR + "otherTest.txt"),
                new File(ROOT_ENCRYPT_DIR + "document.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "testingText.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "opa.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "lalala.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "docCeption.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "bem.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "otherFile.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "lalalal" + File.separator + "bizarro" + File.separator + "bizarr.txt")
        };
        List<File> expectedListOfFiles = new ArrayList<>(Arrays.asList(expectedFileNames));
        List<File> actualListOfFiles = FileOperations.getAllFilenamesFromDir(ROOT_ENCRYPT_DIR);
        assertThat(actualListOfFiles, is(expectedListOfFiles));
    }

    @Test
    public void testEncryptFiles() {
        File[] expectedFileNames = {
                new File(ROOT_ENCRYPT_DIR + "otherTest.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "document.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "opa.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "testingText.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "lalala.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "docCeption.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "bem.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "otherFile.encrypted.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "lalalal" + File.separator + "bizarro" + File.separator + "bizarr.encrypted.txt")
        };
        List<File> expectedListOfFiles = new ArrayList<>(Arrays.asList(expectedFileNames));
        try {
            FileOperations.encryptFiles(ROOT_ENCRYPT_DIR, "senha");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> actualListOfFiles = FileOperations.getAllFilenamesFromDir(ROOT_ENCRYPT_DIR);
        assertThat(actualListOfFiles, is(expectedListOfFiles));
    }

    @Test
    public void testDecryptFiles() {
        File[] expectedFileNames = {
                new File(ROOT_ENCRYPT_DIR + "otherTest.txt"),
                new File(ROOT_ENCRYPT_DIR + "document.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "testingText.txt"),
                new File(ROOT_ENCRYPT_DIR + "otherDir" + File.separator + "opa.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "lalala.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "docCeption.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "bem.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "otherFile.txt"),
                new File(ROOT_ENCRYPT_DIR + "dirCeption" + File.separator + "fileCeptions" + File.separator +
                        "lalalal" + File.separator + "bizarro" + File.separator + "bizarr.txt")
        };
        List<File> expectedListOfFiles = new ArrayList<>(Arrays.asList(expectedFileNames));
        List<String> tableContent = mountTableContent(expectedListOfFiles);
        try {
            FileOperations.encryptFiles(ROOT_ENCRYPT_DIR, "senha");
            FileOperations.decryptFiles(ROOT_ENCRYPT_DIR, "senha");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> actualListOfFiles = FileOperations.getAllFilenamesFromDir(ROOT_ENCRYPT_DIR);
        assertThat(actualListOfFiles, is(expectedListOfFiles));
    }

    public List<String> mountTableContent(List<File> expectedFiles) {
        List<String> tableContent = new ArrayList<>();
        for (File expectedFile : expectedFiles) {
            tableContent.add(expectedFile.getPath() + ";1234567890123456");
        }
        return tableContent;
    }
}
