package com.gmartise.Blockchain.node.controller;

import com.gmartise.Blockchain.modules.Block;
import com.gmartise.Blockchain.modules.BlockChain;
import com.gmartise.Blockchain.node.services.BlockServices;
import com.gmartise.Blockchain.node.services.NodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("block")
public class BlockRestController {
    private final BlockServices blockServices;
    private final NodeServices nodeServices;

    @Autowired
    public BlockRestController(BlockServices blockchainService, NodeServices nodeServices){
        this.blockServices = blockchainService;
        this.nodeServices = nodeServices;
    }

    BlockChain getBlockChain(){
        return blockServices.getBlockChain();
    }

    @RequestMapping(method = RequestMethod.POST)
    void addBlock(@RequestBody Block block, @RequestParam(required = false) Boolean propagate, HttpServletResponse response){
        boolean success = blockServices.addBlock(block);

        if(success){
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            if(propagate != null && propagate){
                nodeServices.sendPostToNeighbours("block", block);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }
}
