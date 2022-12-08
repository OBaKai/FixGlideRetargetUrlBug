package com.llk.glide_fix_bug;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.llk.glide_fix_bug.glide_2.RetargetableOkhttpUrlLoader;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 方法2、感知重定向，如果发生重定向将本次加载视为失败，并且将重定向url抛出，使用新url再次进行加载。
 * 该方法适合app内 少量 网络资源链接都存在重定向的情况。
 */
@GlideModule
public final class MyGlideModule_2 extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        builder.cache(null);
        OkHttpClient okHttpClient = builder.build();
        registry.replace(GlideUrl.class, InputStream.class, new RetargetableOkhttpUrlLoader.Factory(okHttpClient));
    }
}
