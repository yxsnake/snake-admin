package com.snake.admin.common.security.sm3;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * @author jiangzhe
 * @create 2022/8/23 14:13
 */
public class SM3Util {

    public static String hash1(String data){
        return Hex.toHexString(hash2(data));
    }

    public static byte[] hash2(String data){
        SM3Digest sm3Digest=new SM3Digest();
        byte[] srcData = data.getBytes();
        sm3Digest.update(srcData,0,srcData.length);
        byte[] hash = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    public static void main(String[] args) {
        String data = "abcd";
        System.out.println("摘要：" + SM3Util.hash1(data));
        //输出 82ec580fe6d36ae4f81cae3c73f4a5b3b5a09c943172dc9053c69fd8e18dca1e 就说明符合SM3算法要求
    }
}

