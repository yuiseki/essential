package net.yuiseki.essential;

import android.app.Service;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class EssentialView {
    WindowManager windowManager;
    WindowManager.LayoutParams essentialLayoutParams;

    View essentialView;

    EssentialPosition initialPosition;
    EssentialService essentialService;


    EssentialView(final EssentialService essentialService){
        this.essentialService = essentialService;
        windowManager = (WindowManager) essentialService.getSystemService(Service.WINDOW_SERVICE);
        essentialLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        essentialLayoutParams.gravity = Gravity.CENTER;
        essentialLayoutParams.x = 0;
        essentialLayoutParams.y = 0;
    }

    private Boolean isVisible = false;
    public void setVisibility(Boolean b){
        if (this.isVisible != b) {
            this.isVisible = b;
            if (b) {
                windowManager.addView(essentialView, essentialLayoutParams);
            } else {
                windowManager.removeView(essentialView);
            }
        }
    }
}
