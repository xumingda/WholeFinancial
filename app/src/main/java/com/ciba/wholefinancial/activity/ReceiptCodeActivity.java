package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.BaseApplication;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.ClockProtocol;
import com.ciba.wholefinancial.protocol.GetQrCodeProtocol;
import com.ciba.wholefinancial.protocol.GetTgCodeProtocol;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.GetQrCodeResponse;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//收款二维码
public class ReceiptCodeActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back;
    private Dialog loadingDialog;
    private Gson gson;
    private int businessShopId;
    private ImageLoader imageLoader;

    private ImageView iv_pic;
    private String code_url;
    private Button btn_save;
    private TextView tv_title;
    private String title;
    private SalesmanBean salesmanBean;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_receipt_code, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        title=getIntent().getStringExtra("title");
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        salesmanBean= SPUtils.getBeanFromSp(this,"salesmanObject","SalesmanBean");
        businessShopId=getIntent().getIntExtra("businessShopId",0);
        iv_pic=(ImageView) findViewById(R.id.iv_pic);
        btn_save=(Button) findViewById(R.id.btn_save);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        view_back=(View)findViewById(R.id.view_back);
        tv_title=(TextView) findViewById(R.id.tv_title);
        view_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        if(!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
            getTgCode();
        }else{
            getQrCode();
        }

    }




    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    Toast.makeText(ReceiptCodeActivity.this,"开始保存图片...",Toast.LENGTH_SHORT).show();
                    btn_save.setClickable(false);
                    break;
                case SAVE_SUCCESS:
                    Toast.makeText(ReceiptCodeActivity.this,"图片保存成功,请到相册查找",Toast.LENGTH_SHORT).show();
                    btn_save.setClickable(true);
                    break;
                case SAVE_FAILURE:
                    Toast.makeText(ReceiptCodeActivity.this,"图片保存失败,请稍后再试...",Toast.LENGTH_SHORT).show();
                    btn_save.setClickable(true);
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                        Bitmap bp = returnBitMap(code_url);
                        saveImageToPhotos(ReceiptCodeActivity.this,bp);
                    }
                }).start();
                break;
            }

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }
    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public  Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存二维码到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }



    //获取二维码
    public void getQrCode() {
        loadingDialog.show();
        GetQrCodeProtocol getQrCodeProtocol = new GetQrCodeProtocol();

        String url = getQrCodeProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("businessShopId", String.valueOf(businessShopId));



        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetQrCodeResponse getQrCodeResponse = gson.fromJson(json, GetQrCodeResponse.class);
                LogUtils.e("getQrCodeResponse:" + getQrCodeResponse.toString());
                loadingDialog.dismiss();
                if (getQrCodeResponse.code==0) {
                    code_url=getQrCodeResponse.getData().qrUrl;
                    imageLoader.displayImage(code_url, iv_pic, PictureOption.getSimpleOptions());
                } else {
                    DialogUtils.showAlertDialog(ReceiptCodeActivity.this,
                            getQrCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ReceiptCodeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(ReceiptCodeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }


    //获取推广二维码
    public void getTgCode() {
        loadingDialog.show();
        GetTgCodeProtocol getQrCodeProtocol = new GetTgCodeProtocol();

        String url = getQrCodeProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("salesmanId", String.valueOf(salesmanBean.getId()));



        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetQrCodeResponse getQrCodeResponse = gson.fromJson(json, GetQrCodeResponse.class);
                LogUtils.e("getQrCodeResponse:" + getQrCodeResponse.toString());
                loadingDialog.dismiss();
                if (getQrCodeResponse.code==0) {
                    code_url=getQrCodeResponse.getData().qrUrl;
                    imageLoader.displayImage(code_url, iv_pic, PictureOption.getSimpleOptions());
                } else {
                    DialogUtils.showAlertDialog(ReceiptCodeActivity.this,
                            getQrCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ReceiptCodeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(ReceiptCodeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

}
