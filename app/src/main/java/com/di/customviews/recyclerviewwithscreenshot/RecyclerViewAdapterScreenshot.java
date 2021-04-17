package com.di.customviews.recyclerviewwithscreenshot;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Interface that allows to configure a RecyclerView's Adapter to be able to take a screenshot of
 * its content.
 */
public interface RecyclerViewAdapterScreenshot {
    /**
     * {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}.
     */
    RecyclerView.ViewHolder onCreateViewHolderForScreenshot(@NonNull ViewGroup parent, int viewType);

    /**
     * {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}.
     */
    void onBindViewHolderForScreenshot(@NonNull RecyclerView.ViewHolder holder, int position);

    /**
     * {@link RecyclerView.Adapter#getItemViewType(int)}.
     */
    int getItemViewTypeForScreenshot(int position);

    /**
     * {@link RecyclerView.Adapter#getItemCount()}.
     */
    int getItemCountForScreenshot();
}