package com.llk.glide_fix_bug;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.llk.glide_fix_bug.glide_1.RetargetableGlideUrl;
import com.llk.glide_fix_bug.glide_1.RetargetableOkhttpUrlLoader;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 方法1、感知重定向，将重定向后的url更新给Glide，让Glide用重定向后的url进行缓存。
 * 该方法适合app内 大量 网络资源链接都存在重定向的情况。
 *
 * todo 方法1 只测试了glide disk cache，memory cache并没有测试
 */
@GlideModule
public final class MyGlideModule_1 extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        builder.cache(null);
        OkHttpClient okHttpClient = builder.build();
        registry.replace(RetargetableGlideUrl.class, InputStream.class, new RetargetableOkhttpUrlLoader.Factory(okHttpClient));
    }
}
