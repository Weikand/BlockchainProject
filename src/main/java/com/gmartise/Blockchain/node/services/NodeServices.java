package com.gmartise.Blockchain.node.services;

import com.gmartise.Blockchain.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class NodeServices implements ApplicationListener<WebServerInitializedEvent> {

    private final BlockServices blockServices;
    private final TransactionServices transactionServices;

    //Node URL
    private URL myNodeURL;

    //Network node
    private Set<URL> neighbourNodes = new HashSet();

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public NodeServices(BlockServices blockChainService, TransactionServices transactionServices){
        this.blockServices = blockChainService;
        this.transactionServices = transactionServices;
    }

    /**
     * In order to init the node we must to obtain all node list, obtain the blockchain, obtain transaction pool and
     * add our node into the network
     * @param webServerInitializedEvent
     */
    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        //Obtain master node url
        URL masterNodeURL = this.getMasterNode();

        //Calculate my URL
        String host = this.getPublicIp(masterNodeURL, restTemplate);
        int port = webServerInitializedEvent.getWebServer().getPort();

        myNodeURL = this.getMyNodeURL(host, port);

        //download blockchain and pool transactions if this isn't the master node
        if(!myNodeURL.equals(masterNodeURL)){
            neighbourNodes.add(masterNodeURL);

            //Get node, blocks and transaction list
            this.obtainNeighbourNodes(masterNodeURL, restTemplate);
            this.blockServices.obtainBlockChain(masterNodeURL, restTemplate);
            this.transactionServices.obtainTransactionPool(masterNodeURL, restTemplate);

            //add my node in the network
            this.sendPostToNeighbours("node", myNodeURL);
        } else {
            System.out.println("Executing master Node");
        }

    }

    /**
     * Delete the node from the network
     * @return
     */
    @PreDestroy
    public void shutdown(){
        //Send request in order to delete this node from the others
        this.sendDeleteNodeRequestToNeighbours("node", myNodeURL);
    }

    public synchronized void addNode(URL nodeURL){
        neighbourNodes.add(nodeURL);
    }

    public synchronized void deleteNode(URL nodeURL){
        neighbourNodes.remove(nodeURL);
    }

    /**
     * Send post to all the neighbours
     * @param endpoint
     * @param data
     */
    public void sendPostToNeighbours(String endpoint, Object data){
        neighbourNodes.parallelStream().forEach(nodeURL -> restTemplate.postForLocation(nodeURL+"/"+endpoint, data));
    }

    /**
     * Delete from all nodes
     * @param endpoint
     * @param data
     */
    public void sendDeleteNodeRequestToNeighbours(String endpoint, Object data){
        neighbourNodes.parallelStream().forEach(nodeURL -> restTemplate.delete(nodeURL+"/"+endpoint, data));
    }

    /**
     * Get all neighbour nodes
     * @param neighbourNodeURL
     * @param restTemplate
     */
    public void obtainNeighbourNodes(URL neighbourNodeURL, RestTemplate restTemplate){
        URL[] nodes = restTemplate.getForObject(neighbourNodeURL+"/node", URL[].class);
        if (nodes != null) {
            Collections.addAll(neighbourNodes, nodes);
        }
    }

    /**
     * Get public ip from node
     * @param neighbourNodeURL
     * @param restTemplate
     * @return
     */
    public String getPublicIp(URL neighbourNodeURL, RestTemplate restTemplate){
        return restTemplate.getForObject(neighbourNodeURL+"/node/ip", String.class);
    }

    /**
     * Build node url
     * @param host
     * @param port
     * @return
     */
    private URL getMyNodeURL(String host, int port){
        try{
            return new URL("http", host, port, "");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private URL getMasterNode(){
        try{
            return new URL(Configuration.getInstance().getMasterNodeURL());
        } catch (MalformedURLException e){
            return null;
        }
    }

    public Set<URL> getNeighbourNodes() {
        return neighbourNodes;
    }
}
