package com.gmartise.Blockchain.modules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TransactionPool {

    private Set<Transaction> pool = new HashSet<>();

    public synchronized boolean add(Transaction transaction){
        if(transaction.isValid()){
            pool.add(transaction);
            return true;
        }
        return false;
    }

    public void delete(Transaction transaction){
        pool.remove(transaction);
    }

    public boolean containsTransactions(Collection<Transaction> transactions){
        return pool.contains(transactions);
    }

}
