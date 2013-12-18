package pro.chinasoft.service.start;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

//@SuppressLint("NewApi")
public class InSmsService extends Service {
	private static final String TAG="Test";  
    
    @Override 
    //Service时被调用  
    public void onCreate()  
    {  
        Log.i(TAG, "Service onCreate--->");  
        super.onCreate();  
    }  
 
    @Override 
    //当调用者使用startService()方法启动Service时，该方法被调用  
    public void onStart(Intent intent, int startId)  
    {  
        Log.i(TAG, "Service onStart--->");  
        super.onStart(intent, startId);  
    }  
 
    @Override 
    //当Service不在使用时调用  
    public void onDestroy()  
    {  
        Log.i(TAG, "Service onDestroy--->");  
        super.onDestroy();  
    }  
 
    @Override 
    //当使用startService()方法启动Service时，方法体内只需写return null  
    public IBinder onBind(Intent intent)  
    {  
        return null;  
    }  
}
