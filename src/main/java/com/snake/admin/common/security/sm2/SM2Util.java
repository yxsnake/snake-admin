package com.snake.admin.common.security.sm2;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class SM2Util {

    /**
     * 生成 SM2 公私钥对
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     */
    public static KeyPair geneSM2KeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 使用SM2参数初始化生成器
        kpg.initialize(sm2Spec);
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取私钥（16进制字符串，头部不带00长度共64）
     * @param privateKey 私钥
     * @return
     */
    public static String getPriKeyHexString(PrivateKey privateKey){
        BCECPrivateKey s=(BCECPrivateKey)privateKey;
        String priKeyHexString = Hex.toHexString(s.getD().toByteArray());
        if(null!= priKeyHexString && priKeyHexString.length()==66 && "00".equals(priKeyHexString.substring(0,2))){
            return priKeyHexString.substring(2);
        }
        return priKeyHexString;
    }

    /**
     * 获取公钥（16进制字符串，头部带04长度共130）
     * @param publicKey
     * @return
     */
    public static String getPubKeyHexString(PublicKey publicKey){
        BCECPublicKey p=(BCECPublicKey)publicKey;
        return Hex.toHexString(p.getQ().getEncoded(false));
    }

    /**
     * SM2加密算法
     * @param publicKey 公钥
     * @param data      明文数据
     * @return
     */
    public static String encrypt(PublicKey publicKey, String data){
        BCECPublicKey p=(BCECPublicKey)publicKey;
        return encrypt(Hex.toHexString(p.getQ().getEncoded(false)), data);
    }

    /**
     * SM2解密算法
     * @param privateKey  私钥（16进制字符串）
     * @param cipherData  密文数据
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String cipherData) {
        BCECPrivateKey s=(BCECPrivateKey)privateKey;
        return decrypt(Hex.toHexString(s.getD().toByteArray()), cipherData);
    }

    /**
     * SM2加密算法
     * @param pubKeyHexString  公钥（16进制字符串）
     * @param data             明文数据
     * @return
     */
    public static String encrypt(String pubKeyHexString, String data) {
        
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(pubKeyHexString));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        // 设置sm2为加密模式
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2加密时出现异常:"+e.getMessage());
        }
        return Hex.toHexString(arrayOfBytes);

    }

    /**
     * SM2解密算法
     * @param priKeyHexString  私钥（16进制字符串）
     * @param cipherData       密文数据
     * @return
     */
    public static String decrypt(String priKeyHexString, String cipherData) {

        // 使用BC库加解密时密文以04开头，传入的密文前面没有04则补上
        if (!cipherData.startsWith("04")){
            cipherData = "04" + cipherData;
        }
        byte[] cipherDataByte = Hex.decode(cipherData);

        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());

        BigInteger privateKeyD = new BigInteger(priKeyHexString, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        // 设置sm2为解密模式
        sm2Engine.init(false, privateKeyParameters);

        String result = "";
        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(arrayOfBytes);
        } catch (Exception e) {
            System.out.println("SM2解密时出现异常:"+e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        String M="encryption standard";
        System.out.println("\n明文："+ M);

        SM2Util sm2 = new SM2Util();
        KeyPair keyPair = sm2.geneSM2KeyPair();

        PublicKey publicKey = keyPair.getPublic();
        String pubKeyHexString = sm2.getPubKeyHexString(publicKey);
        System.out.println("公钥："+ pubKeyHexString);

        PrivateKey privateKey = keyPair.getPrivate();
        String priKeyHexString = sm2.getPriKeyHexString(privateKey);
        System.out.println("私钥："+ priKeyHexString);

        String cipherData = sm2.encrypt(pubKeyHexString, M);
        System.out.println("密文：" + cipherData);

        String text=sm2.decrypt(priKeyHexString, cipherData);
        System.out.println("解密："+text);
    }
}



