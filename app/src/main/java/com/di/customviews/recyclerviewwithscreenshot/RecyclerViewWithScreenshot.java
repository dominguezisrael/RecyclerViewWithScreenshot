package com.di.customviews.recyclerviewwithscreenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * RecyclerView that offers the functionality to take a screenshot of its Adapter contents.
 */
public class RecyclerViewWithScreenshot extends RecyclerView {
    private static final int SCROLLVIEW_CONTAINER_ID = ViewCompat.generateViewId();

    /**
     * Constructor.
     *
     * @param context Application context.
     */
    public RecyclerViewWithScreenshot(@NonNull Context context) {
        super(context);
    }

    /**
     * Constructor.
     *
     * @param context Application context.
     * @param attrs   Attributes for this view.
     */
    public RecyclerViewWithScreenshot(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor.
     *
     * @param context      Application context.
     * @param attrs        Attributes for this view.
     * @param defStyleAttr Defined styles attributes for this view.
     */
    public RecyclerViewWithScreenshot(@NonNull Context context, @Nullable AttributeSet attrs,
                                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof RecyclerViewAdapterScreenshot) {
            super.setAdapter(adapter);
        } else {
            Log.e(RecyclerViewWithScreenshot.class.getSimpleName(),
                    "Adapter must be an instance of " + RecyclerViewAdapterScreenshot.class.toString());
        }
    }

    /**
     * Take screenshot of the contents of this RecyclerView's adapter.
     *
     * @param callback Callback to inform when the screenshot is ready.
     */
    public void takeScreenshot(OnTakeScreenshot callback) {
        ViewGroup viewGroup = (ViewGroup) getParent();

        if (viewGroup.findViewById(SCROLLVIEW_CONTAINER_ID) == null) {
            RecyclerViewAdapterScreenshot srv = (RecyclerViewAdapterScreenshot) getAdapter();

            if (srv != null) {
                ScrollView scrollView = new ScrollView(getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                scrollView.setVisibility(View.INVISIBLE);

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
                        android.R.color.white));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setId(SCROLLVIEW_CONTAINER_ID);

                scrollView.addView(linearLayout);

                for (int index = 0; index < srv.getItemCountForScreenshot(); index++) {
                    RecyclerView.ViewHolder viewHolder = srv.onCreateViewHolderForScreenshot(linearLayout,
                            srv.getItemViewTypeForScreenshot(index));
                    srv.onBindViewHolderForScreenshot(viewHolder, index);

                    viewHolder.itemView.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    viewHolder.itemView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());

                    linearLayout.addView(viewHolder.itemView);
                }

                linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int width = linearLayout.getWidth();
                        int height = linearLayout.getHeight();

                        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        scrollView.setVisibility(View.GONE);

                        Bitmap screenshot = takeScreenshot(linearLayout, width, height);

                        callback.onScreenshotReady(screenshot);
                    }
                });

                viewGroup.addView(scrollView);
            }
        } else {
            LinearLayout linearLayout = viewGroup.findViewById(SCROLLVIEW_CONTAINER_ID);

            Bitmap screenshot = takeScreenshot(linearLayout, linearLayout.getWidth(),
                    linearLayout.getHeight());

            callback.onScreenshotReady(screenshot);
        }
    }

    /**
     * Take a screenshot of some container.
     *
     * @param screenshotContainerDetails Container to take screenshot of.
     * @param width                      Width of container to take screenshot of.
     * @param height                     Height of container to take screenshot of.
     * @return Bitmap with screenshot taken.
     */
    private Bitmap takeScreenshot(View screenshotContainerDetails, int width, int height) {
        Bitmap screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(screenshot);

        screenshotContainerDetails.draw(canvas);

        return screenshot;
    }
}