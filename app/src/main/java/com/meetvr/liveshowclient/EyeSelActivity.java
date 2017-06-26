package com.meetvr.liveshowclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.meetvr.liveshowclient.adapter.EyeViewAdapter;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;

import java.util.ArrayList;

public class EyeSelActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView  listView;
    private BaseAdapter adapter;
    private ArrayList<String> datas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_sel);
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
        try {

            Mutils.setTitle(findViewById(R.id.title_txt),"选择眼镜");
            findViewById(R.id.back).setOnClickListener(this);
            listView = (ListView)findViewById(R.id.listview);
            addEyeType();
            adapter = new EyeViewAdapter(this,datas);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PreferencesUtils.putSharePre(EyeSelActivity.this,"sel",i);
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEyeType(){
        try {
            datas.add("暴风墨镜5");
            datas.add("暴风墨镜4");
            datas.add("小鸟看看");
            datas.add("大朋看看");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }

    }
}
