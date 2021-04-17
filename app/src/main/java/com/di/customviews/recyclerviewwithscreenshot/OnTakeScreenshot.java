package com.di.customviews.recyclerviewwithscreenshot;

import android.graphics.Bitmap;

/**
 * Interface to inform the implementer when the screenshot done.
 */
public interface OnTakeScreenshot {
    /**
     * Delivers the implementer the screenshot when is done.
     *
     * @param screenshot Bitmap representing the screenshot taken.
     */
    void onScreenshotReady(Bitmap screenshot);
}