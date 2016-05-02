import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FETableTest {

    private static final String ROOT_ENCRYPT_DIR = "dir2Encrypt" + File.separator;

    @Before
    public void setUpFiles() {
        File otherDir = new File(ROOT_ENCRYPT_DIR);
        otherDir.mkdir();
        Path table = Paths.get(ROOT_ENCRYPT_DIR + ".FETable");
        List<String> linesOfTable = Arrays.asList(
                "",
                "macaco",
                "martelos");
        try {
            Files.write(table, linesOfTable, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
