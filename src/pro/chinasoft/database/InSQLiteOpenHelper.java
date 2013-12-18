package pro.chinasoft.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InSQLiteOpenHelper extends SQLiteOpenHelper{

	private static final String DB_NAME = "mydata.db";
	private static final int version = 1; 

	public InSQLiteOpenHelper(Context context){
		super(context, DB_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
