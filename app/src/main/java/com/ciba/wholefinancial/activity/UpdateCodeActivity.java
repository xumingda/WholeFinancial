package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.BusinessShopBean;
import com.ciba.wholefinancial.bean.TableCodeBean;
import com.ciba.wholefinancial.protocol.DeleteShopProtocol;
import com.ciba.wholefinancial.protocol.UpdateCodeProtocol;
import com.ciba.wholefinancial.protocol.UpdateShopProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
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

public class UpdateCodeActivity extends BaseActivity implements View.OnClickListener {

    private View view_back;
    private TextView mTextMessage;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_code;
    private TextView tv_code,tv_name;
//    private RelativeLayout rl_clear_new_pwd;
//    private RelativeLayout rl_clear_new_pwd_again;
//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private TableCodeBean tableCodeBean;
    private String code;
    private RelativeLayout rl_main;
    private TextView tv_delete,tv_save,tv_download;
    private AlertDialog alertDialog;
    private File cache;//缓存路径
    private String img_path;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            loadingDialog.dismiss();
            //保存至本地
            //显示
            Bitmap b=(Bitmap)msg.obj;
            try {
                saveJPGFile(b,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+ ".jpg");
                //通知相册刷新
                Uri uriData = Uri.parse("file://" + img_path);
                UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));
                DialogUtils.showAlertDialog(UpdateCodeActivity.this,
                        "保存成功!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    });
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_code, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initDate(){
        tableCodeBean=(TableCodeBean) getIntent().getSerializableExtra("TableCodeBean");

        loadingDialog = DialogUtils.createLoadDialog(UpdateCodeActivity.this, false);
        view_back=(View) findViewById(R.id.view_back);
        et_code=(EditText)findViewById(R.id.et_code);
        tv_code=(TextView) findViewById(R.id.tv_code);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_delete=(TextView) findViewById(R.id.tv_delete);
        tv_save=(TextView) findViewById(R.id.tv_save);
        tv_download=(TextView) findViewById(R.id.tv_download);
        rl_main=(RelativeLayout)findViewById(R.id.rl_main);

        tv_name.setText(tableCodeBean.getShopName());
        et_code.setText(tableCodeBean.getCode()+"");


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = et_code.getText().toString();

                if(!TextUtils.isEmpty(code)){
                    updateCode();
                }
                else{
                    DialogUtils.showAlertDialog(UpdateCodeActivity.this,
                            "餐桌码不能为空！");
                }
            }
        });
        view_back.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_download.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_download:{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                        Bitmap bp = returnBitMap(tableCodeBean.getQrcodeUrl());
                        saveImageToPhotos(UpdateCodeActivity.this, bp);
                    }
                }).start();



                break;
            }
            case R.id.tv_delete:{
                LogUtils.e("删除");
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(UpdateCodeActivity.this,"删除店铺？","",1, UpdateCodeActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                alertDialog.cancel();
                deleteCode();
                break;
            }

        }
    }

    public void download(final String myurl){
        loadingDialog.show();
        cache=new File(Environment.getExternalStorageDirectory(), "QrcodeUrl");
        if(!cache.exists()){
            cache.mkdirs();
        }
        //耗时操作都要放在子线程操作
        //开启子线程获取输入流
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                InputStream is=null;
                try {
                    URL url=new URL(myurl);
                    //开启连接
                    conn=(HttpURLConnection) url.openConnection();
                    //设置连接超时
                    conn.setConnectTimeout(5000);
                    //设置请求方式
                    conn.setRequestMethod("GET");
                    //conn.connect();
                    if(conn.getResponseCode()==200){
                        Bitmap b=BitmapFactory.decodeStream(is);
                        //把输入流转化成bitmap格式，以msg形式发送至主线程
                        Message msg=new Message();
                        msg.obj=b;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    try {
                        //用完记得关闭
                        is.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    Toast.makeText(UpdateCodeActivity.this,"开始保存图片...",Toast.LENGTH_SHORT).show();
                    tv_download.setClickable(false);
                    break;
                case SAVE_SUCCESS:
                    Toast.makeText(UpdateCodeActivity.this,"图片保存成功,请到相册查找",Toast.LENGTH_SHORT).show();
                    tv_download.setClickable(true);
                    break;
                case SAVE_FAILURE:
                    Toast.makeText(UpdateCodeActivity.this,"图片保存失败,请稍后再试...",Toast.LENGTH_SHORT).show();
                    tv_download.setClickable(true);
                    break;
            }
        }
    };


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


    public void updateCode() {
        loadingDialog.show();
        UpdateCodeProtocol updateCodeProtocol = new UpdateCodeProtocol();

        String url = updateCodeProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("code", code);
        params.put("businessShopCodeId",  String.valueOf(tableCodeBean.getBusinessShopCodeId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addOrUpdateLoginResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.code==0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UpdateCodeActivity.this,null,"保存成功");
                    clockPopuwindow.showPopupWindow(rl_main);
                    loadingDialog.dismiss();
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdateCodeActivity.this,
                            addOrUpdateLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateCodeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateCodeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    public void deleteCode() {
        loadingDialog.show();
        DeleteShopProtocol deleteShopProtocol = new DeleteShopProtocol();

        String url = deleteShopProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("businessShopCodeId",  String.valueOf(tableCodeBean.getBusinessShopCodeId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdateCodeActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateCodeActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateCodeActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    private void DownloadImage(String imguri,String imgname)
    {
        loadingDialog.show();
        URL url;
        byte[] b=null;
        try {
            url = new URL(imguri);   //设置URL
            HttpURLConnection con;
            con = (HttpURLConnection)url.openConnection();  //打开连接
            con.setRequestMethod("GET"); //设置请求方法
            //设置连接超时时间为5s
            con.setConnectTimeout(5000);
            InputStream in=con.getInputStream();  //取得字节输入流
            b=readInputStream(in);
            Log.v("Save","getbyte");
            Bitmap bitmap=decodeSampledBitmapFromStream(b,400,400);
            try
            {
                saveJPGFile(bitmap, imgname);
                loadingDialog.dismiss();
                Log.v("Downloadimage","保存图片成功");
            }
            catch(Exception e)
            {
                loadingDialog.dismiss();
                Log.v("getimage","保存图片失败");
            }
        } catch (Exception e) {
            loadingDialog.dismiss();
            e.printStackTrace();
        }
    }

    /**
     *
     * @param bm
     *            图片的bitmap
     * @param fileName
     *            文件名
     *            文件夹名
     * @throws IOException
     */
    private void saveJPGFile(Bitmap bm,String fileName)
            throws IOException {
        String path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
        File dirFile = new File(path);
        img_path=path;
        // 文件夹不存在则创建文件夹
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        Log.v("保存文件函数", "创建文件夹成功");
        File myCaptureFile = new File(path + fileName + ".jpg");
        Log.v("保存文件函数", "文件路径");

        try{
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            Log.v("保存文件函数", "文件流");
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.v("保存文件函数", "保存成功");
            bos.flush();
            bos.close();
            if(bm.isRecycled()==false)
            {
                bm.recycle();
                Log.v("Util","回收bitmap");
            }
        }catch(Exception e){

        }
    }

    private byte[] readInputStream(InputStream in) throws Exception{
        int len=0;
        byte buf[]=new byte[1024];
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        while((len=in.read(buf))!=-1){
            out.write(buf,0,len);  //把数据写入内存
        }
        out.close();  //关闭内存输出流
        return out.toByteArray(); //把内存输出流转换成byte数组
    }

    private Bitmap decodeSampledBitmapFromStream(byte[] b,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(b, 0, b.length, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Log.v("decode","返回bitmap");
        return BitmapFactory.decodeByteArray(b, 0, b.length, options);
    }
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.v("calculate"," "+inSampleSize);
        return inSampleSize;
    }



}
