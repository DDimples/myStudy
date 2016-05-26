package com.mystudy.web.common.mongo;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryOperators;

public class MongoClient {
	private final DBCollection dbCollection;
	private static final DBObject FIELDS = new BasicDBObject(MongoKVModel.ID, 0)
			.append(MongoKVModel.VALUE, 1).append(MongoKVModel.TYPE, 1);

	public MongoClient(Mongo mongo, String dbname, String collection) {
		if (dbname == null || dbname.isEmpty()) {
			throw new IllegalArgumentException(
					dbname == null ? "dbname could not be null"
							: ("illegal dbname:" + dbname));
		}
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException(
					collection == null ? "collection could not be null"
							: ("illegal collection:" + collection));
		}
		//mongodb dbname出现大小写不一致的时候，会出现问题，因此把dbname全转为小写
		dbCollection = mongo.getDB(dbname.toLowerCase()).getCollection(collection);
	}

	public void setex(String key, final Object value, final long expire) {
		if (key != null && Serializable.class.isInstance(value)) {
			MongoKVModel kvModel = MongoKVModel.getModel4Write(value);
			if (kvModel != null) {
				dbCollection.save(new BasicDBObject(MongoKVModel.ID, key)
						.append(MongoKVModel.EXPIRE,
								System.currentTimeMillis() + expire)
						.append(MongoKVModel.TYPE, kvModel.getType())
						.append(MongoKVModel.VALUE, kvModel.getValue()));
			}
		}
	}

	public void remove(final String key) {
		if (key != null) {
			dbCollection.remove(new BasicDBObject(MongoKVModel.ID, key));
		}
	}

	public Object get(final String key, long expire) {
		if (key != null) {
			long now = System.currentTimeMillis();
			DBObject dbObject = dbCollection.findAndModify(new BasicDBObject(
					MongoKVModel.ID, key).append(MongoKVModel.EXPIRE,
					new BasicDBObject(QueryOperators.GTE, now)), FIELDS, null,
					false, new BasicDBObject("$set", new BasicDBObject(
							MongoKVModel.EXPIRE, now + expire)), false, false);
			if (dbObject != null) {
				return MongoKVModel.convert(dbObject);
			}
		}
		return null;
	}

	public Object get(final String key) {
		if (key != null) {
			DBObject document = dbCollection.findOne(new BasicDBObject(
					MongoKVModel.ID, key).append(
					MongoKVModel.EXPIRE,
					new BasicDBObject(QueryOperators.GTE, System
							.currentTimeMillis())), FIELDS);
			if (document != null) {
				return MongoKVModel.convert(document);
			}
		}
		return null;
	}

}
