import br.ufsc.CryptoUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.Security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CryptoUtilsTest {

    @Test
    public void testDoCryptoWithGCM() {
        Security.addProvider(new BouncyCastleProvider());
        String textToEncrypt = "e266fd670acca196fc2b2b15fd41997e";
        String key = "abobrinha";
        String storedIv = "cafebabefacedbaddecaf888";
        String encryptedText = CryptoUtils.doCryptoWithGCM(true,key,textToEncrypt,storedIv);
        String result = CryptoUtils.doCryptoWithGCM(false,key,encryptedText,storedIv);
        assertThat(result, is(textToEncrypt));
    }
}
