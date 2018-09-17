package net.yuiseki.essential.view;

import android.app.Service;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import net.yuiseki.essential.EssentialService;

public class Essence {
    public WindowManager windowManager;
    public WindowManager.LayoutParams essentialWindowLayoutParams;

    View essentialView;

    EssentialPosition initialPosition;
    EssentialService essentialService;


    public Essence(final EssentialService essentialService){
        this.essentialService = essentialService;
        windowManager = (WindowManager) essentialService.getSystemService(Service.WINDOW_SERVICE);
        essentialWindowLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        essentialWindowLayoutParams.gravity = Gravity.CENTER;
        essentialWindowLayoutParams.x = 0;
        essentialWindowLayoutParams.y = 0;
    }

    private Boolean isVisible = false;
    public void setVisibility(Boolean b){
        if (this.isVisible != b) {
            this.isVisible = b;
            if (b) {
                windowManager.addView(essentialView, essentialWindowLayoutParams);
            } else {
                windowManager.removeView(essentialView);
            }
        }
    }
}
