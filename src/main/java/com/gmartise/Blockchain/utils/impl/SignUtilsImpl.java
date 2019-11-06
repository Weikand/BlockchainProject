package com.gmartise.Blockchain.utils.impl;

import com.gmartise.Blockchain.utils.SignUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignUtilsImpl implements SignUtils {

    private static SignUtils instance;
    public static SignUtils getInstance(){
        if(instance == null){
            instance = new SignUtilsImpl();
        }
        return instance;
    }

    private static KeyFactory keyFactory = null;

    static {
        try {
            keyFactory = KeyFactory.getInstance("DSA", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException ignored) {
        }
    }

    @Override
    public KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(1024, random);
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public boolean checkSign(byte[] transactionContent, byte[] privateSignature, byte[] senderHash) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Create PublicKey
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(senderHash);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        //Validate signature
        Signature sign = getSignatureInstance();
        sign.initVerify(publicKey);
        sign.update(transactionContent);
        return sign.verify(privateSignature);
    }

    @Override
    public byte[] sign(byte[] info, byte[] privateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        //Create PrivateKey
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);

        //Sign
        Signature sign = getSignatureInstance();
        sign.initSign(privateKeyObj);
        sign.update(info);
        return sign.sign();
    }

    private Signature getSignatureInstance() throws NoSuchProviderException, NoSuchAlgorithmException {
        return Signature.getInstance("SHA1withDSA", "SUN");
    }
}
