package net.yuiseki.essential;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class EssentialRelativeLayout extends RelativeLayout {
    private OnTouchListener onTouchListener;
    public EssentialRelativeLayout(Context context) {
        super(context);
    }

    public EssentialRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EssentialRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.onTouchListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return onTouchListener != null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onTouchListener.onTouch(getRootView(), event);
    }
}
