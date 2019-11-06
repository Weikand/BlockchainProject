package com.gmartise.Blockchain.modules;

import com.google.common.primitives.Longs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Block {

    private byte[] hash;
    private byte[] previousHash;
    private long nonce;
    private long timeStamp;
    private byte[] merkleTreeRoot;
    private List<Transaction> transactionList;

    public Block() {
    }

    public Block(byte[] hash, byte[] previousHash, long nonce, long timeStamp, byte[] merkleTreeRoot, List<Transaction> transactionList) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.nonce = nonce;
        this.timeStamp = timeStamp;
        this.merkleTreeRoot = merkleTreeRoot;
        this.transactionList = transactionList;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(byte[] previousHash) {
        this.previousHash = previousHash;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public byte[] getMerkleTreeRoot() {
        return merkleTreeRoot;
    }

    public void setMerkleTreeRoot(byte[] merkleTreeRoot) {
        this.merkleTreeRoot = merkleTreeRoot;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    /**
     * Calculate hash without transactions
     * @return
     */
    public byte[] calculateHash(){
        byte[] hashableData = ArrayUtils.addAll(previousHash, merkleTreeRoot);
        hashableData = ArrayUtils.addAll(hashableData, Longs.toByteArray(nonce));
        hashableData = ArrayUtils.addAll(hashableData, Longs.toByteArray(timeStamp));
        return DigestUtils.sha3_512(hashableData);
    }

    public byte[] calculateMerkleTreeRoot(){
        Queue<byte[]> hashQueue = new LinkedList<>(transactionList.stream().map(Transaction::getHash).collect(Collectors.toList()));

        while (hashQueue.size() > 1){
            //Calculate hash by previous ones
            byte[] info = ArrayUtils.addAll(hashQueue.poll(), hashQueue.poll());
            //Add to queue
            hashQueue.add(DigestUtils.sha3_512(info));
        }
        return hashQueue.poll();
    }

    public int calculateLeadingZeros(){
        for(int i = 0; i< getHash().length ; i++){
          if(getHash()[i]!=0){
              return i;
          }
        }
        return getHash().length;
    }

    public boolean isValid(){
        if(this.hash == null) {
            return false;
        }

        if(this.previousHash != null && this.nonce <= 0) {
            return false;
        }

        if(this.merkleTreeRoot == null) {
            return false;
        }

        if(this.transactionList == null || this.transactionList.size() == 0) {
            return false;
        }

        // Merkle Tree Root is equal
        if (!Arrays.equals(getMerkleTreeRoot(), calculateMerkleTreeRoot())) {
            return false;
        }

        // Block hash is equal
        if (!Arrays.equals(getHash(), calculateHash())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Block block = (Block) o;

        return Arrays.equals(hash, block.getHash());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override
    public String toString() {
        return "{Hash:" + hash + ", PreviousHash:" + previousHash + ", MerkleRoot:" + merkleTreeRoot + ", Nonce:" +
                nonce + ", Timestamp:" + new Date(timeStamp) + ", Transactions:" + transactionList.toString() + "}";
    }
}
