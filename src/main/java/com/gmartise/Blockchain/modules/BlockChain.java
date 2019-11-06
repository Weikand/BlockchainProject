package com.gmartise.Blockchain.modules;

import java.util.List;

public class BlockChain {

    private List<Block> blockList;

    public BlockChain() {
    }

    public BlockChain(List<Block> blockList) {
        this.blockList = blockList;
    }

    public void addBlock(Block block) {
//
//        for(Transaction transaction : block.getTransactionList()){
//
//        }

        this.blockList.add(block);

    }

    public boolean isEmpty(){
        return this.blockList == null || this.blockList.isEmpty();
    }

    public Block getLastBlock(){
        return this.isEmpty() ? null : this.blockList.get(this.blockList.size() - 1);
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BlockChain blockChain = (BlockChain) o;

        if(blockList.size() != blockChain.getBlockList().size())
            return false;

        for(int i=0;i<blockList.size();i++) {
            if(blockList.get(i) !=  blockChain.getBlockList().get(i))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
