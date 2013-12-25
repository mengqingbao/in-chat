package org.xmpp.client.util;

import java.util.ArrayList;
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
		// Cursor c = db.query("InMessage",null,null,null,null,null,null);
		Cursor c = db.rawQuery(
				"select * from InMessage where userId=? and friendId=? order by id desc limit "+limit+" offset "+start,
				new String[] { userId, friendId });
		// String[] columns = new String[]{"content"};
		// Cursor c = db.query("InMessage", columns, "userId='"+userId+"'",null,
		// null, null, "Id");
		int count=c.getCount();
		if (c.moveToFirst()) {// 判断游标是否为空
			for (int i = 0; i < count; i++) {
				InMessage inMessage = new InMessage();
				inMessage.setContent(c.getString(c.getColumnIndex("content")));
				result.add(inMessage);
				c.moveToNext();// 移动到指定记录
				//c.moveToPosition(i);
			}
		}

		return result;
	}

	public static void add(String tname, Map<String, String> map,Context context) {
		db = getDb(false,context);
		ContentValues cv = new ContentValues();
		Set<String> key = map.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			cv.put(s, map.get(s));
		}
		db.insert(tname, null, cv);
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
		// Cursor c = db.query("InMessage",null,null,null,null,null,null);
		Cursor c = db.rawQuery(
				"select * from InMessage group by friendId order by reviceDate desc", null);
		// String[] columns = new String[]{"content"};
		// Cursor c = db.query("InMessage", columns, "userId='"+userId+"'",null,
		// null, null, "Id");
		int count=c.getCount();
		if (c.moveToFirst()) {// 判断游标是否为空
			for (int i = 0; i < count; i++) {
				InMessage inMessage = new InMessage();
				InUser user=new InUser();
				user.setNick(c.getString(c.getColumnIndex("userId")));
				inMessage.setContent(c.getString(c.getColumnIndex("content")));
				//inMessage.setReviceDate();
				inMessage.setInUser(user);
				result.add(inMessage);
				c.moveToNext();// 移动到指定记录
				//c.moveToPosition(i);
			}
		}

		return result;
	}
}
