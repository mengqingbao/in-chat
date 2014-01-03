package org.xmpp.client.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pro.chinasoft.database.InSQLiteOpenHelper;
import pro.chinasoft.model.InMessage;
import pro.chinasoft.model.InUser;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InMessageStore {
	
	private static SQLiteDatabase db;

	private static SQLiteDatabase getDb(boolean isRead,Context context) {
		// 读取查询数据		
		InSQLiteOpenHelper isqloh = new InSQLiteOpenHelper(context);
		if(isRead){
			return isqloh.getReadableDatabase();
		}else{
			return isqloh.getWritableDatabase();
		}
		//return SQLiteDatabase.openOrCreateDatabase("data/data/pro.chinasoft.activity/databases/mydata.db", null);
	}

	public static  List<InMessage> getMessages(String userId, String friendId,int start ,int limit,Context context) {
		List<InMessage> result = new ArrayList<InMessage>();
		db = getDb(true,context);
		Cursor c = db.rawQuery(
				"select * from InMessage where userId=? and friendId=? order by id asc limit "+limit+" offset "+start,
				new String[] { userId, friendId });
		int count=c.getCount();
		if (c.moveToFirst()) {// 判断游标是否为空
			for (int i = 0; i < count; i++) {
				InMessage inMessage = new InMessage();
				inMessage.setContent(c.getString(c.getColumnIndex("content")));
				inMessage.setType(c.getInt(c.getColumnIndex("type"))==1);
				inMessage.setCreateDate(new Date(c.getLong(c.getColumnIndex("createDate"))));
				result.add(inMessage);
				c.moveToNext();// 移动到指定记录
			}
		}

		return result;
	}

	public static void saveOrUpdate(String userId, String friendId, String content,boolean type,Context context) {
		db = getDb(false,context);
		ContentValues cv = new ContentValues();
		cv.put("content", content);
		cv.put("userId", userId);
		cv.put("friendId", friendId);
		if(type){
			cv.put("type", 1);
		}else{
			cv.put("type", 0);
		}
		cv.put("createDate",new Date().getTime());
		db.insert("InMessage", null, cv);
		db.close();
	}
	
	public static void close(){
		if(db!=null){
			db.close();
		}
	}

	public static List<InMessage> getUserMessage(Context context) {
		List<InMessage> result = new ArrayList<InMessage>();
		db = getDb(true,context);
		Cursor c = db.rawQuery(
				"select * from InMessage group by friendId order by createDate asc", null);
		int count=c.getCount();
		if (c.moveToFirst()) {// 判断游标是否为空
			for (int i = 0; i < count; i++) {
				InMessage inMessage = new InMessage();
				InUser user=new InUser();
				user.setNick(c.getString(c.getColumnIndex("userId")));
				inMessage.setContent(c.getString(c.getColumnIndex("content")));
				inMessage.setInUser(user);
				result.add(inMessage);
				c.moveToNext();// 移动到指定记录
			}
		}

		return result;
	}
}
