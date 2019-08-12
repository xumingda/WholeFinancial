package com.ciba.wholefinancial.tabpager;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.AddMerchantInfoActivity;
import com.ciba.wholefinancial.activity.InformationReportActivity;
import com.ciba.wholefinancial.activity.UpdateBusinessLoginActivity;
import com.ciba.wholefinancial.adapter.AddImageAdapter;
import com.ciba.wholefinancial.adapter.AddImageNewAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.callback.OnShowCallBack;
import com.ciba.wholefinancial.protocol.SubmitReportProtocol;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MobileUtils;
import com.ciba.wholefinancial.util.ProviderUtil;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @描述: 最热门的品牌页面
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class MyReportPager extends ViewTabBasePager implements OnShowCallBack {
    private List<Bitmap> mybitList;
    private Button btn_commit;
    private AddImageNewAdapter addImageAdapter;
    private GridView gv;
    private  View v_add;
    private LinearLayout ll_main;
    private EditText et_report_theme;
    private EditText et_report_content;

    /**
     * 照相选择界面
     */
    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private File mFile;
    private String timepath;
    private List<String> pics = new ArrayList<String>();
    /**
     * 照片参数
     */
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private Activity activity;
    private String theme;
    private String content;
    private Dialog loadingDialog;
    private SalesmanBean salesmanBean;
    private MerchantBean merchantBean;
    public String getTimepath() {
        return timepath;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public MyReportPager(Context context, Activity activity) {
		super(context);
        this.activity=activity;
	}

    public List<Bitmap> getBitmapList() {
        return mybitList;
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (addImageAdapter == null) {
                        addImageAdapter = new AddImageNewAdapter(mContext, mybitList,MyReportPager.this,pics);
                        gv.setAdapter(addImageAdapter);
                    }else {
                        addImageAdapter.setDate(mybitList,pics);
                    }
                    break;

            }
        }
    };
    public void setBitmapList(List<Bitmap> bitmapList) {
        this.mybitList = bitmapList;
        LogUtils.e("mybitList:"+mybitList.size());
        handler.sendEmptyMessage(0);
    }

    @Override
	protected View initView() {
		View view = View.inflate(mContext,
				R.layout.report_pager, null);
        btn_commit=(Button)view.findViewById(R.id.btn_commit);
        gv=(GridView)view.findViewById(R.id.gv);
        v_add=(View)view.findViewById(R.id.v_add);
        et_report_theme=(EditText)view.findViewById(R.id.et_report_theme);
        et_report_content=(EditText)view.findViewById(R.id.et_report_content);
        ll_main=(LinearLayout)view.findViewById(R.id.ll_main);
        initData();
		return view;
	}

	@Override
	public void initData() {
        salesmanBean= SPUtils.getBeanFromSp(mContext,"salesmanObject","SalesmanBean");
        merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
        mybitList=new ArrayList<>();
        mybitList.add(null);
        btn_commit.getBackground().setAlpha(87);

        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
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
                            if (ContextCompat.checkSelfPermission(mContext,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(activity,
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
                                    uri = FileProvider.getUriForFile(mContext, ProviderUtil.getFileProviderName(mContext), mFile);
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
        v_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pWindow.setAnimationStyle(R.style.AnimBottom);
                pWindow.showAtLocation(ll_main,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme=et_report_theme.getText().toString();
                content=et_report_content.getText().toString();
                if(!TextUtils.isEmpty(theme)&&!TextUtils.isEmpty(content)){

                    submitReport();
                }
            }
        });
        if (addImageAdapter == null) {
            addImageAdapter = new AddImageNewAdapter(mContext, mybitList,MyReportPager.this,pics);
        }
        gv.setAdapter(addImageAdapter);

        et_report_theme.addTextChangedListener(new TextWatcher() {
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

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(temp)) {
                    btn_commit.getBackground().setAlpha(255);
                }

            }
        });

        et_report_content.addTextChangedListener(new TextWatcher() {
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

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(temp)) {
                    btn_commit.getBackground().setAlpha(255);
                }

            }
        });
	}

    //提交
    private void submitReport() {
        loadingDialog.show();
        SubmitReportProtocol submitReportProtocol=new SubmitReportProtocol();
        String url = Constants.SERVER_URL +submitReportProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        Gson gson = new Gson();
        if(salesmanBean!=null){
            params.put("type", "0");
            params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        }else{
            params.put("type", "1");
            params.put("businessId",  String.valueOf(merchantBean.getId()));
        }

        params.put("authorization",SharedPrefrenceUtils.getString(mContext,"token"));

        params.put("title", theme);
        params.put("content", content);
        params.put("pics", gson.toJson(pics));

        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                LogUtils.e("submitReportResponse:" + json.toString());
                SubmitReportResponse submitReportResponse = gson.fromJson(json, SubmitReportResponse.class);

                if (submitReportResponse.code == 0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UIUtils.getActivity(),null,"举报成功");
                    clockPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(mContext,
                            submitReportResponse.msg);
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext,
                        error);
            }
            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(mContext,
                        "登录超时，请重新登录！");
            }
        });
    }

    @Override
    public void show() {
        pWindow.setAnimationStyle(R.style.AnimBottom);
        pWindow.showAtLocation(ll_main,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


}