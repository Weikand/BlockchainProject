package com.gmartise.Blockchain.node.services;

import com.gmartise.Blockchain.modules.Transaction;
import com.gmartise.Blockchain.modules.TransactionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collection;

@Service
public class TransactionServices {

    private TransactionPool transactionPool = new TransactionPool();

    @Autowired
    public TransactionServices(){}

    /**
     * Add transaction to the pool
     * @param transaction
     * @return
     */
    public synchronized boolean addTransaction(Transaction transaction){
        return transactionPool.add(transaction);
    }

    public TransactionPool getTransactionPool() {
        return transactionPool;
    }

    /**
     * Delete transaction from the pool
     * @param transaction
     */
    public void deleteTransaction(Transaction transaction){
        transactionPool.delete(transaction);
    }

    /**
     * Check if pool contains transactions
     * @param transactions
     * @return
     */
    public boolean containTransactions(Collection<Transaction> transactions){
        return transactionPool.containsTransactions(transactions);
    }

    /**
     * Download transactionPool from another node
     * @param nodeURL
     * @param restTemplate
     */
    public void obtainTransactionPool(URL nodeURL, RestTemplate restTemplate){
        TransactionPool transactionPool = restTemplate.getForObject(nodeURL+"/transaction", TransactionPool.class);
        this.transactionPool = transactionPool;
    }


}
