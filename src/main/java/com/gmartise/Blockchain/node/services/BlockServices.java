package com.gmartise.Blockchain.node.services;

import com.gmartise.Blockchain.config.Configuration;
import com.gmartise.Blockchain.modules.Block;
import com.gmartise.Blockchain.modules.BlockChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;

@Service
public class BlockServices {

    private final TransactionServices transactionServices;
    private BlockChain blockChain = new BlockChain();

    @Autowired
    public BlockServices(TransactionServices transactionServices){
        this.transactionServices = transactionServices;
    }

    public BlockChain getBlockChain() {
        return blockChain;
    }

    /**
     * Add block to the blockchain
     * @param block
     * @return
     */
    public synchronized boolean addBlock(Block block){
        if(this.validateBlock(block)){
            this.blockChain.addBlock(block);

            //delete transaction included into transactionPool of the block
            block.getTransactionList().forEach(new TransactionServices()::deleteTransaction);
            return true;
        }
        return false;
    }

    /**
     * Get blockchain from another node
     * @param nodeURL
     * @param restTemplate
     */
    public void obtainBlockChain(URL nodeURL, RestTemplate restTemplate){
        BlockChain chain = restTemplate.getForObject(nodeURL+"/chain", BlockChain.class);
        this.blockChain = chain;
    }

    private boolean validateBlock(Block block){
        if(!block.isValid()){
            return false;
        }

        //Check if hash of previous block references to my chain
        if(!blockChain.isEmpty()){
            byte[] hashLastBlock = blockChain.getLastBlock().getHash();
            if(!Arrays.equals(block.getPreviousHash(), hashLastBlock)){
                return false;
            }
        } else {
            if(block.getPreviousHash() != null){
                return false;
            }
        }

        //Max transactions on a block
        if(block.getTransactionList().size() > Configuration.getInstance().getMaxBlockTransactions()){
            return false;
        }

        //Check that all transactions where on my pool
        if(!transactionServices.containTransactions(block.getTransactionList())){
            return false;
        }

        //The difficulty is the same
        if(block.calculateLeadingZeros() < Configuration.getInstance().getDifficulty()){
            return false;
        }

        return true;

    }
}
