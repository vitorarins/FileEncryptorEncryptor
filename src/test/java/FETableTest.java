import br.ufsc.FETable;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FETableTest {

    private static final String ROOT_ENCRYPT_DIR = "dir2Encrypt" + File.separator;
    private FETable feTable;

    @Before
    public void setUpFiles() {
        int addProvider = Security.addProvider(new BouncyCastleProvider());

        File otherDir = new File(ROOT_ENCRYPT_DIR);
        otherDir.mkdir();
        Path table = Paths.get(ROOT_ENCRYPT_DIR + ".FETable");
        List<String> linesOfTable = Arrays.asList(
                "document.txt;1234567890123456",
                "otherTest.txt;1234567890123456",
                "dirCeption/docCeption.txt;1234567890123456"
        );
        try {
            Files.write(table, linesOfTable, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        feTable = new FETable("senha");
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
    public void testLoadTableFromDir() {
        feTable.loadTableFromDir(ROOT_ENCRYPT_DIR);
        HashMap<String,String> keyTable = feTable.getKeyTable();
        HashMap<String,String> expectedKeyTable = new LinkedHashMap<>();
        expectedKeyTable.put("document.txt","1234567890123456");
        expectedKeyTable.put("otherTest.txt","1234567890123456");
        expectedKeyTable.put("dirCeption/docCeption.txt","1234567890123456");

        assertThat(keyTable, is(expectedKeyTable));
    }
}
