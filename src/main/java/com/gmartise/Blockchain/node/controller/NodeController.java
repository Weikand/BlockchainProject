package com.gmartise.Blockchain.node.controller;

import com.gmartise.Blockchain.node.services.NodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@RestController
@RequestMapping("node")
public class NodeController {

    private final NodeServices nodeServices;

    @Autowired
    public NodeController(NodeServices nodeServices){
        this.nodeServices = nodeServices;
    }

    /**
     * Get Neighbour nodes in the network
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    Set<URL> getNeighbourNodes(){
        return nodeServices.getNeighbourNodes();
    }

    /**
     * Register a new node
     * @param nodeURL
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST)
    void registerNode(@RequestBody String nodeURL, HttpServletResponse response){
        try{
            nodeServices.addNode(new URL(nodeURL));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (MalformedURLException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Delete a node from the network
     * @param nodeURL
     * @param response
     */
    @RequestMapping(method = RequestMethod.DELETE)
    void deleteNode(@RequestBody String nodeURL, HttpServletResponse response){
        try{
            nodeServices.deleteNode(new URL(nodeURL));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (MalformedURLException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Endpoint where a node can know its public IP in order that other nodes can communicate
     * @param request
     * @return
     */
    @RequestMapping(path = "ip", method = RequestMethod.GET)
    String getPublicIp(HttpServletRequest request){
        return request.getRemoteAddr();
    }


}
