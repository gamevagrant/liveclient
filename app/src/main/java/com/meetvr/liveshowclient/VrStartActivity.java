package com.meetvr.liveshowclient;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.meetvr.share.utrils.Mutils;

public class VrStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_start);
        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        Mutils.setWindowStatusBarColor(this,R.color.color_black);

        Log.e("Unity","VrStartActivity");

        ImageView img = (ImageView)findViewById(R.id.vr_ani);
        img.setImageResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable)img.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // int ori = getResources().getConfiguration().orientation;
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){ //启动vr

            Intent intent = new Intent(this,com.meetvr.liveshowclient.MainActivity.class);
            intent.putExtra("meetvr_live",getIntent().getStringExtra("meetvr_live"));
            startActivity(intent);
           /// Toast.makeText(this,"start vr",Toast.LENGTH_SHORT).show();
           Log.d("start_param",getIntent().getStringExtra("meetvr_live"));
            finish();
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("Unity","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        // 当新设置中，屏幕布局模式为横排时
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("meetvr_live",getIntent().getStringExtra("meetvr_live"));
            startActivity(intent);
            /// Toast.makeText(this,"start vr",Toast.LENGTH_SHORT).show();
            //Log.e("start_param",getIntent().getStringExtra("meetvr_live"));
            finish();
            Log.d("start_param",getIntent().getStringExtra("meetvr_live"));
        }

    }
}
