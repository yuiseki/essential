package net.yuiseki.essential.view;

import android.view.MotionEvent;
import android.view.WindowManager;

public class EssentialPosition {
    Float fx;
    Float fy;
    EssentialPosition(Float fx, Float fy){
        this.fx = fx;
        this.fy = fy;
    }
    EssentialPosition(int ix, int iy){
        this.fx = (float) ix;
        this.fy = (float) iy;
    }
    EssentialPosition(WindowManager.LayoutParams params){
        this.fx = (float) params.x;
        this.fy = (float) params.y;
    }
    EssentialPosition(MotionEvent event){
        this.fx = event.getRawX();
        this.fy = event.getRawY();
    }
    EssentialPosition plus(EssentialPosition p){
        return new EssentialPosition(fx + p.fx, fy + p.fy);
    }
    EssentialPosition minus(EssentialPosition p){
        return new EssentialPosition(fx - p.fx, fy - p.fy);
    }
    public int getX(){
        return fx.intValue();
    }
    public int getY(){
        return fy.intValue();
    }
}
