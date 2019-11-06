package com.gmartise.Blockchain.node.controller;

import com.gmartise.Blockchain.modules.Transaction;
import com.gmartise.Blockchain.modules.TransactionPool;
import com.gmartise.Blockchain.node.services.NodeServices;
import com.gmartise.Blockchain.node.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping("transaction")
public class TransactionsRestController {

    private final TransactionServices transactionServices;
    private final NodeServices nodeServices;

    @Autowired
    public TransactionsRestController(TransactionServices transactionServices, NodeServices nodeServices){
        this.transactionServices = transactionServices;
        this.nodeServices = nodeServices;
    }

    /**
     * Get transaction pool pending to be included to a block
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    TransactionPool getTransactionPool(){
        return transactionServices.getTransactionPool();
    }

    @RequestMapping(method = RequestMethod.POST)
    void addTransaction(@RequestBody Transaction transaction, @RequestParam(required = false) Boolean propagate, HttpServletResponse response){

        boolean success = transactionServices.addTransaction(transaction);

        if(success){
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            if(propagate != null && propagate){
                nodeServices.sendPostToNeighbours("transaction", transaction);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }


}
