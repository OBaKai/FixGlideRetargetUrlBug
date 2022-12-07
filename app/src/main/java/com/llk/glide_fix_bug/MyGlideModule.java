package com.llk.glide_fix_bug;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.llk.glide_fix_bug.glide.RetargetableGlideUrl;
import com.llk.glide_fix_bug.glide.RetargetableOkhttpUrlLoader;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public final class MyGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        builder.cache(null);
        //todo 测试用拦截器
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                HttpUrl url = request.url();
//                log("req：" + url);
//
//                Response response = chain.proceed(request);
//                String response_str;
//                if (isRedirected(response)){
//                    response_str = "res(redirected)：" + response.request().url();
//                }else {
//                    response_str = "res：" + url;
//                }
//                log(response_str);
//
//                return response;
//            }
//
//            private boolean isRedirected(Response response) {
//                Response priorResponse = response.priorResponse();
//                if (priorResponse != null) {
//                    int priorResponseCode = priorResponse.code();
//                    return priorResponseCode == HttpURLConnection.HTTP_MOVED_PERM || priorResponseCode == HttpURLConnection.HTTP_MOVED_TEMP;
//                }
//                return false;
//            }
//
//            private void log(String msg){
//                Log.e("llk", msg);
//            }
//        });
        OkHttpClient okHttpClient = builder.build();
        registry.replace(RetargetableGlideUrl.class, InputStream.class, new RetargetableOkhttpUrlLoader.Factory(okHttpClient));
    }
}
