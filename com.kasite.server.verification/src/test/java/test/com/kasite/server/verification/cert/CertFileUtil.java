package test.com.kasite.server.verification.cert;

import com.coreframework.util.FileOper;
import com.kasite.core.common.util.FileToBase64;

public class CertFileUtil {


    public static void main(String[] args) {
        try {
            String base64Code =FileToBase64.encryptToBase64("/Users/daiyanshui/Desktop/Config/apiclient_cert.p12");
            System.out.println(base64Code);
            FileToBase64.decryptByBase64(base64Code, "/Users/daiyanshui/Desktop/Config/test/apiclient_cert.p12");
            FileOper.write("/Users/daiyanshui/Desktop/Config/test/apiclient_cert.txt", base64Code, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
