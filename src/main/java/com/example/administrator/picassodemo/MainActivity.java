package com.example.administrator.picassodemo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MainActivity extends AppCompatActivity implements Picasso.Listener {
    static String path = "http://img5.imgtn.bdimg.com/it/u=103114099,1736388628&fm=11&gp=0.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.iv);
//---------调用Picasso的静态方法加载图片  即 Picasso.with(this) 返回值为一个Picasso对象---------------------------------------------------------------------------
//        Picasso.with(this).invalidate(path);//清除Picasso的本地缓存，缓存的名字可能就是根据路径生成，要重新从网络请求该图片时可先调用该方法。
//        //Picasso自带二级缓存机制，内存缓存为LurCache方式。
//        //请求时默认先从内存，本地查找，再去网络请求。如果本地和网络都没找到，就会把error（）方法设置的默认图片设置给传进的ImageView
//        //Picasso已封装好异步与回调。
//        Picasso.with(this).load(path).error(R.mipmap.ic_launcher).into(iv);//从指定的路径去异步请求一张图片，该路径可以是一个网络URL，可以是一个本地路径.
//---------利用建造者模式来获得Picasso对象-------------------------------------------------------------------------------------
        Picasso.Listener listener = this;//生成一个监听对象
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(listener);//设置监听，用来监听图片加载失败的事件，回调接口如下onImageLoadFailed(Picasso picasso, Uri uri, Exception e)方法。
        builder.build().load(path).resize(50,50)//resize(int targetwidth,int targetHight) 该方法用于重新设置图片宽高，减少内存开销
                .error(R.mipmap.ic_launcher).into(iv);//加载图片，同上
//        builder.build().load(path)
//                .transform(new CropSquareTransformation())//和resize()方法相同
//                .into(iv);
//---------Picasso在Adapter中加载图片时，Adapter的重用会被自动检测到，Picasso会自动取消上次的加载---------------------------------------------------------------------------------------------------

    }

    //加载图片失败的接口回调
    @Override
    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
        Toast.makeText(this,"请求失败",Toast.LENGTH_SHORT).show();
    }


    //实现Picasso框架中的Transformation接口以达到自定义重新设置图片宽高的目的
    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }
        @Override public String key() { return "square()"; }
    }
}
