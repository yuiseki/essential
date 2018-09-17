package net.yuiseki.essential;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.Twitter;

public class EssentialApplication extends Application {
    public boolean inspectYouTubeVideoData;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("EssentialApplication", "onCreate");
        Twitter.initialize(getApplicationContext());
    }
}
