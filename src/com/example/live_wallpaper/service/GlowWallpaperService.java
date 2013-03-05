package com.example.live_wallpaper.service;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import com.example.live_wallpaper.drawable_shape.GlowDrawableShape;

public class GlowWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new GlowEngine();
    }

    private class GlowEngine extends Engine {
        private GlowDrawableShape mDrawable;
        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public GlowEngine() {
            super();
            mDrawable = new GlowDrawableShape();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;

            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mUpdateDisplay);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                mDrawable.setBounds(canvas.getClipBounds());
                mDrawable.draw(canvas);
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            mHandler.removeCallbacks(mUpdateDisplay);

            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, 100);
            }
        }
    }
}
