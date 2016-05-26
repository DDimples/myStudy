package com.mystudy.web.common.mongo;


import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mystudy.web.common.log.LogUtil;

import java.util.List;

/**
 * Created by chengxiang1 on 15/5/25.
 *
 * Mongodb 相关处理
 *
 */
public class MongoDBOperation {

    public MongoClient mongoClient = null;
    public DB db = null;
    public MongoDBOperation(){
        init();
    }

    public static void main(String[] args){
        MongoDBOperation t = new MongoDBOperation();
        if (t.mongoClient != null) {
            t.mongoClient.close();
        }
    }

    public void init(){

        try {
//            Configuration config = new PropertiesConfiguration("config/sys.properties");
//            String ip = config.getString("mongoIp");
//            int port = config.getInt("mongoPort");
//            String dbName = config.getString("mongoDbName");
//            test = ip+ "_"+port+"_"+dbName;
            ServerAddress address = new ServerAddress("192.168.33.199",27017);
            mongoClient = new MongoClient(address);
            db = mongoClient.getDB("SeoData");
        } catch (Exception e) {
            LogUtil.getCommonLogger().error("链接mongodb出错！");
            LogUtil.getCommonLogger().error(e.getMessage());
        }
    }

    public void insertToMongo(List<DBObject> data,String collectionName){
        try {
            if(data==null || data.size()==0){
                return;
            }
            DBCollection coll = db.getCollection(collectionName);
            coll.insert(data);
            
        }catch (Exception e){
            LogUtil.getCommonLogger().error("插入数据出错！~~"+collectionName);

        }
    }

	@Override
	protected void finalize() throws Throwable {
		if (mongoClient != null) {
			mongoClient.close();
		}
		super.finalize();
	}
}
