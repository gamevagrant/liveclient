package com.meetvr.liveshowclient;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.meetvr.LoginControl;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.PointResponse;
import com.meetvr.share.request.LoginRequest;
import com.meetvr.share.request.PointRequest;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.meetvr.share.view.ProgessDlg;

import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener ,RegistInterface{



    private EditText userEdit,pwdEdit;
    private ImageButton eyeBtn;

    private ProgessDlg progessDlg;
    private UserInfo userInfo = new UserInfo();

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
        setContentView(R.layout.activity_login);
        Mutils.hideAction(this);
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();

    }

    private void iniUi(){
        findViewById(R.id.back).setOnClickListener(this);
        Mutils.setTitle(findViewById(R.id.title_txt),"登录");
        findViewById(R.id.del_btn).setOnClickListener(this);
        eyeBtn = (ImageButton) findViewById(R.id.eye_btn);
        eyeBtn.setOnClickListener(this);
        //findViewById(R.id.eye_btn).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.reg_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.reg_btn).setOnClickListener(this);
        findViewById(R.id.get_pwd_txt).setOnClickListener(this);

        userEdit  = (EditText) findViewById(R.id.user_id);
        pwdEdit = (EditText)findViewById(R.id.pwd);
        userEdit.setText(PreferencesUtils.getSharePreStr(this,"user"));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
          case   R.id.back:
              Intent intentGuid = new Intent(LoginActivity.this,GuideActivity.class);
              startActivity(intentGuid);
              finish();
            break;
            case R.id.del_btn:
                userEdit.setText("");
                break;
            case R.id.eye_btn:
                changeInputType();
                break;
            case R.id.login_btn:
//                Intent intent = new Intent(this,MainActivity.class);
//                intent.putExtra("meetvr_live",getPlatformData());
//                startActivity(intent);
                //startMainTable();
                loginNow(view);
                break;
            case R.id.reg_btn:
                Intent intent = new Intent(this,RegistActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.get_pwd_txt:
               // Intent intent1 = new Intent(this,ModifyPwdActivity.class);
               // intent1.putExtra(ModifyPwdActivity.MODIFY_TYPE,0);// 0 是密码 1 是修改昵称
               // intent1.putExtra(ModifyPwdActivity.FROM_TYPE,1);// 1是第一次登录
               // startActivity(intent1);
                startGetPwdView();
                break;
        }
    }

    private void startGetPwdView(){
        Intent intent = new Intent(this,GetPwdActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResulte(boolean isOk) {
        if(isOk){
            MyApplication application =(MyApplication) getApplication();
            application.saveUser(userInfo);
//            if(userInfo.getFirst_login()==0){
//                Intent intent = new Intent(this,ModifyPwdActivity.class);
//                intent.putExtra(ModifyPwdActivity.FROM_TYPE,1);
//                intent.putExtra(ModifyPwdActivity.MODIFY_TYPE,0);
//                intent.putExtra(ModifyPwdActivity.IDENTIFIER,userInfo.getId());
//                intent.putExtra(ModifyPwdActivity.TOKEN,userInfo.getToken());
//                startActivity(intent);
//
//            }else{
                //保存为账号登录的
                PreferencesUtils.putSharePre(this,"login_type",1); //1 账号登录  2 微信登录
                Intent intent = new Intent(this,MainTabActivity.class);
                startActivity(intent);
               //pointEnter(userInfo.getUsercode());

  //          }
            finish();
        }
    }
    /*
    private void pointEnter(String usercoder){
        PointRequest pointRequest = new PointRequest(usercoder,this);
        pointRequest.addArray("vrlive_login","intime",""+new Date().getTime());
        pointRequest.addArray("vrlive_login","outtime",""+new Date().getTime());
        pointRequest.addArray("vrlive_login","state","0");
        pointRequest.addArray("vrlive_login","value","2");
        new Control(this,null,pointRequest,new PointResponse(),null);
    }
    */
    @Override
    public void onResulte(boolean isOk, BaseResponse baseResponse) {

    }

    private void loginNow(View view){
    String user = userEdit.getText().toString();
    String password = pwdEdit.getText().toString();
    if (TextUtils.isEmpty(password)) {
        //pwdEdit.setError(getString(R.string.error_invalid_password));
        Toast.makeText(LoginActivity.this,R.string.error_invalid_password,Toast.LENGTH_SHORT);
        return;

    }
    // Check for a valid email address.
    if (TextUtils.isEmpty(user)) {
        //userEdit.setError(getString(R.string.error_field_required));
        Toast.makeText(LoginActivity.this,R.string.error_field_required,Toast.LENGTH_SHORT);
        return;

    }
    PreferencesUtils.putSharePre(this,"user",user);
    PreferencesUtils.putSharePre(this,"pwd",password);
    //开始登录
    UserRegistInfo userRegistInfo = new UserRegistInfo();
    userRegistInfo.setPhone(user);
    userRegistInfo.setPassword(Mutils.getMD5(password+"A1#"));
    userRegistInfo.setApp_ver(Constants.APP_VER);
    userRegistInfo.setDev_info(Mutils.getInfo());
    userRegistInfo.setOs_ver(Mutils.getInfo());

     progessDlg = new ProgessDlg(this,view);
    new LoginControl(this,this,userRegistInfo,userInfo,new LoginRequest(),progessDlg);

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
            Intent intentGuid = new Intent(LoginActivity.this,GuideActivity.class);
            startActivity(intentGuid);
            finish();

        }

        return super.onKeyDown(keyCode, event);
    }

    private void startMainTable(){
        Intent intent = new Intent(this,MainTabActivity.class);
        startActivity(intent);
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
    private void changeInputType(){
        if(isPasswordInputType(pwdEdit.getInputType())){
            pwdEdit.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeBtn.setBackgroundResource(R.drawable.ic_input_visible);
            Log.e("lao","ddd");
        }else{
            pwdEdit.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            eyeBtn.setBackgroundResource(R.drawable.ic_input_invisible);
            Log.e("lao","yinchang");
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

}

