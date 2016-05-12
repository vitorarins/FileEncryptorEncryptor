import br.ufsc.EncryptFile;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class EncryptFileTest {

    private static final String ROOT_ENCRYPT_DIR = "dir4Encrypt";
    private static final List<String> LINES_OF_DOCUMENT = Arrays.asList("abobrinha", "abobrinha");
    private static final String KEY = "e266fd670acca196fc2b2b15fd41997e";
    private static final String FILE_LOCATION = ROOT_ENCRYPT_DIR + File.separator + "document.txt";
    private static final String ENCRYPTED_FILE_LOCATION = ROOT_ENCRYPT_DIR + File.separator + "document.encrypted.txt";

    @Before
    public void setUp() {
        File encryptDir = new File(ROOT_ENCRYPT_DIR);
        encryptDir.mkdir();
        Path document = Paths.get(FILE_LOCATION);
        try {
            Files.write(document, LINES_OF_DOCUMENT, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroy() {
        try {
            FileUtils.deleteDirectory(new File(ROOT_ENCRYPT_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncryptDecryptFile() {
        EncryptFile.encryptFile(KEY, FILE_LOCATION);
        EncryptFile.decryptFile(KEY, ENCRYPTED_FILE_LOCATION);
        Path document = Paths.get(FILE_LOCATION);
        List<String> documentContent = null;
        try {
            documentContent = Files.readAllLines(document, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(documentContent, is(LINES_OF_DOCUMENT));
    }
}
