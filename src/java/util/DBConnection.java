/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author Santoro
 */
public class DBConnection {
    
    MongoClient client;
    MongoDatabase database;

    public DBConnection() {
        MongoClientURI uri = new MongoClientURI("mongodb://integra:Integra2017@fiscadev0-shard-00-00-wntu1.mongodb.net:27017,fiscadev0-shard-00-01-wntu1.mongodb.net:27017,fiscadev0-shard-00-02-wntu1.mongodb.net:27017/test?ssl=true&replicaSet=FiscaDev0-shard-0&connectTimeoutMS=30000&authSource=admin");
        client = new MongoClient(uri);
        database = client.getDatabase("denuncia");
    }
    
    public MongoDatabase getDatabase(){
        return database;
    }
    
    public void closeDaytaBase(){
        client.close();
    }
    
    
    
    
    
}
