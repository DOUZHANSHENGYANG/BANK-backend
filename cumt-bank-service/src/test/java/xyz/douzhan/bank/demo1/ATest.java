package xyz.douzhan.bank.demo1;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.douzhan.bank.utils.CypherUtil;
import xyz.douzhan.bank.utils.HttpClientUtil;




/**
 * 一些声明信息
 * Description:
 * date: 2023/12/2 15:27
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@SpringBootTest
public class ATest {
    private static  String APP_ID="44513880";
    private static  String API_KEY="YXtFzXr6ssK8eZoPPDKDTi2w";
    private static  String SECRET_KEY="y9GaO27dkLTPHnKcvxrcXbxSpU6892Ox";
    @Test
    public void testSM4Encode()throws Exception{
        System.out.println("CypherUtil.encryptSM4(\"张三\") = " + CypherUtil.encryptSM4("张三"));
        System.out.println("CypherUtil.encryptSM4(\"zhangSan\") = " + CypherUtil.encryptSM4("zhangSan"));
        System.out.println("CypherUtil.encryptSM4(\"431382200408280455\") = " + CypherUtil.encryptSM4("431382200408280455"));
        System.out.println("CypherUtil.encryptSM4(\"16680284176\") = " + CypherUtil.encryptSM4("16680284176"));
        System.out.println("CypherUtil.encryptSM4(\"082127\") = " + CypherUtil.encryptSM4("082127"));
        System.out.println("CypherUtil.encryptSM4(\"zhangsan123\") = " + CypherUtil.encryptSM4("zhangsan123"));
        System.out.println("CypherUtil.encryptSM4(\"6223330111000000103\") = " + CypherUtil.encryptSM4("6223330111000000103"));
    }
    @Test
    public void get()throws Exception{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=YXtFzXr6ssK8eZoPPDKDTi2w&client_secret=y9GaO27dkLTPHnKcvxrcXbxSpU6892Ox&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HttpClientUtil.getHTTP_CLIENT().newCall(request).execute();
        System.out.println(response.body().string());
    }


//    @Test
//    public void testGetKeyPair(){
//        KeyPair keyPair = CypherUtil.getKeyPair();
//        System.out.println("keyPair.getPublic().getEncoded() = " + Arrays.toString(keyPair.getPublic().getEncoded()));
//        System.out.println("keyPair.getPrivate().getEncoded() = " + Arrays.toString(keyPair.getPrivate().getEncoded()));
//    }
    @Test
    public void testJWT()throws Exception{
//        Security.addProvider(new BouncyCastleProvider());
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
//        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
//        keyPairGenerator.initialize(ecSpec);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
//        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
//        System.out.println("私钥: " + privateKey.getD().toString(16));
//        System.out.println("公钥: " + publicKey.getQ().getEncoded(false).toString());

//        String token = JWTUtil.genToken("lishunyang");
//        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJzbTIifQ.eyJ1c2VySW5mbyI6Imxpc2h1bnlhbmciLCJleHAiOjIwMTY5MDQ0MjJ9.046B98FDB0A5EAF292CA1FE8D938AF11C20202DC508F1FEE96173C466120FBEB6EE852B5364E3A8494B94A9B5E138C42C3C616DB3931B1D054F7851EC054FE546BC21BF17DC15376B23549FEA53E451483DAEBDAD019C9EB60DFDF1A7B58E786006F284152DCFD486AB28FAB29B2B47839FBFD100AA987028E0A986B8041CE75669D56B9D6C61D9DF3B188A9C0FCE017D1D367431848F7E3ADCB77CCA78DC732D998B876569B7AC2A325A9F50EBE2582B6C396A1B97CD3A8A53E9BE8";
//        System.out.println("token = " + token);
//        String name = (String) JWTUtil.parseToken(token);
//        System.out.println("name = " + name);
//        KeyPair pair = SecureUtil.generateKeyPair("SM2",256);
//        String publicKey="";
//        String privateKey="";
//        for (byte b : pair.getPublic().getEncoded()) {
//            publicKey+=b;
//        }
//        for (byte b : pair.getPrivate().getEncoded()) {
//            privateKey+=b;
//        }


//        System.out.println("pair.getPublic().getEncoded() = " + publicKey);
//        System.out.println("pair.getPrivate().getEncoded().toString() = " + privateKey);
    }

}
