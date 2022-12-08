package com.llk.glide_fix_bug;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.llk.glide_fix_bug.glide_2.GlideRetargetException;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iv);

        //这不是重定向的url，想要测试自己模拟一个
        String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.huabanimg.com%2Fab0b1a766c67978bbfc3c3e865001f32e561903c144bf-Y8fHs5_fw658&refer=http%3A%2F%2Fhbimg.huabanimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1672987642&t=bf9e774a22a34b6880e10658892bbbba";

        //方法1
//        GlideApp.with(this)
//                .load(new RetargetableGlideUrl(url))
//                .into(imageView);

        //方法2
        GlideApp.with(this)
                .load(url)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null && !e.getRootCauses().isEmpty()){
                            for (Throwable throwable : e.getRootCauses()){
                                if (throwable instanceof GlideRetargetException){

                                    GlideRetargetException exception = (GlideRetargetException) throwable;
                                    //重新请求
                                    handler.post(() ->
                                            Glide.with(MainActivity.this)
                                            .load(exception.getRedirectedUrlUrl())
                                            .into(imageView));
                                    break;
                                }
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
}