package net.yuiseki.essential;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.yuiseki.essential.view.EssentialButton;
import net.yuiseki.essential.view.EssentialDialog;
import net.yuiseki.essential.view.EssentialYouTubeView;

import java.util.Random;

public class EssentialService extends android.app.Service {
    private String TAG = "EssentialService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        essentialButton = null;
        essentialDialog = null;
        essentialYouTubeView = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction().equals("start")) {
            startNotification();
            showEssentialButton();
        } else {
            stopSelf();
        }
        return Service.START_STICKY;
    }

    private void startNotification(){
        NotificationChannel essentialNotificationChannel = new NotificationChannel(TAG, TAG, NotificationManager.IMPORTANCE_NONE);
        essentialNotificationChannel.setLightColor(Color.WHITE);
        essentialNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(essentialNotificationChannel);
        Intent essentialActivityIntent = new Intent(this, EssentialActivity.class);
        essentialActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, essentialActivityIntent, 0);
        Notification essentialNotification = new Notification.Builder(this, TAG)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("本質を実行しています")
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(new Random().nextInt(), essentialNotification);
    }

    public EssentialButton essentialButton = null;
    public void showEssentialButton(){
        if (essentialButton==null) {
            essentialButton = new EssentialButton(this);
        }
        essentialButton.essentialWindowLayoutParams.x = buttonX;
        essentialButton.essentialWindowLayoutParams.y = buttonY;
        essentialButton.setVisibility(true);
    }

    int buttonX = 0;
    int buttonY = 0;
    public void hideEssentialButton(){
        if (essentialButton != null) {
            essentialButton.setVisibility(false);
            buttonX = essentialButton.essentialWindowLayoutParams.x;
            buttonY = essentialButton.essentialWindowLayoutParams.y;
        }
    }

    private EssentialDialog essentialDialog = null;
    public void showEssentialDialog(){
        hideEssentialButton();
        if (essentialDialog==null){
            essentialDialog = new EssentialDialog(this);
        }
        essentialDialog.setVisibility(true);
    }

    public void hideEssentialDialog(){
        showEssentialButton();
        if (essentialDialog!=null){
            essentialDialog.setVisibility(false);
        }
    }

    private EssentialYouTubeView essentialYouTubeView = null;
    public void showEssentialYouTubeView(){
        hideEssentialButton();
        if (essentialYouTubeView == null){
            essentialYouTubeView = new EssentialYouTubeView(this);
        }
        essentialYouTubeView.setVisibility(true);
    }

    public void hideEssentialYouTubeView(){
        showEssentialButton();
        if (essentialYouTubeView!=null){
            essentialYouTubeView.setVisibility(false);
        }
        essentialYouTubeView = null;
    }
}
