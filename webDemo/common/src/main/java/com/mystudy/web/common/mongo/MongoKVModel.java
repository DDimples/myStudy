package com.mystudy.web.common.mongo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.LoggerFactory;

import com.mongodb.DBObject;

class MongoKVModel implements Cloneable {

	static final String ID = "_id";
	static final String EXPIRE = "expire";
	static final String VALUE = "value";
	static final String TYPE = "type";
	private static final byte Obj = 1;
	private static final byte Str = 2;
	private static final byte Err = -1;
	private static final byte Empt = 0;
	private final Object value;
	private final byte type;

	private MongoKVModel(byte type, Object value) {
		this.type = type;
		this.value = value;
	}

	static MongoKVModel getModel4Write(Object value) {
		byte type = Empt;
		if (value != null && Serializable.class.isInstance(value)) {
			if (String.class.isInstance(value)) {
				type = Str;
			} else {
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(
							out);
					objectOutputStream.writeObject(value);
					objectOutputStream.flush();
					objectOutputStream.close();
					value = out.toByteArray();
					type = Obj;
				} catch (Exception e) {
					type = Err;
					LoggerFactory.getLogger(MongoKVModel.class).error(
							"error in seria" + value.getClass(), e);
					return null;
				}
			}
		}
		if (type > 0) {
			return new MongoKVModel(type, value);
		}
		return null;
	}

	static Object getModel4Read(final Number type, Object value) {
		if (value != null && type != null) {
			switch (type.byteValue()) {
			case Str:
				if (!String.class.isInstance(value)) {
					value = null;
				}
				break;
			case Obj:
				Class<?> clzz = value.getClass();
				if (clzz.isArray() && clzz.getComponentType() == Byte.TYPE) {
					try {
						ObjectInputStream inputStream = new ObjectInputStream(
								new ByteArrayInputStream((byte[]) value));
						value = inputStream.readObject();
						inputStream.close();
					} catch (Exception e) {
						LoggerFactory.getLogger(MongoKVModel.class).error(
								"error in reseria", e);
						value = null;
					}
				} else {
					value = null;
				}
				break;
			default:
				value = null;
				break;
			}
		}
		return value;
	}

	/**
	 * @deprecated Replaced by using $gte in query
	 * @param document
	 * @return
	 */
	@Deprecated
	static boolean isValid(DBObject document) {
		try {
			Long exp = (Long) document.get(EXPIRE);
			if (exp != null && exp >= System.currentTimeMillis()) {
				return true;
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(MongoKVModel.class).error(
					"error in valid-check", e);
		}
		return false;
	}

	static Object convert(DBObject document) {
		try {
			return getModel4Read((Number) document.get(TYPE),
					document.get(VALUE));
		} catch (Exception e) {
			LoggerFactory.getLogger(MongoKVModel.class).error(
					"error in convert", e);
		}
		return null;
	}

	@Override
	public MongoKVModel clone() {
		try {
			return (MongoKVModel) super.clone();
		} catch (CloneNotSupportedException e) {
			// should not happen
			LoggerFactory.getLogger(MongoKVModel.class).error("error in clone",
					e);
		}
		return null;
	}

	public Object getValue() {
		return value;
	}

	public byte getType() {
		return type;
	}
}
