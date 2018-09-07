package net.yuiseki.essential;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class EssentialActivity extends AppCompatActivity {
    private String TAG = "EssentialActivity";
    private TwitterLoginButton twitterLoginButton;
    private static final int overlayPermissionRequestCode = 1;
    private SharedPreferences sharedPreferences;

    private Boolean hasOverlayPermission(){
        return Settings.canDrawOverlays(this);
    }

    private void requestOverlayPermission(int requestCode){
        Intent essentialIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        essentialIntent.setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
        essentialIntent.setFlags(268435456);
        try {
            startActivityForResult(essentialIntent, requestCode);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essential);

        if (hasOverlayPermission()) {
            Intent essentialServiceIntent = new Intent( getApplicationContext(), EssentialService.class);
            essentialServiceIntent.setAction("start");
            startForegroundService(essentialServiceIntent);
        } else {
            requestOverlayPermission(overlayPermissionRequestCode);
        }

        Twitter.initialize(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("twitter_token", "").isEmpty()
                || sharedPreferences.getString("twitter_secret", "").isEmpty()
                || sharedPreferences.getLong("twitter_user_id", 0)==0
                || sharedPreferences.getString("twitter_user_name", "").isEmpty()){
            twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
            twitterLoginButton.setEnabled(true);
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    twitterLoginButton.setEnabled(false);
                    twitterLoginButton.setVisibility(View.GONE);
                }

                @Override
                public void failure(TwitterException exception) {
                    exception.printStackTrace();
                }
            });
        } else {
            this.finishAndRemoveTask();
        }




    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        /*
        Intent intent = new Intent( getApplicationContext(), EssentialService.class);
        intent.setAction("stop");
        startService(intent);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        sharedPreferences.edit()
                .putString("twitter_token", authToken.token)
                .putString("twitter_secret", authToken.secret)
                .putLong("twitter_user_id", session.getUserId())
                .putString("twitter_user_name", session.getUserName())
                .apply();
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case overlayPermissionRequestCode:
                    Log.d(TAG, "enable overlay permission");
                    break;
                default:
                    Log.d(TAG, "unknown request code: "+requestCode);
                    break;
            }
        }
    }
}
