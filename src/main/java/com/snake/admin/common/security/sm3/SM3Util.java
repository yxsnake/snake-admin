package com.snake.admin.common.security.sm3;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;;

/**
 * @author jiangzhe
 * @create 2022/8/23 14:13
 */
@Slf4j
public class SM3Util {

    public static String  encryptText(String content)  {
        byte[] bytes = content.getBytes();
        byte[] hash = hash(bytes);
        String sm3 = ByteUtils.toHexString(hash);
        log.info("[sm3 -> encryptText]sm3 hex:{}",sm3);
        return sm3;
    }


    public static byte[] hash(byte[] srcData){
        SM3Digest digest=new SM3Digest();
        digest.update(srcData,0,srcData.length);
        byte[] bytes = new byte[digest.getDigestSize()];
        digest.doFinal(bytes,0);
        return bytes;
    }

    /**
     * sm3算法通过密钥进行加密，不可逆加密
     * @param keyText 密钥字符串
     * @param plainText 需加密的明文字符串
     * @return 加密后固定长度64的16进制字符串
     */
    public static String encryptByKey(String keyText, String plainText) {
        return ByteUtils.toHexString(encryptByKey(keyText.getBytes(), plainText.getBytes()));
    }

    /**
     * sm3算法通过密钥进行加密，不可逆加密
     * @param keyByte 密钥数组
     * @param plainByte 需加密的明文数组
     * @return 加密后固定长度64的16进制数组
     */
    public static byte[] encryptByKey(byte[] keyByte, byte[] plainByte) {
        KeyParameter keyParameter = new KeyParameter(keyByte);
        SM3Digest sm3Digest = new SM3Digest();
        HMac hMac = new HMac(sm3Digest);
        hMac.init(keyParameter);
        hMac.update(plainByte, 0, plainByte.length);
        byte[] result = new byte[hMac.getMacSize()];
        hMac.doFinal(result, 0);
        return result;
    }



    public static void main(String[] args) {
        String data = "abcd";
        log.info("摘要：{}" ,SM3Util.encryptText(data));
        //输出 82ec580fe6d36ae4f81cae3c73f4a5b3b5a09c943172dc9053c69fd8e18dca1e 就说明符合SM3算法要求
    }
}

