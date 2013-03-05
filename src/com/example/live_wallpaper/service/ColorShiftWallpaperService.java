package com.example.live_wallpaper.service;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class ColorShiftWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new ColorShiftEngine();
    }

    private class ColorShiftEngine extends Engine {
        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        private int[] colors = {0, 0, 0};

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
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();

                if (canvas != null) {
                    canvas.drawARGB(200, colors[0], colors[1], colors[2]);
                }

                updateColors(colors);
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

        /**
         * Method updateColors updates the colors by increasing the value per RGB. The values are reset to zero
         * if the maximum value is reached.
         * @param colors to be updated.
         */
        private void updateColors(int[] colors) {
            if (colors[0] < 255) {
                colors[0]++;
            } else {
                if (colors[1] < 255) {
                    colors[1]++;
                } else {
                    if (colors[2] < 255) {
                        colors[2]++;
                    } else {
                        colors[0] = 0;
                        colors[1] = 0;
                        colors[2] = 0;
                    }
                }
            }
        }
    }
}
