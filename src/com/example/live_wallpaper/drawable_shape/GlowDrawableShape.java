package com.example.live_wallpaper.drawable_shape;

import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

public class GlowDrawableShape extends ShapeDrawable {
    private Rect mBounds;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private float mOffsetX = 40.0f;
    private float mOffsetY = 80.0f;
    private float mRadius = 0.0f;
    private float mSpeedX = 10.0f;
    private float mSpeedY = 20.0f;

    private int mColorFG = Color.rgb(0xFF, 0xFF, 0x00); // yellow
    private int mColorBG = Color.rgb(0xFF, 0x66, 0x33); // orange

    public GlowDrawableShape() {
        this(new RectShape());
    }

    public GlowDrawableShape(Shape s) {
        super(s);
    }

    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        mBounds = bounds;
        if (mRadius == 0.0f) {
            mCenterX = (mBounds.right - mBounds.left) / 2.0f;
            mCenterY = (mBounds.bottom - mBounds.top) / 2.0f;
            mRadius = mCenterX + mCenterY;
        }
    }

    public void animate() {
        mCenterX += mSpeedX;
        mCenterY += mSpeedY;

        if (mCenterX < mBounds.left + mOffsetX ||
                mCenterX > mBounds.right - mOffsetX) {
            mSpeedX *= -1.0f;
        }

        if (mCenterY < mBounds.top + mOffsetY ||
                mCenterY > mBounds.bottom - mOffsetY) {
            mSpeedY *= -1.0f;
        }
    }

    public Paint getPaint(float width, float height) {
        animate();

        RadialGradient shader = new RadialGradient(
                mCenterX, mCenterY, mRadius,
                mColorFG, mColorBG,
                Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(shader);
        return paint;
    }

    public void draw(Canvas c) {
        float width = c.getWidth();
        float height = c.getHeight();
        c.drawRect(0, 0, width, height, getPaint(width, height));
    }
}
