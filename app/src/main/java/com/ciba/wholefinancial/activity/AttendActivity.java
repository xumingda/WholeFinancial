package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.BaseApplication;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.datepicker.DateFormatUtils;
import com.ciba.wholefinancial.protocol.ClockProtocol;
import com.ciba.wholefinancial.protocol.SubmitBusinessInfoProtocol;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MobileUtils;
import com.ciba.wholefinancial.util.ProviderUtil;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttendActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private SalesmanBean salesmanBean;
    private View view_back;
    private Dialog loadingDialog;
    private RelativeLayout ll_main;
    private Gson gson;


    private String timepath;


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private String path;



    private File mFile;
    private String attendanceId;
    private View view_pic;
    private ImageView iv_pic,iv;
    private TextView tv_palce;
    private String clickPlace;
    private String pic;
    private LocationService locationService;
    private Bitmap new_photo;
    private Button btn_again,btn_ok;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_attend, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        salesmanBean= SPUtils.getBeanFromSp(AttendActivity.this,"salesmanObject","SalesmanBean");
        attendanceId=getIntent().getStringExtra("attendanceId");
        view_pic=(View)findViewById(R.id.view_pic);
        iv_pic=(ImageView) findViewById(R.id.iv_pic);
        ll_main=(RelativeLayout) findViewById(R.id.ll_main);
        iv=(ImageView) findViewById(R.id.iv);
        tv_palce=(TextView) findViewById(R.id.tv_palce);
        btn_again=(Button) findViewById(R.id.btn_again);
        btn_ok=(Button) findViewById(R.id.btn_ok);
        btn_again.getBackground().setAlpha(100);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        view_back=(View)findViewById(R.id.view_back);

        view_back.setOnClickListener(this);
        view_pic.setOnClickListener(this);
        btn_again.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }



    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    addBitmap(new_photo);
                    uploadImage();
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";
            File file = new File(path);
            String img_path = path;
            if (file.exists()) {
                new Thread() {
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getimage(path);
                            if (photo != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));


                                String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
                                path = BitmapUtils.saveMyBitmap(suoName, new_photo);

                                handler.sendEmptyMessage(INTERCEPT);

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    ;
                }.start();
                //通知相册刷新
                Uri uriData = Uri.parse("file://" + img_path);
                UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:{
                goClock();
                break;
            }
            case R.id.btn_again:{
                timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    mFile = new File(dir, timepath + ".jpg");
                    if (ContextCompat.checkSelfPermission(AttendActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(AttendActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        //解决7.0以上手机拍照问题
                        Uri uri;
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                            uri = Uri.fromFile(mFile);
                        }else{
                            /**
                             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                             */
                            uri = FileProvider.getUriForFile(AttendActivity.this, ProviderUtil.getFileProviderName(AttendActivity.this), mFile);
                        }
                        UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                MediaStore.EXTRA_OUTPUT, uri), PHOTO_GRAPH);
                    }
                }
                break;
            }
            case R.id.view_pic :{
                timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    mFile = new File(dir, timepath + ".jpg");
                    if (ContextCompat.checkSelfPermission(AttendActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(AttendActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        //解决7.0以上手机拍照问题
                        Uri uri;
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                            uri = Uri.fromFile(mFile);
                        }else{
                            /**
                             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                             */
                            uri = FileProvider.getUriForFile(AttendActivity.this, ProviderUtil.getFileProviderName(AttendActivity.this), mFile);
                        }
                        UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                MediaStore.EXTRA_OUTPUT, uri), PHOTO_GRAPH);
                    }
                }
                break;
            }

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    private void addBitmap(Bitmap bitmap){
        iv_pic.setImageBitmap(bitmap);
        iv.setVisibility(View.GONE);
        view_pic.setVisibility(View.GONE);
        btn_again.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.VISIBLE);
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        loadingDialog.show();
        String url = Constants.SERVER_URL + "/common/uploadImg";
        Map<String, String> paramMap = new HashMap<String, String>();
        String token = SharedPrefrenceUtils.getString(UIUtils.getContext(), "token");
        if (!TextUtils.isEmpty(token)) {
            paramMap.put("authorization", token);
        }
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("picFile", path);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                ImageResponse imageResponse = gson.fromJson(json, ImageResponse.class);

                LogUtils.e("baseResponse:" + json.toString());
                if (imageResponse.code == 0) {
                    pic=imageResponse.getPicUrl();
                    UIUtils.showToastCenter(AttendActivity.this, "上传成功");
                } else {
                    DialogUtils.showAlertDialog(AttendActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AttendActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AttendActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }

    //打卡
    public void goClock() {
        loadingDialog.show();
        ClockProtocol clockProtocol = new ClockProtocol();

        String url = clockProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("attendanceId", attendanceId);
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));

        params.put("clickPlace", clickPlace);
        params.put("pic", pic);


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                ClockResponse clockResponse = gson.fromJson(json, ClockResponse.class);
                LogUtils.e("clockResponse:" + clockResponse.toString());
                loadingDialog.dismiss();
                if (clockResponse.code==0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(AttendActivity.this,null,"打卡成功");
                    clockPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(AttendActivity.this,
                            clockResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AttendActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AttendActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        //定位
        locationService.start();
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     *考勤位置
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
//                /**
//                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
//                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
//                 */
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//
//                sb.append("\ncity : ");// 城市
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");// 地址信息
//                sb.append(location.getAddrStr());


                LogUtils.e("clickPlace:"+clickPlace);
                if(TextUtils.isEmpty(tv_palce.getText().toString())){
                    if(location.getAddrStr().equals("null")){
                        tv_palce.setText("");
                    }else {
                        clickPlace=location.getAddrStr();
                        tv_palce.setText(location.getAddrStr());
                    }
                }
            }
        }

    };
}
