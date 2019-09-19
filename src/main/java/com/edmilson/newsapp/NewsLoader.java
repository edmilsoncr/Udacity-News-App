package com.edmilson.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import java.util.List;

/**
 * Loads a News List using a AsyncTask to make a network requisition
 * to the given URL*/
public class NewsLoader extends AsyncTaskLoader<List<News>>{

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** URL for the search */
    private String mUrl;

    /**
     * Make a new {@link NewsLoader}
     * @param context is the activity's context
     * @param url is the given url which will give the data
     */
    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /** This is the background Thread */
    @Nullable
    @Override
    public List<News> loadInBackground() {
        // Doesn't make a request if no url was provided or if it's null
        if (mUrl == null) {
            return null;
        }
        // Make a network requisition, decode thw response and extract the news List
        List<News> result = QueryUtils.fetchNewsData(mUrl);
        return result;
    }
}
