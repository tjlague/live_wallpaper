package com.example.live_wallpaper.service;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class SystemTimeWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new SystemTimeEngine();
    }

    private class SystemTimeEngine extends Engine {
        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

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

                if (canvas != null) {
                    Paint p = new Paint();
                    p.setTextSize(20);
                    p.setAntiAlias(true);
                    String text = "system time: " + Long.toString(System.currentTimeMillis());
                    float w = p.measureText(text, 0, text.length());
                    int offset = (int) w / 2;
                    int x = canvas.getWidth() / 2 - offset;
                    int y = canvas.getHeight() / 2;
                    p.setColor(Color.BLACK);
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
                    p.setColor(Color.WHITE);
                    canvas.drawText(text, x, y, p);
                }
            } finally {
                if (canvas != null){
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

