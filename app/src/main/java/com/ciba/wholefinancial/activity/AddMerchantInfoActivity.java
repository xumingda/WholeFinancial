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
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnCodeListsCallBack;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.datepicker.DateFormatUtils;
import com.ciba.wholefinancial.protocol.GetMerchantInfoProtocol;
import com.ciba.wholefinancial.protocol.SubmitBusinessInfoProtocol;
import com.ciba.wholefinancial.protocol.SubmitReportProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.ciba.wholefinancial.tabpager.AllMerchantPager;
import com.ciba.wholefinancial.tabpager.BusinessStatePager;
import com.ciba.wholefinancial.tabpager.MerchantInfoPager;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.ListDataSave;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MobileUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.ProviderUtil;
import com.ciba.wholefinancial.util.ReadCityCodeAsyncTask;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AddMerchantInfoActivity extends BaseActivity implements View.OnClickListener ,OnSetViewCallBack , OnCodeListsCallBack {
    private ListDataSave listDataSave;
    private LayoutInflater mInflater;
    private View rootView;
    private MerchantBean merchantBean;
    private SalesmanBean salesmanBean;
    private ImageView iv_top_back;
    private Button btn_commit;
    private int businessId;
    private GetMerchantInfoResponse getMerchantInfoResponse;
    private Dialog loadingDialog;
    private Gson gson;
    private EditText et_shop_name;
    private EditText et_address;
    private ImageView iv_business_license;
    private ImageView iv_shop_pic;
    private ImageView iv_shop_in_pic;
    private ImageView iv_shop_in_pic_two;
    private ImageView iv_shop_in_pic_three,iv_specialPic,iv_bank_card_front,iv_bank_card_back;
    private EditText et_manager_name;
    private EditText et_tellphone;
    private EditText et_idCardName;
    private EditText et_idCardNumber,et_merchantshortname;
    private ImageView iv_id_card_front;
    private ImageView iv_id_card_back;
    private EditText et_bank_card;
    private EditText et_bank_user;
    private EditText et_bank,et_accountBank;
    private EditText et_weixinPayRate,et_alipayRate,et_xqRate;
    private EditText et_remark,et_weixinNumber,et_alipayNumber,et_creditCode,et_business_scope;

    private File mFile;
    private String timepath;

    private CheckBox CbBasketball;
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private List<Bitmap> bitmapList;
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private String path;
    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private LinearLayout ll_main;
    //0营业执照，1门头照，2店内照1，3店内照2,4店内照3,5身份证前,6身份证后,7特殊照片
    private int pic_type;
    private String servicePhone;
    private String licensePic;
    private String doorOutPic;
    private String managerIdcardPic1;
    private String managerIdcardPic2;
    private String businessName;
    private String address;
    private String managerName;
    private String managerPhoneNumber;
    private String merchantShortname;
    private String bankNumber;
    private String bankUserName;
    private String bankName;
    private String accountBank;
    private String productDesc;
    private String weixinPayRate,alipayRate,xqRate;
    private String remark;
    private String idCardName;
    private String idCardNumber;
    private String alipayNumber;
    private String weixinNumber;
    private String businessScope;
    private String creditCode;
    private String specialPic,bankPic1,bankPic2;
    private List<String> timeList;


    private List<String> doorInPics;
    private HashMap<Integer ,String>  doorInPicsHash;
    private TextView et_storeAddressCode,et_productDesc,tv_endTime,tv_startTime,et_bankAddressCode,et_servicePhone;
    private CustomDatePicker  mTimerPicker,mEndTimerPicker;
    private String cityCode,bankCode,indoorPic;
    private SelectShopPopuwindow selectLevelPopuwindow;
    private List<String> list;
    private Bitmap new_photo;
    private ImageLoader imageLoader;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.add_merchant_pager, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        listDataSave=new ListDataSave(this,"whole");
        boolean getCode=SharedPrefrenceUtils.getBoolean(this,"getCode",false);
        if(!getCode){
            loadingDialog.show();
            ReadCityCodeAsyncTask readCityCodeAsyncTask= new ReadCityCodeAsyncTask();
            readCityCodeAsyncTask.execute();
            readCityCodeAsyncTask.setOnCodeListsCallBack(this);
        }
        salesmanBean= SPUtils.getBeanFromSp(AddMerchantInfoActivity.this,"salesmanObject","SalesmanBean");
        merchantBean= SPUtils.getBeanFromSp(AddMerchantInfoActivity.this,"businessObject","MerchantBean");
        doorInPicsHash=new HashMap<>();
        doorInPics=new ArrayList<>();
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
                            if (ContextCompat.checkSelfPermission(AddMerchantInfoActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(AddMerchantInfoActivity.this,
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
                                    uri = FileProvider.getUriForFile(AddMerchantInfoActivity.this, ProviderUtil.getFileProviderName(AddMerchantInfoActivity.this), mFile);
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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        businessId=getIntent().getIntExtra("businessId",0);
        et_bankAddressCode=(TextView) findViewById(R.id.et_bankAddressCode);
        CbBasketball=(CheckBox)findViewById(R.id.CbBasketball);
        tv_startTime=(TextView) findViewById(R.id.tv_startTime);
        tv_endTime=(TextView) findViewById(R.id.tv_endTime);
        et_productDesc=(TextView)findViewById(R.id.et_productDesc);
        et_storeAddressCode=(TextView)findViewById(R.id.et_storeAddressCode);
        btn_commit=(Button) findViewById(R.id.btn_commit);
        ll_main=(LinearLayout) findViewById(R.id.ll_main);
        et_remark=(EditText)findViewById(R.id.et_remark);
        iv_top_back=(ImageView)findViewById(R.id.iv_top_back);
        et_shop_name=(EditText)findViewById(R.id.et_shop_name);
        et_address=(EditText)findViewById(R.id.et_address);
        iv_specialPic=(ImageView)findViewById(R.id.iv_specialPic);
        iv_business_license=(ImageView)findViewById(R.id.iv_business_license);
        iv_shop_pic=(ImageView)findViewById(R.id.iv_shop_pic);
        iv_shop_in_pic=(ImageView)findViewById(R.id.iv_shop_in_pic);
        iv_shop_in_pic_two=(ImageView)findViewById(R.id.iv_shop_in_pic_two);
        iv_shop_in_pic_three=(ImageView)findViewById(R.id.iv_shop_in_pic_three);
        iv_id_card_front=(ImageView)findViewById(R.id.iv_id_card_front);
        iv_bank_card_front=(ImageView)findViewById(R.id.iv_bank_card_front);
        iv_bank_card_back=(ImageView)findViewById(R.id.iv_bank_card_back);
        et_weixinNumber=(EditText)findViewById(R.id.et_weixinNumber);
        et_alipayNumber=(EditText)findViewById(R.id.et_alipayNumber);
        et_manager_name=(EditText)findViewById(R.id.et_manager_name);
        et_tellphone=(EditText)findViewById(R.id.et_tellphone);
        et_idCardName=(EditText)findViewById(R.id.et_idCardName);
        et_idCardNumber=(EditText)findViewById(R.id.et_idCardNumber);
        et_merchantshortname=(EditText) findViewById(R.id.et_merchantshortname);
        et_bank_card=(EditText)findViewById(R.id.et_bank_card);
        et_bank_user=(EditText)findViewById(R.id.et_bank_user);
        et_accountBank=(EditText)findViewById(R.id.et_accountBank);
        et_bank=(EditText)findViewById(R.id.et_bank);
        et_servicePhone=(EditText)findViewById(R.id.et_servicePhone);
        et_weixinPayRate=(EditText)findViewById(R.id.et_weixinPayRate);
        et_alipayRate=(EditText)findViewById(R.id.et_alipayRate);
        et_xqRate=(EditText)findViewById(R.id.et_xqRate);
        et_creditCode=(EditText)findViewById(R.id.et_creditCode);
        et_business_scope=(EditText)findViewById(R.id.et_business_scope);
        iv_id_card_back=(ImageView)findViewById(R.id.iv_id_card_back);

        iv_id_card_back.setOnClickListener(this);
        iv_top_back.setOnClickListener(this);
        iv_specialPic.setOnClickListener(this);
        iv_business_license.setOnClickListener(this);
        iv_shop_pic.setOnClickListener(this);
        iv_shop_in_pic.setOnClickListener(this);
        iv_shop_in_pic_two.setOnClickListener(this);
        iv_shop_in_pic_three.setOnClickListener(this);
        iv_id_card_front.setOnClickListener(this);
        iv_bank_card_front.setOnClickListener(this);
        iv_bank_card_back.setOnClickListener(this);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        et_bankAddressCode.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        et_storeAddressCode.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        timeList=new ArrayList<>();
        list=new ArrayList<>();
        list.add("餐饮");
        list.add("线下零售");
        list.add("居民生活服务");
        list.add("休闲娱乐");
        list.add("交通出行");
        list.add("其他");
        selectLevelPopuwindow = new SelectShopPopuwindow(this,"服务描述",list,itemsOnClick);
        et_productDesc.setOnClickListener(this);
        initTimerPicker();
        initEndTimerPicker();

        if(merchantBean!=null){
//            et_creditCode.setText(merchantBean.getCreditCode());
//            et_business_scope.setText(merchantBean.getBusinessScope());
//            et_servicePhone.setText(merchantBean.getServicePhone());//客服电话
//            et_idCardName.setText(merchantBean.getIdCardName());//身份证名
//            et_idCardNumber.setText(merchantBean.getIdCardNumber());//身份号
//            et_merchantshortname.setText(merchantBean.getMerchantShortname());//简称
//            et_shop_name.setText(merchantBean.getStoreName());//	商家名称，必填
//            et_address.setText(merchantBean.getStoreStreet());//	商家地址，必填
//            et_manager_name.setText(merchantBean.getContact());//	经营者姓名，必填
//            et_tellphone.setText(merchantBean.getContactPhone());//	经营者手机号码，必填
//            et_bank_card.setText(merchantBean.getAccountNumber());//	银行卡号，必填
//            et_bank_user.setText(merchantBean.getAccountName());	//开户人，必填
//            et_bank.setText(merchantBean.getBankName());	//	开户行全称，必填
//            et_accountBank.setText(merchantBean.getAccountBank());//开户行，必填
//            et_weixinPayRate.setText(merchantBean.getWeixinPayRate());//		结算费率，必填
//            et_alipayRate.setText(merchantBean.getAlipayRate());
//            et_xqRate.setText(merchantBean.getXqRate());
//            et_remark.setText(merchantBean.getRemark());//		备注
//            et_alipayNumber.setText(merchantBean.getAlipayNumber());
//            et_weixinNumber.setText(merchantBean.getWeixinNumber());
//            et_storeAddressCode.setText(merchantBean.getStoreAddressCode());
//            et_productDesc.setText(merchantBean.getProductDesc());
//            et_bankAddressCode.setText(merchantBean.getBankAddressCode());
//            tv_startTime.setText(merchantBean.getIdCardValidStartTime());
//            if(!TextUtils.isEmpty(merchantBean.getIdCardValidEndTime())){
//                if(merchantBean.getIdCardValidEndTime().indexOf("长期")!=-1){
//                    CbBasketball.setChecked(true);
//                    tv_endTime.setText("2060-12-12");
//                }else{
//                    tv_endTime.setText(merchantBean.getIdCardValidEndTime());
//                }
//            }
//            imageLoader.displayImage(merchantBean.getLicensePic(), iv_business_license, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getStoreEntrancePic(), iv_shop_pic, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getIndoorPic(), iv_shop_in_pic, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getIdCardCopy(), iv_id_card_front, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getIdCardNational(), iv_id_card_back, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getSpecialPic(), iv_specialPic, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getBankPic1(), iv_bank_card_front, PictureOption.getSimpleOptions());
//            imageLoader.displayImage(merchantBean.getBankPic2(), iv_bank_card_back, PictureOption.getSimpleOptions());


           setDate();
        }

        if(businessId>0){
            getMerchantInfo();
        }
    }
    private void initTimerPicker() {
        String beginTime ="2000-01-01 00:00";
        String endTime = "2070-12-31 18:00";

        tv_startTime.setText(beginTime.substring(0,10));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_startTime.setText(DateFormatUtils.long2Str(timestamp, true).substring(0,10));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    private void initEndTimerPicker() {
        String beginTime ="2000-01-01 00:00";
        String endTime = "2070-12-31 18:00";

        tv_endTime.setText(beginTime.substring(0,10));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mEndTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_endTime.setText(DateFormatUtils.long2Str(timestamp, true).substring(0,10));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mEndTimerPicker.setCancelable(true);
        // 显示时和分
        mEndTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mEndTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mEndTimerPicker.setCanShowAnim(true);
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
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            productDesc = list.get(selectLevelPopuwindow.getText());
            et_productDesc.setText(productDesc);
            selectLevelPopuwindow.dismissPopupWindow();

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==50&&resultCode==100){
            cityCode=data.getStringExtra("cityCode").substring(0,6);
            String cityName=data.getStringExtra("cityName");
            et_storeAddressCode.setText(cityCode);

        }else if(requestCode==51&&resultCode==100){
            bankCode=data.getStringExtra("cityCode").substring(0,6);
            String cityName=data.getStringExtra("cityName");
            et_bankAddressCode.setText(bankCode);

        }
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
                if(uri==null){
                    geturi(getIntent());
                }
                // 获取包含所需数据的Cursor对象
                @SuppressWarnings("deprecation")
                Cursor cursor = null;
                try {
                    cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor == null) {
                        uri = BitmapUtils.getPictureUri(data, AddMerchantInfoActivity.this);
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

    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_startTime:{
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(tv_startTime.getText().toString());
                break;
            }
            case R.id.tv_endTime:{
                // 日期格式为yyyy-MM-dd HH:mm
                mEndTimerPicker.show(tv_endTime.getText().toString());
                break;
            }
            case R.id.et_bankAddressCode:{
                Intent intent=new Intent(this,SearchCityCodeActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,51);
                break;
            }
            case R.id.et_productDesc:{
                selectLevelPopuwindow.showPopupWindow(ll_main);
                break;
            }
            case R.id.btn_commit:{
                timeList=new ArrayList<>();
                if(CbBasketball.isChecked()){
                    timeList.add(tv_startTime.getText().toString());
                    timeList.add("长期");
                }else {
                    timeList.add(tv_startTime.getText().toString());
                    timeList.add(tv_endTime.getText().toString());
                }
                creditCode=et_creditCode.getText().toString();
                businessScope=et_business_scope.getText().toString();
                servicePhone=et_servicePhone.getText().toString();//客服电话
                idCardName=et_idCardName.getText().toString();//身份证名
                idCardNumber=et_idCardNumber.getText().toString();//身份号
                merchantShortname=et_merchantshortname.getText().toString();//简称
                businessName=et_shop_name.getText().toString();//	商家名称，必填
                address=et_address.getText().toString();//	商家地址，必填
                managerName=et_manager_name.getText().toString();//	经营者姓名，必填
                managerPhoneNumber=et_tellphone.getText().toString();//	经营者手机号码，必填
                bankNumber=et_bank_card.getText().toString();//	银行卡号，必填
                bankUserName=et_bank_user.getText().toString();	//开户人，必填
                bankName=et_bank.getText().toString();	//	开户行全称，必填
                accountBank=et_accountBank.getText().toString();//开户行，必填
                weixinPayRate=et_weixinPayRate.getText().toString();//		结算费率，必填
                alipayRate=et_alipayRate.getText().toString();
                xqRate=et_xqRate.getText().toString();
                remark=et_remark.getText().toString();//		备注
                alipayNumber=et_alipayNumber.getText().toString();
                weixinNumber=et_weixinNumber.getText().toString();


                if (!TextUtils.isEmpty(alipayNumber) &&!TextUtils.isEmpty(weixinNumber) &&!TextUtils.isEmpty(xqRate) &&!TextUtils.isEmpty(alipayRate) &&!TextUtils.isEmpty(productDesc) &&!TextUtils.isEmpty(servicePhone) &&!TextUtils.isEmpty(merchantShortname) &&!TextUtils.isEmpty(indoorPic) &&!TextUtils.isEmpty(cityCode) &&!TextUtils.isEmpty(bankCode) &&!TextUtils.isEmpty(accountBank) &&timeList.size()==2&&!TextUtils.isEmpty(idCardNumber)&&!TextUtils.isEmpty(idCardName)&&!TextUtils.isEmpty(creditCode)&&!TextUtils.isEmpty(businessScope)&&!TextUtils.isEmpty(specialPic)&&!TextUtils.isEmpty(bankPic1)&&!TextUtils.isEmpty(bankPic2) && !TextUtils.isEmpty(businessName) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(managerName) && !TextUtils.isEmpty(managerPhoneNumber) && !TextUtils.isEmpty(bankNumber) && !TextUtils.isEmpty(bankUserName) && !TextUtils.isEmpty(bankName)
                        && !TextUtils.isEmpty(weixinPayRate) && !TextUtils.isEmpty(licensePic) && (!TextUtils.isEmpty(doorOutPic) ||doorInPics.size() > 0 )&& !TextUtils.isEmpty(managerIdcardPic1) && !TextUtils.isEmpty(managerIdcardPic2)) {
                    submitBusinessInfo();
                } else {
                    DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                            "请把信息填写完整，再提交！");
                }
                break;
            }
            case R.id.et_storeAddressCode:{
                Intent intent=new Intent(this,SearchCityCodeActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,50);
                break;
            }
            case R.id.iv_top_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.iv_shop_pic:{
                pic_type=1;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_specialPic:{
                pic_type=7;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_business_license:{
                pic_type=0;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic:{
                pic_type=2;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_two:{
                pic_type=3;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_shop_in_pic_three:{
                pic_type=4;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_id_card_front:{
                pic_type=5;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_id_card_back:{
                pic_type=6;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_bank_card_front:{
                pic_type=8;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.iv_bank_card_back:{
                pic_type=9;
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }

        }
    }

    private void addBitmap(Bitmap bitmap){

        switch (pic_type){
            case 0: {
                iv_business_license.setImageBitmap(bitmap);
                break;
            } case 1: {
                iv_shop_pic.setImageBitmap(bitmap);
                break;
            }case 2: {
                iv_shop_in_pic.setImageBitmap(bitmap);
                break;
            }case 3: {
                iv_shop_in_pic_two.setImageBitmap(bitmap);
                break;
            }
            case 4: {
                iv_shop_in_pic_three.setImageBitmap(bitmap);
                break;
            }case 5: {
                iv_id_card_front.setImageBitmap(bitmap);
                break;
            }case 6: {
                iv_id_card_back.setImageBitmap(bitmap);
                break;
            }case 7: {
                iv_specialPic.setImageBitmap(bitmap);
                break;
            }
            case 8:{
                iv_bank_card_front.setImageBitmap(bitmap);
                break;
            }
            case 9:{
                iv_bank_card_back.setImageBitmap(bitmap);
                break;
            }
        }
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

                LogUtils.e("baseResponsexmd:" + json.toString());
                if (imageResponse.code == 0) {
                    LogUtils.e("baseResponsexmd:" + pic_type);
                    switch (pic_type){

                        case 0: {
                            licensePic=imageResponse.picUrl;
                            break;
                        } case 1: {
                            doorOutPic=imageResponse.picUrl;
                            break;
                        }case 2: {
                            indoorPic=imageResponse.picUrl;
                            doorInPics.add(0,imageResponse.picUrl);
                            break;
                        }case 3: {
                            doorInPics.add(1,imageResponse.picUrl);
                            break;
                        }
                        case 4: {
                            doorInPics.add(2,imageResponse.picUrl);
                            break;
                        }case 5: {
                            managerIdcardPic1=imageResponse.picUrl;
                            break;
                        }case 6: {
                            managerIdcardPic2=imageResponse.picUrl;
                            break;
                        }case 7: {
                            specialPic=imageResponse.picUrl;
                            break;
                        }
                        case 8: {
                            bankPic1=imageResponse.picUrl;
                            break;
                        }
                        case 9: {
                            bankPic2=imageResponse.picUrl;
                            break;
                        }
                    }
                    UIUtils.showToastCenter(AddMerchantInfoActivity.this, "上传成功");
                } else {
                    DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                            imageResponse.msg);

                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });

    }

    //提交
    private void submitBusinessInfo() {
        loadingDialog.show();
        SubmitBusinessInfoProtocol submitBusinessInfoProtocol=new SubmitBusinessInfoProtocol();
        String url = Constants.SERVER_URL +submitBusinessInfoProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        Gson gson = new Gson();
        params.put("bankPic2", bankPic2);
        params.put("bankPic1", bankPic1);
        params.put("specialPic", specialPic);
        params.put("authorization",SharedPrefrenceUtils.getString(AddMerchantInfoActivity.this,"token"));
        if(businessId==0){
            if(salesmanBean!=null) {
                params.put("salesmanId", String.valueOf(salesmanBean.getId()));
            }else{
                params.put("businessId", String.valueOf(merchantBean.getId()));
            }
        }else{
            params.put("businessId", String.valueOf(businessId));
        }

        params.put("businessScope", businessScope);
        params.put("creditCode", creditCode);
        params.put("idCardCopy", managerIdcardPic1);
        params.put("idCardNational", managerIdcardPic2);
        params.put("idCardName", idCardName);
        params.put("idCardNumber", idCardNumber);
        params.put("idCardValidStartTime",  timeList.get(0));
        params.put("idCardValidEndTime",   timeList.get(1));
        params.put("accountName", bankUserName);
        params.put("accountBank", accountBank);
        params.put("bankName", bankName);
        params.put("bankAddressCode",bankCode);
        params.put("accountNumber", bankNumber);
        params.put("storeName", businessName);
        params.put("storeStreet", address);
        params.put("storeEntrancePic", doorOutPic);
        params.put("storeAddressCode",cityCode);
        params.put("indoorPic",indoorPic);
        params.put("merchantShortname", merchantShortname);
        params.put("licensePic", licensePic);
        params.put("servicePhone", servicePhone);
        params.put("productDesc",productDesc);
        params.put("weixinPayRate", weixinPayRate);
        params.put("alipayRate", alipayRate);
        params.put("xqRate", xqRate);
        params.put("contact", managerName);
        params.put("contactPhone", managerPhoneNumber);
        params.put("remark", remark);
        params.put("weixinNumber", weixinNumber);
        params.put("alipayNumber", alipayNumber);
        LogUtils.e("SubmitBusinessInfoProtocol:"+params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("submitReportResponse:" + json.toString());
                SubmitReportResponse submitReportResponse = gson.fromJson(json, SubmitReportResponse.class);

                if (submitReportResponse.code == 0) {
                    if(salesmanBean!=null) {
                        salesmanBean.setBusinessCount(salesmanBean.getBusinessCount() + 1);
                        SPUtils.saveBean2Sp(AddMerchantInfoActivity.this, salesmanBean, "salesmanObject", "SalesmanBean");
                    }
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UIUtils.getActivity(),AddMerchantInfoActivity.this,"商户登记成功");
                    clockPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                            submitReportResponse.msg);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMerchantInfoActivity.this,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddMerchantInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    @Override
    public void CallBackSuccess(int type) {
        if(salesmanBean!=null) {
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
        }else{
            Intent intent=new Intent(this, LoginActivity.class);
            UIUtils.startActivityNextAnim(intent);
            finish();
        }
    }

    @Override
    public void CallBackDate(List<TypeBean> typeBeanList) {

    }

    public void setDate() {
        if(businessId>0) {
            MerchantBean merchantBean = getMerchantInfoResponse.getData();
        }
        et_creditCode.setText(merchantBean.getCreditCode());
        et_business_scope.setText(merchantBean.getBusinessScope());
        et_servicePhone.setText(merchantBean.getServicePhone());//客服电话
        et_idCardName.setText(merchantBean.getIdCardName());//身份证名
        et_idCardNumber.setText(merchantBean.getIdCardNumber());//身份号
        et_merchantshortname.setText(merchantBean.getMerchantShortname());//简称
        et_shop_name.setText(merchantBean.getStoreName());//	商家名称，必填
        et_address.setText(merchantBean.getStoreStreet());//	商家地址，必填
        et_manager_name.setText(merchantBean.getContact());//	经营者姓名，必填
        et_tellphone.setText(merchantBean.getContactPhone());//	经营者手机号码，必填
        et_bank_card.setText(merchantBean.getAccountNumber());//	银行卡号，必填
        et_bank_user.setText(merchantBean.getAccountName());	//开户人，必填
        et_bank.setText(merchantBean.getBankName());	//	开户行全称，必填
        et_accountBank.setText(merchantBean.getAccountBank());//开户行，必填
        et_weixinPayRate.setText(merchantBean.getWeixinPayRate());//		结算费率，必填
        et_alipayRate.setText(merchantBean.getAlipayRate());
        et_xqRate.setText(merchantBean.getXqRate());
        et_remark.setText(merchantBean.getRemark());//		备注
        et_alipayNumber.setText(merchantBean.getAlipayNumber());
        et_weixinNumber.setText(merchantBean.getWeixinNumber());
        et_storeAddressCode.setText(merchantBean.getStoreAddressCode());
        et_productDesc.setText(merchantBean.getProductDesc());
        et_bankAddressCode.setText(merchantBean.getBankAddressCode());
        if(!TextUtils.isEmpty(merchantBean.getIdCardValidStartTime())){
            tv_startTime.setText(merchantBean.getIdCardValidStartTime());
        }
        if(!TextUtils.isEmpty(merchantBean.getIdCardValidEndTime())) {
            if (merchantBean.getIdCardValidEndTime().indexOf("长期") != -1) {
                CbBasketball.setChecked(true);
                tv_endTime.setText("2060-12-12");
            } else {
                tv_endTime.setText(merchantBean.getIdCardValidEndTime());
            }
        }
        imageLoader.displayImage(merchantBean.getLicensePic(), iv_business_license, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getStoreEntrancePic(), iv_shop_pic, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIdCardCopy(), iv_id_card_front, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIdCardNational(), iv_id_card_back, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getBankPic1(), iv_bank_card_front, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getBankPic2(), iv_bank_card_back, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIndoorPic(), iv_shop_in_pic, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getSpecialPic(), iv_specialPic, PictureOption.getSimpleOptions());
        businessName=merchantBean.getStoreName();
        address=merchantBean.getStoreStreet();
        managerName=merchantBean.getContact();
        managerPhoneNumber=merchantBean.getContactPhone();
        bankNumber=merchantBean.getAccountNumber();
        bankUserName=merchantBean.getAccountName();
        bankName=merchantBean.getBankName();
        weixinPayRate=merchantBean.getWeixinPayRate();
        licensePic=merchantBean.getLicensePic();
        doorOutPic=merchantBean.getStoreEntrancePic();
        managerIdcardPic1=merchantBean.idCardCopy;
        managerIdcardPic2=merchantBean.getIdCardNational();
        bankPic2=merchantBean.getBankPic2();
        bankPic1=merchantBean.getBankPic1();
        specialPic=merchantBean.getSpecialPic();
        businessScope=merchantBean.getBusinessScope();
        creditCode=merchantBean.getCreditCode();
        idCardName=merchantBean.getIdCardName();
        idCardNumber=merchantBean.getIdCardNumber();
        if(merchantBean.getIdCardValidStartTime()!=null){
            timeList.add(merchantBean.getIdCardValidStartTime());
        }
        if(merchantBean.getIdCardValidEndTime()!=null){
            timeList.add(merchantBean.getIdCardValidEndTime());
        }
        bankCode=merchantBean.getBankAddressCode();
        cityCode=merchantBean.getCreditCode();
        indoorPic=merchantBean.getIndoorPic();
        merchantShortname=merchantBean.getMerchantShortname();
        servicePhone=merchantBean.getServicePhone();
        productDesc=merchantBean.getProductDesc();
        alipayRate=merchantBean.getAlipayRate();
        xqRate=merchantBean.getXqRate();
        remark=merchantBean.getRemark();
        weixinNumber=merchantBean.getWeixinNumber();
        alipayNumber=merchantBean.getAlipayNumber();


    }
    private void getMerchantInfo() {
        loadingDialog.show();
        GetMerchantInfoProtocol getMerchantInfoProtocol = new GetMerchantInfoProtocol();
        String url = getMerchantInfoProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(businessId));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getMerchantInfoResponse = gson.fromJson(json, GetMerchantInfoResponse.class);
                LogUtils.e("getMerchantInfoResponse:" + getMerchantInfoResponse.toString());
                if (getMerchantInfoResponse.getCode() == 0) {
                    setDate();
                } else {
                    DialogUtils.showAlertDialog(AddMerchantInfoActivity.this, getMerchantInfoResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMerchantInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    @Override
    public void CallBackCityCodeList(List<CityCodeBean> list) {
        SharedPrefrenceUtils.setBoolean(AddMerchantInfoActivity.this,"getCode",true);
        List<CityCodeBean> cityCodeBeanList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(i!=0){
                cityCodeBeanList.add(list.get(i));
            }
        }
        listDataSave.setDataList("cityCodeBeanList",cityCodeBeanList);
        loadingDialog.dismiss();
    }
}
