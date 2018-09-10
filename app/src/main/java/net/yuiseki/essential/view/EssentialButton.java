package net.yuiseki.essential.view;

import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import net.yuiseki.essential.EssentialService;
import net.yuiseki.essential.R;


public class EssentialButton extends EssentialView {
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
                essentialService.showEssentialYouTubeView();
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
