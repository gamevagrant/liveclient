package com.meetvr.liveshowclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();
    }

    private void iniUi(){
        Mutils.setTitle(findViewById(R.id.title_txt),"关于我们");
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_updata_layout:

                break;
            case R.id.clean_cache_layout:
                try {
                    MyApplication application = (MyApplication) getApplication();
                    application.getBmpCache().clean();
                    Toast.makeText(this,"清除完成",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.user_xieyi_layout:
                break;
            case R.id.about_me_layout:
                break;

        }
    }
    
}
