package net.yuiseki.essential;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.yuiseki.essential.view.EssentialButton;
import net.yuiseki.essential.view.EssentialTweetDialog;
import net.yuiseki.essential.view.EssentialYouTubeView;

import java.util.Random;

public class EssentialService extends android.app.Service {
    private String TAG = "EssentialService";
    public EssentialEverything essentialEverything;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        essentialEverything = new EssentialEverything(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        essentialEverything.onDestroy();
        AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocusRequest(audioFocusRequest);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction().equals("start")) {
            startNotification();
            essentialEverything.showEssentialButton();
            requestAudioFocus();
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

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "onAudioFocusChange: "+focusChange);
            AudioManager audioManager = (AudioManager)EssentialService.this.getSystemService(Context.AUDIO_SERVICE);
            if(audioManager.isMusicActive()){
                Log.d(TAG, "isMusicActive");
            }

        }
    };
    private AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build();
    private void requestAudioFocus(){
        AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(audioFocusRequest);
        switch (result){
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                Log.d(TAG, "AUDIOFOCUS_REQUEST_GRANTED");
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_DELAYED:
                Log.d(TAG, "AUDIOFOCUS_REQUEST_DELAYED");
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                Log.d(TAG, "AUDIOFOCUS_REQUEST_FAILED");
                break;
        }
    }

}
