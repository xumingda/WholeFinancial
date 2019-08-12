package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.tabpager.HistoryReportPager;
import com.ciba.wholefinancial.tabpager.MyReportPager;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MobileUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationReportActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener{
    private List<String> pics = new ArrayList<String>();
    private TextView mTextMessage;
    /**
     * 关注的内容viewpager
     */
    private NoScrollViewPager vpContent;
    //     */
    private List<String> pagerTitles;
    /**
     * 存放商场,品牌,喜欢我，我喜欢页面的集合
     */
    private List<ViewTabBasePager> concernBasePagerList;
    /**
     * 关注标题指示器
     */
    private TabSlidingIndicator titleIndicator;
    private LayoutInflater mInflater;
    private View rootView;
    private List<Bitmap> bitmapList;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private String path;
    private  MyReportPager myReportPager;
    private Dialog loadingDialog;
    private View view_back;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    uploadImage();
                    break;

            }
        }
    };

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_report, null);
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
        loadingDialog = DialogUtils.createLoadDialog(InformationReportActivity.this, false);
        titleIndicator = (TabSlidingIndicator) findViewById(R.id.indicator_concern_title);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp);
        view_back=(View)findViewById(R.id.view_back);
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("我的举报");
        pagerTitles.add("历史举报");

        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        myReportPager = new MyReportPager(this,this);
        HistoryReportPager historyReportPager = new HistoryReportPager(this);
        concernBasePagerList.add(myReportPager);
        concernBasePagerList.add(historyReportPager);

        bitmapList=myReportPager.getBitmapList();
        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_color_black),
                UIUtils.getColor(R.color.errorColor));
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        concernBasePagerList.get(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ConcernInfoPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {

            return pagerTitles.get(position);
        }

        @Override
        public int getCount() {
            return pagerTitles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewTabBasePager concernBasePager = concernBasePagerList
                    .get(position);
            View rootView = concernBasePager.getRootView();
            container.removeView(rootView);
            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + myReportPager.getTimepath() + ".jpg";
            File file = new File(path);
            String img_path = path;
            if (file.exists()) {
                new Thread() {
                    public void run() {
                        try {
                            Bitmap photo = BitmapUtils.getSmallBitmap(path);
                            if (photo != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                addBitmap(new_photo, bitmapList);

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
                        uri = BitmapUtils.getPictureUri(data, InformationReportActivity.this);
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
                                Bitmap photo = BitmapUtils.getSmallBitmap(path);
                                if (photo != null) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                                    addBitmap(new_photo, bitmapList);
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

    //把新的图片加载到最前面
    public void addBitmap(Bitmap bitmap, List<Bitmap> old_bitmap) {
        List<Bitmap> new_bitmap = new ArrayList<>();
        new_bitmap.add(bitmap);
        new_bitmap.addAll(old_bitmap);
        bitmapList.clear();
        bitmapList.addAll(new_bitmap);
        myReportPager.setBitmapList(bitmapList);
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
                    pics.add(imageResponse.getPicUrl());
                    myReportPager.setPics(pics);
                    UIUtils.showToastCenter(InformationReportActivity.this, "上传成功");
                } else {
                    DialogUtils.showAlertDialog(InformationReportActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(InformationReportActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(InformationReportActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }
}
