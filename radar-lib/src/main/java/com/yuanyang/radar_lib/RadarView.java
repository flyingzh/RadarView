package com.yuanyang.radar_lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by yuanyang on 2016/12/6.
 */

public class RadarView extends View {

    private static final String[] texts = new String[] {
            "身份特质", "履约能力", "信用能力", "人脉关系", "行为偏好"
    };

    private static final int[] scores = new int[] { 4, 6, 5, 3, 5 };

    private static final String TAG = "RadarView";

    private int radius;

    private Paint linePaint;

    private Paint textPaint;

    private Paint imagePaint;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radius = dp2Px(120);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dp2Px(1));
        linePaint.setColor(0xFF444444);

        textPaint = new Paint();
        textPaint.setTextSize(sp2Px(12));
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xFF333333);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        imagePaint.setStyle(Paint.Style.FILL);
        imagePaint.setColor(0x33FF1493);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawRadar(canvas);
        canvas.rotate(90);//回复到原本的角度
        canvas.rotate(-5 * 72);
        drawText(canvas);
        drawScore(canvas);
    }

    private void drawRadar(Canvas canvas) {
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;
        canvas.translate(cx, cy);

        canvas.rotate(-90);
        int distanceAngle = 360 / 5;

        linePaint.setColor(0xFF444444);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(0, 0, radius, 0, linePaint);
            canvas.rotate(distanceAngle);
        }
        canvas.save();

        int perRadius = radius / 6;
        for (int j = 1; j <= 6; j++) {
            int distance = perRadius * j;
            for (int i = 0; i < 5; i++) {
                canvas.drawLine(distance, 0, (float) (distance * Math.cos(Math.toRadians(72))),
                        (float) (distance * Math.sin(Math.toRadians(72))), linePaint);
                canvas.rotate(72);
            }
        }
        canvas.save();
    }

    private void drawText(Canvas canvas) {
        int textRadius = radius + dp2Px(5);
        int startX;
        int startY;

        Rect rect;
        for (int i = 0; i < 5; i++) {
            startX = (int) (textRadius * Math.sin(Math.toRadians(i * 72)));
            startY = -(int) (textRadius * Math.cos(Math.toRadians(i * 72)));
            canvas.translate(startX, startY);
            String text = texts[i];
            rect = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), rect);
            Path path = new Path();
            if (i == 0) {
                path.setLastPoint(-rect.width() / 2, 0);
                path.lineTo(rect.width() / 2, 0);
            } else if (i == 1) {
                path.setLastPoint(0, rect.height() / 2);
                path.lineTo(rect.width(), rect.height() / 2);
            } else if (i == 2) {
                path.setLastPoint(0, rect.height() / 2);
                path.lineTo(rect.width(), rect.height() / 2);
            } else if (i == 3) {
                path.setLastPoint(-rect.width(), rect.height() / 2);
                path.lineTo(0, rect.height() / 2);
            } else if (i == 4) {
                path.setLastPoint(-rect.width(), rect.height() / 2);
                path.lineTo(0, rect.height() / 2);
            }
            canvas.drawTextOnPath(text, path, 0, 0, textPaint);
            canvas.translate(-startX, -startY);
        }

        canvas.save();
    }

    private void drawScore(Canvas canvas) {
        Path path = new Path();
        float startX = 0;
        float startY = 0;
        for (int i = 0; i < 5; i++) {
            int score = scores[i];
            if (i == 0) {
                path.moveTo(0, -radius * score / 6);
            } else if (i == 1) {
                startX = (float) (radius * score / 6 * Math.sin(Math.toRadians(72)));
                startY = -(float) (radius * score / 6 * Math.cos(Math.toRadians(72)));
            } else if (i == 2) {
                startX = (float) (radius * score / 6 * Math.cos(Math.toRadians(54)));
                startY = (float) (radius * score / 6 * Math.sin(Math.toRadians(54)));
            } else if (i == 3) {
                startX = -(float) (radius * score / 6 * Math.sin(Math.toRadians(36)));
                startY = (float) (radius * score / 6 * Math.cos(Math.toRadians(36)));
            } else if (i == 4) {
                startX = -(float) (radius * score / 6 * Math.sin(Math.toRadians(72)));
                startY = -(float) (radius * score / 6 * Math.cos(Math.toRadians(72)));
            }
            Log.i(TAG, "startX:" + startX + ";" + "startY" + startY);
            if (i != 0) {
                path.lineTo(startX, startY);
            }
        }

        path.close();
        canvas.drawPath(path, imagePaint);
    }

    private int dp2Px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    private int sp2Px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                getResources().getDisplayMetrics());
    }
}
