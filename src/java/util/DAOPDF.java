/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Santoro
 */
public class DAOPDF {

    private DBConnection connection;

    public DAOPDF() {
        connection = new DBConnection();
    }

    public JSONArray getData(String startDate, String endDate) throws ParseException {
        JSONArray array = new JSONArray();
        MongoCollection<Document> collection = connection.getDatabase().getCollection("denuncia2018");
        DBObject condition = new BasicDBObject(2);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        condition.put("$gte", df.parse(startDate));
        condition.put("$lt", df.parse(endDate));
        
        DBObject condition2 = new BasicDBObject(1);
        condition2.put("$eq", 1);
        
        MongoCursor<Document> cursor = collection.find(Filters.and(new BasicDBObject("created_at", condition),new BasicDBObject("status", condition2))).iterator();
        
        try {
            while (cursor.hasNext()) {
                array.put(new JSONObject(cursor.next().toJson()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            connection.closeDaytaBase();
        }
        return array;
    }

    public static void main(String[] args) {
        try {
            new DAOPDF().getData("2018/06/26 11:19:46", "2018/06/27 12:33:46");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}
