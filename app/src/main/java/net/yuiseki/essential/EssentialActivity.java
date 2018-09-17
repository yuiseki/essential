package net.yuiseki.essential;

import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
    public static final int REQUEST_OVERLAY = 1;
    public static final int REQUEST_USAGE_STATS = 2;
    public static final int REQUEST_ACCESSIBILITY = 3;
    private SharedPreferences sharedPreferences;
    boolean twitterAppInstalled = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essential);

        if (EssentialPermission.hasAllPermission(this)) {
            Log.d(TAG, "permission is ok");
        } else {
            EssentialPermission.requestPermission(this);
            return;
        }


        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.twitter.android", 0);
        } catch (PackageManager.NameNotFoundException e) {
            twitterAppInstalled = false;
            e.printStackTrace();
        }

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        if (twitterAppInstalled){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.getString("twitter_token", "").isEmpty()
                    || sharedPreferences.getString("twitter_secret", "").isEmpty()
                    || sharedPreferences.getLong("twitter_user_id", 0)==0
                    || sharedPreferences.getString("twitter_user_name", "").isEmpty()) {
                Log.d(TAG, "request twitter auth");
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
                Intent essentialServiceIntent = new Intent(getApplicationContext(), EssentialService.class);
                essentialServiceIntent.setAction("start");
                startForegroundService(essentialServiceIntent);
                finishAndRemoveTask();
            }
        } else {
            twitterLoginButton.setEnabled(false);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.twitter.android"));
            startActivity(intent);
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

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_OVERLAY:
                    Log.d(TAG, "enable overlay permission");
                    break;
                case REQUEST_USAGE_STATS:
                    Log.d(TAG, "enable usage stats permission");
                    break;
                case REQUEST_ACCESSIBILITY:
                    Log.d(TAG, "enable accessibility permission");
                    break;
                default:
                    Log.d(TAG, "unknown request code: "+requestCode);
                    twitterLoginButton.onActivityResult(requestCode, resultCode, data);
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    sharedPreferences.edit()
                            .putString("twitter_token", authToken.token)
                            .putString("twitter_secret", authToken.secret)
                            .putLong("twitter_user_id", session.getUserId())
                            .putString("twitter_user_name", session.getUserName())
                            .apply();
                    break;
            }
        }
    }
}
