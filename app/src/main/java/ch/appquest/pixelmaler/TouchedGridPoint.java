package ch.appquest.pixelmaler;

import android.graphics.Canvas;

/**
 * Created by cyrilleulmi on 12/9/2014.
 */
public class TouchedGridPoint {
    private float pointX;
    private float pointY;

    public TouchedGridPoint(float x, float y){
        this.pointX = x;
        this.pointY = y;
    }


    public float getPointX() {
        return pointX;
    }

    public float getPointY() {
        return pointY;
    }
}
