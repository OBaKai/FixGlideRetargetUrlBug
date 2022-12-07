package com.llk.glide_fix_bug.glide;


import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import okhttp3.Call;

public class RetargetableOkhttpUrlLoader implements ModelLoader<RetargetableGlideUrl, InputStream> {

    private final Call.Factory myClient;

    public RetargetableOkhttpUrlLoader(@NonNull Call.Factory client) {
        myClient = client;
    }

    @Override
    public boolean handles(@NonNull RetargetableGlideUrl glideUrl) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull RetargetableGlideUrl model, int width, int height, @NonNull Options options) {
        return new LoadData<>(model, new RetargetableOkHttpStreamFetcher(myClient, model));
    }

    public static class Factory implements ModelLoaderFactory<RetargetableGlideUrl, InputStream> {

        private final Call.Factory myClient;

        public Factory(@NonNull Call.Factory client) {
            myClient = client;
        }

        @NonNull
        @Override
        public ModelLoader<RetargetableGlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new RetargetableOkhttpUrlLoader(myClient);
        }

        @Override
        public void teardown() {}
    }
}
