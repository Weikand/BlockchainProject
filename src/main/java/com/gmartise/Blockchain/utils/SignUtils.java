package com.gmartise.Blockchain.utils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;

public interface SignUtils {

    /**
     * Generate a key pair
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException;

    /**
     * Validate a signature given data and a key
     * @param transactionContent
     * @param privateSignature
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    boolean checkSign(byte[] transactionContent, byte[] privateSignature, byte[] publicKey) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;

    byte[] sign(byte[] info, byte[] privateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException;
}
