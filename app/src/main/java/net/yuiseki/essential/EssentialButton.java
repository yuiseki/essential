package net.yuiseki.essential;

import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

class EssentialButton extends EssentialView {
    private String TAG = "EssentialButton";


    EssentialButton(EssentialService essentialService){
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
                    initialPosition = new EssentialPosition(essentialLayoutParams).minus(new EssentialPosition(event));
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (initialPosition ==null) {
                        break;
                    }
                    EssentialPosition newPosition = initialPosition.plus(new EssentialPosition(event));
                    essentialLayoutParams.x = newPosition.getX();
                    essentialLayoutParams.y = newPosition.getY();
                    windowManager.updateViewLayout(essentialView, essentialLayoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    initialPosition = null;;
                    break;
            }
            return false;
        }
    };

    private Boolean doubleClick = false;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            if (doubleClick){
                essentialService.showEssentialDialog();
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
