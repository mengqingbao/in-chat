package pro.chinasoft.service.start;

import pro.chinasoft.activity.InChatActivity;
import pro.chinasoft.activity.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

//@SuppressLint("NewApi")
public class InSmsServicebak extends IntentService {
	public InSmsServicebak(String name) {
		super("InSmsService");
		// TODO Auto-generated constructor stub
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		/*long endTime = System.currentTimeMillis() + 5 * 1000;
		while (System.currentTimeMillis() < endTime) {
			synchronized (this) {
				try {
					wait(endTime - System.currentTimeMillis());
				} catch (Exception e) {
				}
			}
		}*/
		System.out.println("sms service start");
		this.showNotification();
	}

	private void showNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.icon_meassage_nor)
				.setContentTitle("My notification")
				.setContentText("Hello World!");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, InChatActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		/*
		 * TaskStackBuilder stackBuilder = TaskStackBuilder.create(this); //
		 * Adds the back stack for the Intent (but not the Intent itself)
		 * stackBuilder.addParentStack(InChatActivity.class); // Adds the Intent
		 * that starts the Activity to the top of the stack
		 * stackBuilder.addNextIntent(resultIntent); PendingIntent
		 * resultPendingIntent = stackBuilder.getPendingIntent( 0,
		 * PendingIntent.FLAG_UPDATE_CURRENT );
		 * mBuilder.setContentIntent(resultPendingIntent);
		 */
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());

	}
}
