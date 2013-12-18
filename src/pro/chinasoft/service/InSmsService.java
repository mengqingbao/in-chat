package pro.chinasoft.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//@SuppressLint("NewApi")
public class InSmsService extends Service{
	private static final String TAG="Test";
	private String userid;  
    
    
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("++++++++++insmsservice+++++++onBind+++++++++++++++++++");
		return null;
	}
	@Override
	public void onCreate() {
		System.out.println("++++++++++insmsservice+++++onCreate+++++++++++++++++++++");
		super.onCreate();
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		this.userid = intent.getStringExtra("USERID");
		System.out.println("+++++++++++++++++++++++++++++++"+userid);
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("++++++++++insmsservice+++++++++onStartCommand+++++++++++++++");
		this.userid = intent.getStringExtra("USERID");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		System.out.println("++++++++++insmsservice+++++++++onDestroy+++++++++++++++");
		super.onDestroy();
	}
    
}
