package net.yuiseki.essential.view;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.yuiseki.essential.EssentialApplication;
import net.yuiseki.essential.EssentialService;
import net.yuiseki.essential.R;


public class EssentialButton extends Essence {
    private String TAG = "EssentialButton";

    public EssentialButton(EssentialService essentialService){
        super(essentialService);

        ImageView imageView = new ImageView(essentialService);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setScaleX(0.6f);
        imageView.setScaleY(0.6f);
        imageView.setBackground(essentialService.getDrawable(R.drawable.round_image));
        TypedValue attribute = new TypedValue();
        essentialService.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, attribute, true);
        imageView.setForeground(essentialService.getDrawable(attribute.resourceId));
        imageView.setElevation(15.0f);
        essentialView = imageView;

        essentialView.setOnTouchListener(onTouchListener);
        essentialView.setOnClickListener(onClickListener);

    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initialPosition = new EssentialPosition(essentialWindowLayoutParams).minus(new EssentialPosition(event));
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (initialPosition ==null) {
                        break;
                    }
                    EssentialPosition newPosition = initialPosition.plus(new EssentialPosition(event));
                    essentialWindowLayoutParams.x = newPosition.getX();
                    essentialWindowLayoutParams.y = newPosition.getY();
                    windowManager.updateViewLayout(essentialView, essentialWindowLayoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    initialPosition = null;;
                    break;
            }
            // Important return false for clickable
            return false;
        }
    };

    private Boolean doubleClick = false;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            if (doubleClick){
                Log.d(TAG, "doubleClick");
                //essentialService.essentialEverything.showEssentialYouTubeView();
                long time = System.currentTimeMillis();
                long interval = 10 * 1000;
                EssentialApplication essentialApplication = (EssentialApplication) essentialService.getApplication();
                essentialApplication.inspectYouTubeVideoData = true;
                try {
                    UsageStatsManager usageStatsManager = (UsageStatsManager) essentialService.getSystemService(Context.USAGE_STATS_SERVICE);
                    UsageEvents events = usageStatsManager.queryEvents(time - interval, time);
                    while (events.hasNextEvent()){
                        UsageEvents.Event event = new UsageEvents.Event();
                        if (events.getNextEvent(event)) {
                            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                                Log.d(TAG, "event.getPackageName: "+event.getPackageName());
                                Log.d(TAG, "event.getClassName: "+event.getClassName());
                                if (event.getPackageName().equals("com.google.android.youtube")
                                        && event.getClassName().equals("com.google.android.apps.youtube.app.WatchWhileActivity")){
                                    ActivityInfo activityInfo = essentialService.getPackageManager().getActivityInfo(
                                            new ComponentName(event.getPackageName(), event.getClassName()),
                                            PackageManager.GET_META_DATA);
                                    String activityTitle = activityInfo.loadLabel(essentialService.getPackageManager()).toString();
                                    Log.d(TAG, "activityTitle: "+activityTitle);

                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                doubleClick = true;
            }
            essentialView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClick = false;
                }
            }, 250);
        }
    };
}
