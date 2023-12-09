package xyz.douzhan.bank.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.jwt.signers.JWTSigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.properties.CypherProperties;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.*;

/**
 * 一些声明信息
 * Description: 秘钥工具类
 * date: 2023/12/2 14:59
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Slf4j
public class CypherUtils {

    private static CypherProperties cypherProperties;
    private static ResourceLoader resourceLoader;
    public CypherUtils (CypherProperties cypherProperties,ResourceLoader resourceLoader){
        CypherUtils.cypherProperties=cypherProperties;
        CypherUtils.resourceLoader=resourceLoader;
    }

    /**
     * 自定义获取密钥方式
     * @param cypherProperties
     */
    public static void setCypherProperties(CypherProperties cypherProperties) {
        CypherUtils.cypherProperties = cypherProperties;
    }

    /**
     * 获取sm4实例
     * @return
     */
    public static SM4 getSM4(){
        return new SM4(
                Mode.CBC, Padding.ZeroPadding,
                "abc1111111111333".getBytes(CharsetUtil.CHARSET_UTF_8),
                "huiyinwobaailiya".getBytes(CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * sm4加密
     * @param plainText
     * @return
     */
    public static String encryptSM4(String plainText){
        SM4 sm4 = getSM4();
        return Base64.encode(sm4.encrypt(plainText));
    }
    /**
     * sm4解密
     * @param cipherText
     * @return
     */
    public static String decryptSM4(String cipherText){
        SM4 sm4 = getSM4();
        return sm4.decryptStr(Base64.decode(cipherText),CharsetUtil.CHARSET_UTF_8);
    }
    /**
     * 获取JwtSigner
     * @return
     */
    public static JWTSigner getJwtSigner(){
        return  new CypherUtils.CustomJWTSigner(SecureUtil.generateKeyPair("SM2",256));
    }
    /**
     * 从类路径获取密钥对
     * @return
     */
    public static KeyPair getKeyPair(){
        try{
            InputStream is = resourceLoader.getResource(cypherProperties.getLocation()).getInputStream();
            KeyStore keyStore = KeyStore.getInstance(cypherProperties.getFileType());
            keyStore.load(is,cypherProperties.getStorePass().toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(cypherProperties.getAlias(), cypherProperties.getKeyPass().toCharArray());
            PublicKey publicKey = keyStore.getCertificate(cypherProperties.getAlias()).getPublicKey();
            return new KeyPair(publicKey,privateKey);
        }catch (Exception e){
            log.error("密钥对读取失败，{}",e.getMessage());
            throw new RuntimeException("密钥对读取失败");
        }

    }
    /**
     * 一些声明信息
     * Description: 自定义signer实现自定义jwt
     * date: 2023/11/25 18:20
     * @author 斗战圣洋
     * @version
     * @since JDK 17
     */

    public static class CustomJWTSigner implements JWTSigner {
        private final Charset CHARSET= CharsetUtil.CHARSET_UTF_8;

        private SM2 sm2;

        public CustomJWTSigner(KeyPair keyPair) {
            this.sm2 = new SM2(keyPair.getPrivate(), keyPair.getPublic());
        }

        @Override
        public String sign(String headerBase64, String payloadBase64) {
            return sm2.encryptBcd(StrUtil.format("{}.{}",headerBase64,payloadBase64), KeyType.PublicKey);
        }

        @Override
        public boolean verify(String headerBase64, String payloadBase64, String userToken) {
            String token = this.sign(headerBase64, payloadBase64);
            if(!MessageDigest.isEqual(StrUtil.bytes(userToken,CHARSET),StrUtil.bytes(token,CHARSET))){
                return false;
            }
            return true;
        }

        @Override
        public String getAlgorithm() {
            return "sm2";
        }
    }
}
