package net.yuiseki.essential.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;

import net.yuiseki.essential.EssentialService;

public class EssentialYouTubeView extends Essence {

    public EssentialYouTubeView(final EssentialService essentialService) {
        super(essentialService);
        int size = essentialService.essentialEverything.essentialButton.essentialView.getMeasuredHeight();
        EssentialRelativeLayout essentialRelativeLayout = new EssentialRelativeLayout(essentialService);
        essentialWindowLayoutParams = new WindowManager.LayoutParams(
                size,
                size,
                essentialWindowLayoutParams.type,
                essentialWindowLayoutParams.flags,
                essentialWindowLayoutParams.format
        );
        RelativeLayout.LayoutParams essentialRelativeLayoutParams = new RelativeLayout.LayoutParams(size, size);
        essentialRelativeLayoutParams.setMargins(0, 0, 0, 0);
        essentialRelativeLayout.setLayoutParams(essentialRelativeLayoutParams);

        final YouTubePlayerView youtubePlayerView = new YouTubePlayerView(essentialService);
        youtubePlayerView.setScaleX(1.5f);
        youtubePlayerView.setScaleY(1.5f);
        youtubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer youTubePlayer) {
                youTubePlayer.addListener(new YouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        youTubePlayer.loadVideo("crMPAApflq8", 0);
                        youTubePlayer.play();
                    }

                    @Override
                    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                        switch (state){
                            case ENDED:
                                essentialService.essentialEverything.hideEssentialYouTubeView();
                                break;
                        }
                    }

                    @Override
                    public void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(@NonNull PlayerConstants.PlayerError error) {

                    }

                    @Override
                    public void onApiChange() {

                    }

                    @Override
                    public void onCurrentSecond(float second) {

                    }

                    @Override
                    public void onVideoDuration(float duration) {

                    }

                    @Override
                    public void onVideoLoadedFraction(float loadedFraction) {

                    }

                    @Override
                    public void onVideoId(@NonNull String videoId) {

                    }
                });

            }
        }, false);
        RelativeLayout.LayoutParams youtubePlayerLayoutParams = (RelativeLayout.LayoutParams) youtubePlayerView.getLayoutParams();
        if (youtubePlayerLayoutParams==null){
            youtubePlayerLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        youtubePlayerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        youtubePlayerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        essentialRelativeLayout.addView(youtubePlayerView, youtubePlayerLayoutParams);
        essentialView = essentialRelativeLayout;
        essentialView.setOnTouchListener(onTouchListener);
    }

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    initialPosition = new EssentialPosition(essentialWindowLayoutParams).minus(new EssentialPosition(event));
                    return true;
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
            return false;
        }
    };


}
