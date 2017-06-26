package com.meetvr.liveshowclient;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.ModifyNicknameRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.ImageTools;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.CircleImageView;
import com.meetvr.share.view.ProgessDlg;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private UserInfo userInfo;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;

    private static final int SCALE = 5;//照片缩小比例

    private CircleImageView headIimg;
    MyApplication application  ;
    Uri imageUri;
    private static final int REQUEST_READ_CONTACTS = 0;
    private String pngFileName;

    private ProgessDlg progessDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
       // Mutils.hideAction(this);
        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }

        application =(MyApplication) getApplication();

        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        getUserInfo();
        iniUi();
    }
    private void iniUi(){
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.head_layout).setOnClickListener(this);
        findViewById(R.id.nickname_layout).setOnClickListener(this);
        findViewById(R.id.modify_pwd_layout).setOnClickListener(this);
        Mutils.setTitle(findViewById(R.id.title_txt),"修改资料");
        headIimg = (CircleImageView)findViewById(R.id.live_info_header);
        application.displayImg(userInfo.getHeadphoto()+"?imageView2/1/w/100/h/100",headIimg,R.drawable.avatar_bg,R.drawable.avatar_bg);

    }

    private void getUserInfo(){

        userInfo = application.getUserInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Mutils.setTitle(findViewById(R.id.nickname_txt),userInfo.getUsername());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.head_layout:
               // upLoadFile();
               showPicturePicker(this);
               // startPhotoZoom(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"image.jpg"),100);
                break;
            case R.id.nickname_layout:
                changeNickName(1);
                break;
            case  R.id.modify_pwd_layout:
                changeNickName(0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(progessDlg!=null){
                return false;
            }


        }

        return super.onKeyDown(keyCode, event);
    }

    private void shiwProgress(){
        progessDlg = new ProgessDlg(this,headIimg);
        progessDlg.showPopupWindow();
    }
    private void hideProgess(){
        progessDlg.dismiss();
        progessDlg = null;
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(CAMERA)&&shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(headIimg, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // populateAutoComplete();
                showPicturePicker(this);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode == RESULT_OK) {
                // 拿到剪切数据
               if(data!=null){
                   Bitmap bmap = data.getParcelableExtra("data");
                   if(bmap!=null);
                   headIimg.setImageBitmap(bmap);
                   pngFileName = String.valueOf(System.currentTimeMillis());
                   ImageTools.savePhotoToSDCard(bmap,Environment.getExternalStorageDirectory().getAbsolutePath(), pngFileName);
                   //上传该文件
                   shiwProgress();
                   upLoadFile();
               }


            }

        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                  /*
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    headIimg.setImageBitmap(newBitmap);
                    String  value =  String.valueOf(System.currentTimeMillis());
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), value);
                    imageUri=  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/"+value+".png"));

                    cropImageUri(imageUri,100,100,100);
                    //startPhotoZoom(imageUri,100);
                    */

                    cropImageUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg")),100,100,100);
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        /*
                      Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            headIimg.setImageBitmap(smallBitmap);
                            String  value1 =  String.valueOf(System.currentTimeMillis()) ;
                            ImageTools.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), value1);
                            imageUri=  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/"+value1+".png"));
                            cropImageUri(imageUri,100,100,100);
                            }
                            */
                        cropImageUri(originalUri,100,100,100);

                        // cropImageUri(originalUri,100,100,100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                default:
                    break;
            }
        }
    }



    private Bitmap decodeUriAsBitmap(Uri uri){

        Bitmap bitmap = null;

        try {

            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;

        }

        return bitmap;

    }
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){


        Intent intent = new Intent();

        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent,requestCode);



    }

    public void showPicturePicker(Context context){
        if(!mayRequestContacts()){
            return ;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    //http://cdn.store-online.moxiangtv.com
    private void upLoadFile(){
        UploadManager uploadManager = new UploadManager();
        String data = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+pngFileName+".png";
        String key = pngFileName+".png";
        String token = userInfo.getQiniu_token();
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。
                       //文件上传挖成
                        if(info.isOK()){
                            //修改头像地址
                            try {
                                String urlImg = userInfo.getQiniu_file_domain();
                                if(urlImg.substring(urlImg.length()-1,urlImg.length()).equals("/")){

                                }else{
                                    urlImg+="/";
                                    userInfo.setQiniu_file_domain(urlImg);
                                }
                                saveNickName();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            //Qiniu token过期
                            if(info.error.equals("expired token"))
                            {
                                ModifyInfoActivity.this.sendBroadcast(new Intent("com.login.again"));
                            }
                        }
                    }
                }, null);

//        uploadManager.put(data, key, token,handler,
//                new UploadOptions(null, null, false,
//                        new UpProgressHandler(){
//                            public void progress(String key, double percent){
//                                Log.i("qiniu", key + ": " + percent);
//                            }
//                        }, null));
    }


    private void saveNickName(){

        ModifyNicknameRequest modifyNicknameRequest = new ModifyNicknameRequest(userInfo.getToken(),userInfo.getId());
        // modifyPwdRequest.setUrlParam();
        modifyNicknameRequest.setParam(userInfo.getId(),null,userInfo.getQiniu_file_domain()+"/"+pngFileName+".png");
        modifyNicknameRequest.addQuery(this, new Request.OnNetWorkProc() {
            @Override
            public void onStart() {

            }

            @Override
            public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                try {
                    if(isOK){
                        Response response1 = new Response();
                        response1.parse(response);
                        if(Mutils.isResponseOk(response1.getAct_stat())){
                            Toast.makeText(ModifyInfoActivity.this,"头像修改成功", Toast.LENGTH_SHORT).show();
                            ((MyApplication)getApplication()).getUserInfo().setHeadphoto(userInfo.getQiniu_file_domain()+"/"+pngFileName+".png");
                        }else{
                            Toast.makeText(ModifyInfoActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(ModifyInfoActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//                        if(errMsg!=null&&errMsg.length()>0){
//                            Toast.makeText(ModifyInfoActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(ModifyInfoActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//                        }

                    }
                   hideProgess();

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeNickName(int type){
        Intent intent = new Intent(this,ModifyPwdActivity.class);
        intent.putExtra(ModifyPwdActivity.FROM_TYPE,0);
        intent.putExtra(ModifyPwdActivity.MODIFY_TYPE,type);//0 pwd 1 nick
        intent.putExtra(ModifyPwdActivity.IDENTIFIER,userInfo.getId());
        intent.putExtra(ModifyPwdActivity.TOKEN,userInfo.getToken());
        startActivity(intent);
    }
}
