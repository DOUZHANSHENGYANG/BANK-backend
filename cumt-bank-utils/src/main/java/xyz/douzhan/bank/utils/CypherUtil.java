package xyz.douzhan.bank.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.jwt.signers.JWTSigner;
import lombok.extern.slf4j.Slf4j;
import xyz.douzhan.bank.constants.BizConstant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * 一些声明信息
 * Description: 秘钥工具类
 * date: 2023/12/2 14:59
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
public class CypherUtil {


    public static String digest(String salt,String rawString){
        SM3 sm3 = new SM3(salt.getBytes(StandardCharsets.UTF_8), BizConstant.HASH_COUNT);
        return sm3.digestHex(rawString);
    }

    /**
     * 获取sm4实例
     *
     * @return
     */
    public static AES getAES() {
        return  new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());
//                new SM4(
//                Mode.CBC, Padding.ZeroPadding,
//                "abc1111111111333".getBytes(CharsetUtil.CHARSET_UTF_8),
//                "huiyinwobaailiya".getBytes(CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * sm4加密
     *
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText) {

//        SM4 sm4 = getSM4();Base64.encode(sm4.encrypt(plainText));
        return plainText;
    }

    /**
     * sm4解密
     *
     * @param cipherText
     * @return
     */
    public static String decrypt(String cipherText) {
        return cipherText;
    }

    /**
     * 获取JwtSigner
     *
     * @return
     */
    public static JWTSigner getJwtSigner() {
        return new CypherUtil.CustomJWTSigner(SecureUtil.generateKeyPair("SM2", 256));
    }

    /**
     * 一些声明信息
     * Description: 自定义signer实现自定义jwt
     * date: 2023/11/25 18:20
     *
     * @author 斗战圣洋
     * @since JDK 17
     */

    public static class CustomJWTSigner implements JWTSigner {
        private final Charset CHARSET = CharsetUtil.CHARSET_UTF_8;

        private SM2 sm2;

        public CustomJWTSigner(KeyPair keyPair) {
            this.sm2 = new SM2(keyPair.getPrivate(), keyPair.getPublic());
        }

        @Override
        public String sign(String headerBase64, String payloadBase64) {
            return sm2.encryptBcd(StrUtil.format("{}.{}", headerBase64, payloadBase64), KeyType.PublicKey);
        }

        @Override
        public boolean verify(String headerBase64, String payloadBase64, String userToken) {
            String token = this.sign(headerBase64, payloadBase64);
            if (!MessageDigest.isEqual(StrUtil.bytes(userToken, CHARSET), StrUtil.bytes(token, CHARSET))) {
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
