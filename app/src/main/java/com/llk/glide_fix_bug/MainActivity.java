package com.llk.glide_fix_bug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.llk.glide_fix_bug.glide.RetargetableGlideUrl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iv);

        //这不是重定向的url，想要测试自己模拟一个
        String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.huabanimg.com%2Fab0b1a766c67978bbfc3c3e865001f32e561903c144bf-Y8fHs5_fw658&refer=http%3A%2F%2Fhbimg.huabanimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1672987642&t=bf9e774a22a34b6880e10658892bbbba";

        GlideApp.with(this)
                .load(new RetargetableGlideUrl(url))
                .into(imageView);
    }
}