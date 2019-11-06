package com.gmartise.Blockchain.modules;

import com.gmartise.Blockchain.utils.SignUtils;
import com.gmartise.Blockchain.utils.impl.SignUtilsImpl;
import com.google.common.primitives.Longs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Date;

public class Transaction {

    private byte[] hash;
    private byte[] senderHash;
    private byte[] receiverHash;
    private double quantity;
    private byte[] privateSignature;
    private long timeStamp;

    public Transaction() {
    }

    public Transaction(byte[] hash, byte[] senderHash, byte[] receiverHash, double quantity, byte[] privateSignature, long timeStamp) {
        this.hash = hash;
        this.senderHash = senderHash;
        this.receiverHash = receiverHash;
        this.quantity = quantity;
        this.privateSignature = privateSignature;
        this.timeStamp = timeStamp;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getSenderHash() {
        return senderHash;
    }

    public void setSenderHash(byte[] senderHash) {
        this.senderHash = senderHash;
    }

    public byte[] getReceiverHash() {
        return receiverHash;
    }

    public void setReceiverHash(byte[] receiverHash) {
        this.receiverHash = receiverHash;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public byte[] getPrivateSignature() {
        return privateSignature;
    }

    public void setPrivateSignature(byte[] privateSignature) {
        this.privateSignature = privateSignature;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Get signed content
     * @return byte[]
     */
    public byte[] getTransactionContent(){
        byte[] content = ArrayUtils.addAll(String.valueOf(quantity).getBytes());
        content = ArrayUtils.addAll(content, senderHash);
        content = ArrayUtils.addAll(content, receiverHash);
        content = ArrayUtils.addAll(content, privateSignature);
        content = ArrayUtils.addAll(content, Longs.toByteArray(timeStamp));
        return content;
    }

    /**
     * Get transaction hash, identifier
     * @return
     */
    public byte[] getTransactionHash(){
        return DigestUtils.sha3_512(getTransactionContent());
    }

    public boolean isValid(){

        SignUtils signUtils = SignUtilsImpl.getInstance();

        //Check hash
        if(!Arrays.equals(this.getHash(), this.getTransactionHash())){
            return false;
        }
        //Check sign
        try{
            if(!signUtils.checkSign(this.getTransactionContent(), this.getPrivateSignature(), this.getSenderHash())){
                return false;
            }
        } catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        Transaction transaction = (Transaction) obj;
        return Arrays.equals(hash, transaction.getHash());
    }

    @Override
    public String toString() {
        return "{"+hash+","+senderHash+","+receiverHash+","+quantity+","+privateSignature+","+new Date(timeStamp)+"}";
    }
}
