package com.meetvr.liveshowclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.meetvr.RegistControl;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.LoginResponse;
import com.meetvr.share.reponse.PointResponse;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.PointRequest;
import com.meetvr.share.request.RegistRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.request.VcodeRequest;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends AppCompatActivity implements OnClickListener ,RegistInterface{



    private EditText userEdit,pwdEdit,pwdEdit1,phoneEdit;
    private ImageButton eyeBtn,eyeBtn1;


    private enum STATE{PHONE,VERCODE};
    private STATE state=STATE.PHONE;


    private ProgessDlg progessDlg;
    private Button getCodeBtn;
    private UserInfo userInfo = new UserInfo();
    private long intime = new Date().getTime();

    private String err_str;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    getCodeBtn.setText(String.valueOf(msg.arg1)+"秒");
                    break;
                case 0://
                    getCodeBtn.setText("获取");
                    getCodeBtn.setEnabled(true);
                    getCodeBtn.setBackgroundResource(R.drawable.login_btn_bg);
                    break;
                case 2:
                    onResulte(true);
                    break;
                case 3:
                    onResulte(false);
                    if(err_str!=null&&err_str.length()>0)
                        Toast.makeText(RegistActivity.this,err_str,Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(RegistActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void actionBarHide(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // actionBarHide();
        setContentView(R.layout.activity_regist);
        Mutils.hideAction(this);
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();

    }

    private void iniUi(){
        findViewById(R.id.back).setOnClickListener(this);
        Mutils.setTitle(findViewById(R.id.title_txt),"注册");
        findViewById(R.id.del_btn).setOnClickListener(this);
        eyeBtn = (ImageButton) findViewById(R.id.eye_btn);
        eyeBtn.setOnClickListener(this);
        eyeBtn1 = (ImageButton) findViewById(R.id.eye_btn2);
        eyeBtn1.setOnClickListener(this);
        //findViewById(R.id.eye_btn).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);




        userEdit  = (EditText) findViewById(R.id.user_id);
        pwdEdit = (EditText)findViewById(R.id.pwd);
        pwdEdit1 =(EditText)findViewById(R.id.pwd2);
        phoneEdit = (EditText)findViewById(R.id.user_id1);
        getCodeBtn = (Button)findViewById(R.id.del_btn1);


        getCodeBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
          case   R.id.back:
              //pointEnterExt("0","2");
              Intent intent = new Intent(this,LoginActivity.class);
              startActivity(intent);
              finish();
            break;
            case R.id.del_btn:
                userEdit.setText("");
                break;
            case R.id.eye_btn:
                changeInputType(eyeBtn,pwdEdit);
                break;
            case R.id.eye_btn2:
                changeInputType(eyeBtn1,pwdEdit1);
                break;
            case R.id.login_btn:
//                Intent intent = new Intent(this,MainActivity.class);
//                intent.putExtra("meetvr_live",getPlatformData());
//                startActivity(intent);
                view.setEnabled(false);
                registNow();
                break;
            case R.id.del_btn1:
                getVcoder();
                break;

        }
    }

    @Override
    public void onResulte(boolean isOk) {
        try {
            findViewById(R.id.login_btn).setEnabled(true);
            if(progessDlg!=null){
                progessDlg.dismiss();
                progessDlg =null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isOk){
            MyApplication application =(MyApplication) getApplication();
            application.saveUser(userInfo);
            Intent intent = new Intent(this,MainTabActivity.class);
            startActivity(intent);
            // 埋点
           // pointEnter(userInfo.getUsercode());
            finish();
        }

    }

    @Override
    public void onResulte(boolean isOk, BaseResponse baseResponse) {

    }

    private void registNow(){

        String userCode = userEdit.getText().toString();
        String code = phoneEdit.getText().toString();
        String pwd1 = pwdEdit.getText().toString();
        String pwd2 = pwdEdit1.getText().toString();

        if(userCode.length()<11){
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            findViewById(R.id.login_btn).setEnabled(true);
            return;
        }

        if(TextUtils.isEmpty(code)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            findViewById(R.id.login_btn).setEnabled(true);
            return;
        }
        if(TextUtils.isEmpty(pwd1)){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            findViewById(R.id.login_btn).setEnabled(true);
            return;
        }
        if(pwd1.length()<8){
            Toast.makeText(this,"密码不能低于8位",Toast.LENGTH_SHORT).show();
            findViewById(R.id.login_btn).setEnabled(true);
            return;
        }
        if(!pwd1.equals(pwd2)){
            Toast.makeText(this,"两次输入不一致",Toast.LENGTH_SHORT).show();
            findViewById(R.id.login_btn).setEnabled(true);
            return;
        }

        UserRegistInfo userRegistInfo = new UserRegistInfo();
        userRegistInfo.setPhone(userCode);
        userRegistInfo.setPassword(Mutils.getMD5(pwd1+"A1#"));
        userRegistInfo.setVcode(code);
        userRegistInfo.setDevice_id(Mutils.getDeviceId());
        userRegistInfo.setDev_info(Mutils.getInfo());
        userRegistInfo.setOs_ver(Mutils.getInfo());
        userRegistInfo.setApp_ver(Constants.APP_VER);

        progessDlg = new ProgessDlg(this,phoneEdit);
        progessDlg.showPopupWindow();
      //  new RegistControl(this,this,userRegistInfo,userInfo,null);
        registHttpNow(userRegistInfo);
    }

    private void registHttpNow(UserRegistInfo userRegistInfo){

        try {
           final   RegistRequest registRequest =  new RegistRequest();
            registRequest.setRequestParam(userRegistInfo.getPhone(),userRegistInfo.getPassword(),userRegistInfo.getDevice_id(),userRegistInfo.getVcode());
            registRequest.setRequestLogin(userRegistInfo.getApp_ver(),userRegistInfo.getDev_info(),userRegistInfo.getOs_ver());

            new Thread(new Runnable() {
                @Override
                public void run() {
                   postData(registRequest.getUrl(),registRequest.getJsonString());
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void postData( String pathUrl ,String requestString ){

            try{
                //String pathUrl = "http://172.20.0.206:8082/TestServelt/login.do";
                //建立连接
                URL url=new URL(pathUrl);
                HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();

                ////设置连接属性
                httpConn.setDoOutput(true);//使用 URL 连接进行输出
                httpConn.setDoInput(true);//使用 URL 连接进行输入
                httpConn.setUseCaches(false);//忽略缓存
                httpConn.setRequestMethod("POST");//设置URL请求方法
               // String requestString = "客服端要以以流方式发送到服务端的数据...";

                //设置请求属性
                //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
                byte[] requestStringBytes = requestString.getBytes("utf-8");
                httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
                httpConn.setRequestProperty("Content-Type", "application/octet-stream");
                httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                httpConn.setRequestProperty("Charset", "UTF-8");
                //
                String name= URLEncoder.encode("黄武艺","utf-8");
                httpConn.setRequestProperty("NAME", name);

                //建立输出流，并写入数据
                OutputStream outputStream = httpConn.getOutputStream();
                outputStream.write(requestStringBytes);
                outputStream.close();
                //获得响应状态
                int responseCode = httpConn.getResponseCode();
                if(HttpURLConnection.HTTP_OK == responseCode){//连接成功

                    //当正确响应时处理数据
                    StringBuffer sb = new StringBuffer();
                    String readLine;
                    BufferedReader responseReader;
                    //处理响应流，必须与服务器响应流输出的编码一致
                    responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    paseUserinfo(sb.toString());
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

    }

    private void paseUserinfo(String data){
        try {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.parse(Mutils.newJson(data),userInfo);
            if(Mutils.isResponseOk(loginResponse.getAct_stat())){
               handler.sendEmptyMessage(2);

            }else{
                //Toast.makeText(RegistActivity.this,loginResponse.getErr_msg(), Toast.LENGTH_SHORT).show();
                err_str = loginResponse.getErr_msg();
              handler.sendEmptyMessage(3);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }
    private void changeInputType(ImageButton btn,EditText edit){
        if(isPasswordInputType(edit.getInputType())){
            edit.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btn.setBackgroundResource(R.drawable.ic_input_visible);
            Log.e("lao","ddd");
        }else{
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            btn.setBackgroundResource(R.drawable.ic_input_invisible);
            Log.e("lao","yinchang");
        }
    }

    private void showProgess(){
        try {
            progessDlg = new ProgessDlg(this,phoneEdit);
            progessDlg.showPopupWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void hideProgress(){
        try {
            if(progessDlg!=null){
                progessDlg.dismiss();
                progessDlg = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(progessDlg!=null){
                return false;
            }
//            if(state==STATE.VERCODE){
//                showPhone(true);
//                state = STATE.PHONE;
//                return false;
//            }
            //pointEnterExt("0","3");
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onKeyDown(keyCode, event);
    }
    private void sleep(long miseconds){
        try {
            Thread.sleep(miseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void getVcoder(){
        try {
            String phoneNum = userEdit.getText().toString();
            if(TextUtils.isEmpty(phoneNum)){
                //userEdit.setError("请输入手机号！");
                Toast.makeText(RegistActivity.this,"请输入手机号！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(phoneNum.length()<11){
                //userEdit.setError("输入位数不够！");
                Toast.makeText(RegistActivity.this,"输入位数不够！",Toast.LENGTH_SHORT).show();
                return;
            }
            VcodeRequest vcodeRequest = new VcodeRequest();
            vcodeRequest.setPhoneDevid(phoneNum,Mutils.getDeviceId());
            vcodeRequest.setTyoe("regist");
            vcodeRequest.addQuery(this, new Request.OnNetWorkProc() {
                @Override
                public void onStart() {
                    showProgess();
                }

                @Override
                public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                    try {
                        if(isOK){
                            Response response1 = new Response();
                            response1.parse(response);
                            if(Mutils.isResponseOk(response1.getAct_stat())){
                                Toast.makeText(RegistActivity.this,"验证码已经发送到您手机！",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegistActivity.this,response1.getErr_msg(),Toast.LENGTH_SHORT).show();
                            }

                            getCodeBtn.setBackgroundResource(R.drawable.bg_btn1_normal_pressed);
                            getCodeBtn.setEnabled(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int len = 60;
                                    while (len>0){
                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.arg1=len;
                                        handler.sendMessage(msg);
                                        sleep(1000);
                                        len--;
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            }).start();
                        }else{
                            Toast.makeText(RegistActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();

//                            if(errMsg!=null&&errMsg.length()>0){
//                                Toast.makeText(RegistActivity.this,errMsg,Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(RegistActivity.this,R.string.net_con_error,Toast.LENGTH_SHORT).show();
//                            }

                        }
                        hideProgress();

                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTestPlayerString(){
               return "{\n" +
                "    \"eye_type\": \"1\",\n" +
                "    \"is_hand\": \"false\",\n" +
                "    \"start_type\": \"0\",\n" +
                "    \"login_data\": \"登陆接口返回的json\",\n" +
                "    \"item_data\": \"由服务器返回的单条json数据\",\n" +
                "    \"user_id\": \"用户账号\",\n" +
                "    \"password\": \"用户的密码\"\n" +
                "}";
    }

    public String getPlatformData(){
      //  JSONObject jsonObject  = new JSONObject("");
//        return "{\n" +
//                "    \"eye_type\": \"1\",\n" +
//                "    \"is_hand\": \"false\",\n" +
//                "    \"start_type\": \"0\",\n" +
//                "    \"login_data\": \"登陆接口返回的json\",\n" +
//                "    \"item_data\": \"由服务器返回的单条json数据\",\n" +
//                "    \"user_id\": \"用户账号\",\n" +
//                "    \"password\": \"用户的密码\"\n" +
//                "}";

        String value  = getIntent().getStringExtra("meetvr_live");

        return value;
    }
    /*
    private void pointEnter(String usercoder){
        PointRequest pointRequest = new PointRequest(usercoder,this);
        pointRequest.addArray("vrlive_reg","ctime",""+new Date().getTime());
        pointRequest.addArray("vrlive_reg","way","2");
        new Control(this,null,pointRequest,new PointResponse(),null);
    }
    private void pointEnterExt(String usercoder,String way){
        PointRequest pointRequest = new PointRequest(usercoder,this);
        pointRequest.addArray("vrlive_reg_sj","intime",""+intime);
        pointRequest.addArray("vrlive_reg_sj","outtime",""+new Date().getTime());

        pointRequest.addArray("vrlive_reg_sj","way",way);
        new Control(this,null,pointRequest,new PointResponse(),null);
    }
*/


}

