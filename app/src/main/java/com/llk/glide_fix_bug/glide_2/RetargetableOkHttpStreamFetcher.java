package com.llk.glide_fix_bug.glide_2;


import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Response;

public class RetargetableOkHttpStreamFetcher extends OkHttpStreamFetcher {

    private DataCallback<? super InputStream> myCallback;

    public RetargetableOkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        super(client, url);
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        this.myCallback = callback;
        super.loadData(priority, callback);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (response.isSuccessful()) {
            if (isRedirected(response)){
                String redirectedUrl = response.request().url().toString();
                myCallback.onLoadFailed(new GlideRetargetException(redirectedUrl));
                return;
            }
        }
        super.onResponse(call, response);
    }


    /**
     * llk：判断是否重定向了
     */
    private boolean isRedirected(Response response) {
        Response priorResponse = response.priorResponse();
        if (priorResponse != null) {
            int priorResponseCode = priorResponse.code();
            return priorResponseCode == HttpURLConnection.HTTP_MOVED_PERM || priorResponseCode == HttpURLConnection.HTTP_MOVED_TEMP;
        }
        return false;
    }
}
