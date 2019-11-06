package com.gmartise.Blockchain.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
public class Configuration {

    private XMLConfiguration xmlConfiguration = null;
    private static Configuration configuration = null;

    public static final Configuration getInstance(){
        if(configuration == null){
            configuration = new Configuration();
            configuration.xmlConfiguration = new XMLConfiguration();
            configuration.xmlConfiguration.setFileName("configuration.xml");
            try{
                configuration.xmlConfiguration.load();
            } catch (ConfigurationException e) {
                //TODO log -> Error reading file
            }
        }
        return configuration;
    }

    public String getMasterNodeURL(){
        return configuration.xmlConfiguration.getString("masterNodeConfiguration");
    }

    public int getMaxBlockTransactions(){
        return configuration.xmlConfiguration.getInt("maxBlockTransactions");
    }

    public int getDifficulty(){
        return configuration.xmlConfiguration.getInt("difficulty");
    }
}
