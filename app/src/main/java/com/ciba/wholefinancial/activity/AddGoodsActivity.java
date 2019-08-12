package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.protocol.AddGoodsProtocol;
import com.ciba.wholefinancial.protocol.SubmitBusinessInfoProtocol;
import com.ciba.wholefinancial.protocol.UpdateGoodsProtocol;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MobileUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.ProviderUtil;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddGoodsActivity extends BaseActivity implements View.OnClickListener , OnSetViewCallBack {

    private LayoutInflater mInflater;
    private View rootView;

    private SalesmanBean salesmanBean;
    private View view_back;
    private Button btn_commit;
    private int businessId;
    private Dialog loadingDialog;
    private Gson gson;
    private EditText et_goods_name;
    private EditText et_price;
    private ImageView iv_goods;
    private ImageView iv_add;

    private EditText et_danwei;
    private EditText et_des;


    private File mFile;
    private String timepath;


    private static final String IMAGE_UNSPECIFIED = "image/*";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private String path;
    private  Bitmap new_photo;
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private LinearLayout ll_main;

    private String goodsPic;
    private String goodsName;
    private String goodsUnit;
    private String goodsDesc;
    private String classId;
    private String goodsId;
    private String goodsPrice;
    private GoodsBean goodsBean;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_add_goods, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        salesmanBean= SPUtils.getBeanFromSp(AddGoodsActivity.this,"salesmanObject","SalesmanBean");
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        root.findViewById(R.id.btn_Phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                UIUtils.startActivityForResult(intent, PHOTO_ZOOM);
            }
        });
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pWindow.dismiss();
                        timepath = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            mFile = new File(dir, timepath + ".jpg");
                            if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AddGoodsActivity.this,
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
                                    uri = FileProvider.getUriForFile(AddGoodsActivity.this, ProviderUtil.getFileProviderName(AddGoodsActivity.this), mFile);
                                }
                                UIUtils.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                        MediaStore.EXTRA_OUTPUT, uri), PHOTO_GRAPH);
                            }
                        }
                    }
                });
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);

        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.dismiss();
            }
        });


        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        businessId=getIntent().getIntExtra("businessId",0);
        btn_commit=(Button) findViewById(R.id.btn_commit);
        ll_main=(LinearLayout) findViewById(R.id.ll_main);
        et_danwei=(EditText)findViewById(R.id.et_danwei);
        view_back=(View)findViewById(R.id.view_back);
        et_goods_name=(EditText)findViewById(R.id.et_goods_name);
        et_des=(EditText)findViewById(R.id.et_des);
        et_price=(EditText)findViewById(R.id.et_price);
        iv_goods=(ImageView) findViewById(R.id.iv_goods);
        iv_add=(ImageView) findViewById(R.id.iv_add);
        view_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        iv_goods.setOnClickListener(this);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        classId=getIntent().getStringExtra("classId");
        if(TextUtils.isEmpty(classId)){
            goodsBean=(GoodsBean) getIntent().getSerializableExtra("GoodsBean");
            goodsId=String.valueOf(goodsBean.getGoodsId());
            LogUtils.e("classId:ssss"+goodsId);
            et_goods_name.setText(goodsBean.getGoodsName());
            et_price.setText(goodsBean.getGoodsPrice());
            et_danwei.setText(goodsBean.getGoodsUnit());
            et_des.setText(goodsBean.getGoodsDesc());
            imageLoader.displayImage(goodsBean.getGoodsPic(), iv_goods, PictureOption.getSimpleOptions());
            iv_add.setVisibility(View.GONE);
            et_goods_name.addTextChangedListener(new TextWatcher() {
                private CharSequence temp = null;

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    temp = s;
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    if (TextUtils.isEmpty(temp)) {
                        btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
                        btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                    } else {
                        btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_update_btn));
                        btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
                    }

                }
            });

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

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM && data != null) {
            if (data.getData() != null) {
                // 图片信息需包含在返回数据中
                String[] proj = {MediaStore.Images.Media.DATA};
                Uri uri = data.getData();
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, AddGoodsActivity.this);
                        cursor = managedQuery(uri, proj, null, null, null);
                    }
                    if (cursor != null) {
                        // 获取索引
                        int photocolumn = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标一直开头
                        cursor.moveToFirst();
                        // 根据索引值获取图片路径
                        path = cursor.getString(photocolumn);
                    } else {
                        path = uri.getPath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (MobileUtils.getSDKVersionNumber() < 14) {
                            cursor.close();
                        }
                    }
                }


                if (!TextUtils.isEmpty(path)) {

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
                    }.start();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_commit:{
                goodsName=et_goods_name.getText().toString();
                goodsDesc=et_des.getText().toString();//
                goodsPrice=et_price.getText().toString();
                goodsUnit=et_danwei.getText().toString();//


                if(goodsBean!=null){
                    updateGoods();
                }else{
                    if(!TextUtils.isEmpty(goodsName)&&!TextUtils.isEmpty(goodsDesc)&&!TextUtils.isEmpty(goodsPrice)&&!TextUtils.isEmpty(goodsUnit)&&!TextUtils.isEmpty(goodsPic)){
                        addGoods();
                    }else{
                        DialogUtils.showAlertDialog(AddGoodsActivity.this,
                                "请把信息填写完整！");
                    }
                }

                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_goods:{
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }

        }
    }

    private void addBitmap(Bitmap bitmap){
        iv_goods.setImageBitmap(bitmap);
        iv_add.setVisibility(View.GONE);
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
                    goodsPic=imageResponse.picUrl;
                    UIUtils.showToastCenter(AddGoodsActivity.this, "上传成功");
                } else {
                    DialogUtils.showAlertDialog(AddGoodsActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddGoodsActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddGoodsActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }

    //提交
    private void addGoods() {
        loadingDialog.show();
        AddGoodsProtocol addGoodsProtocol=new AddGoodsProtocol();
        String url = Constants.SERVER_URL +addGoodsProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        Gson gson = new Gson();
        params.put("authorization",SharedPrefrenceUtils.getString(AddGoodsActivity.this,"token"));
        params.put("goodsName", goodsName);
        params.put("goodsUnit", goodsUnit);
        params.put("goodsDesc", goodsDesc);
        params.put("goodsPic", goodsPic);
        params.put("classId", classId);
        params.put("goodsPrice", goodsPrice);





        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("addGoodsProtocol:" + json.toString());
                SubmitReportResponse submitReportResponse = gson.fromJson(json, SubmitReportResponse.class);

                if (submitReportResponse.code == 0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(AddGoodsActivity.this,AddGoodsActivity.this,"发布成功");
                    clockPopuwindow.showPopupWindow(ll_main);
                } else {
                    if(submitReportResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(AddGoodsActivity.this,
                                submitReportResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(AddGoodsActivity.this, submitReportResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddGoodsActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddGoodsActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    private void updateGoods() {
        LogUtils.e("goodsId:"+goodsId);
        loadingDialog.show();
        UpdateGoodsProtocol updateGoodsProtocol=new UpdateGoodsProtocol();
        String url = Constants.SERVER_URL +updateGoodsProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        Gson gson = new Gson();
        params.put("authorization",SharedPrefrenceUtils.getString(AddGoodsActivity.this,"token"));
        params.put("goodsName", goodsName);
        params.put("goodsUnit", goodsUnit);
        params.put("goodsDesc", goodsDesc);
        params.put("goodsPic", goodsPic);
        params.put("goodsId", goodsId);
        params.put("goodsPrice", goodsPrice);





        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("addGoodsProtocol:" + json.toString());
                SubmitReportResponse submitReportResponse = gson.fromJson(json, SubmitReportResponse.class);

                if (submitReportResponse.code == 0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(AddGoodsActivity.this,AddGoodsActivity.this,"修改成功");
                    clockPopuwindow.showPopupWindow(ll_main);
                } else {
                    if(submitReportResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(AddGoodsActivity.this,
                                submitReportResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(AddGoodsActivity.this, submitReportResponse.msg);
                    }
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddGoodsActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddGoodsActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    @Override
    public void CallBackSuccess(int type) {
        finish();
        overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
    }

    @Override
    public void CallBackDate(List<TypeBean> typeBeanList) {

    }
}
