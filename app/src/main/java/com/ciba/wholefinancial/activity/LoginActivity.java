package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnCodeListsCallBack;
import com.ciba.wholefinancial.jspush.ExampleUtil;
import com.ciba.wholefinancial.jspush.TagAliasOperatorHelper;
import com.ciba.wholefinancial.protocol.GetCodeProtocol;
import com.ciba.wholefinancial.protocol.GetVersionProtocol;
import com.ciba.wholefinancial.protocol.LoginProtocol;
import com.ciba.wholefinancial.request.GetCodeRequest;
import com.ciba.wholefinancial.request.LoginRequest;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.GetVersionResponse;
import com.ciba.wholefinancial.response.LoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.ListDataSave;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MD5Utils;
import com.ciba.wholefinancial.util.ReadCityCodeAsyncTask;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.util.FileDownloadListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.ACTION_SET;
import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.sequence;


public class LoginActivity extends BaseActivity implements OnCodeListsCallBack {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 清除登录手机号码
     */
    private RelativeLayout rl_clear, rl_clear_pwd;
    /**
     * 手机输入框
     */
    private EditText et_login_phone, et_pwd;
    private TextView tv_findpwd;
    private Dialog loadingDialog;
    private int time = 60;
    private String userphone;
    private String pwd;
    private Button loginButton;
    private String versionName;
    private String downloadUrl;
    public static boolean isForeground = false;
    private ListDataSave listDataSave;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.login_layout, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    //for receive customer msg from jpush server
    public static final String MESSAGE_RECEIVED_ACTION = "com.ciba.wholefinancial.wholefinancial.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    /**
     * 设置标签与别名
     */
    private void setTagAndAlias(String alias) {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了*/
        //上下文、别名【Sting行】、标签【Set型】、回调
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.alias = alias;
        sequence++;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
    }



    private void initDate() {
        listDataSave=new ListDataSave(this,"whole");
        boolean getCode=SharedPrefrenceUtils.getBoolean(this,"getCode",false);
        if(!getCode){
            ReadCityCodeAsyncTask readCityCodeAsyncTask= new ReadCityCodeAsyncTask();
            readCityCodeAsyncTask.execute();
            readCityCodeAsyncTask.setOnCodeListsCallBack(this);
        }
        loadingDialog = DialogUtils.createLoadDialog(LoginActivity.this, false);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        et_login_phone = (EditText) rootView.findViewById(R.id.et_login_phone);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        rl_clear = (RelativeLayout) rootView.findViewById(R.id.rl_clear);
        rl_clear_pwd = (RelativeLayout) rootView.findViewById(R.id.rl_clear_pwd);
        tv_findpwd = (TextView) rootView.findViewById(R.id.tv_findpwd);
        tv_findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UpdatePwdActivity.class);
                intent.putExtra("title", "重置密码");
                UIUtils.startActivityForResult(intent, 100);
            }
        });
        // 清除登录手机号码的图片监听事件
        rl_clear.setVisibility(View.INVISIBLE);
        rl_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_login_phone.setText("");
            }
        });
        rl_clear_pwd.setVisibility(View.INVISIBLE);
        rl_clear_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_pwd.setText("");
            }
        });

        // 登录手机号码编辑框清除事件
        et_login_phone.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(temp)) {
                    rl_clear.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear.setVisibility(View.VISIBLE);
                }

            }
        });

        // 当手机号码编辑框输入完毕后，跳到登录密码编辑框时，手机号码编辑框右边的关闭图片消失；当输入手机号码，手机号码编辑框右边的关闭图片显示
        et_login_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_login_phone.getText().toString()))
                        rl_clear.setVisibility(View.VISIBLE);
                }

            }
        });


        et_pwd.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(temp)) {
                    rl_clear_pwd.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear_pwd.setVisibility(View.VISIBLE);
                }

            }
        });

        // 当手机号码编辑框输入完毕后，跳到登录密码编辑框时，手机号码编辑框右边的关闭图片消失；当输入手机号码，手机号码编辑框右边的关闭图片显示
        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear_pwd.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_login_phone.getText().toString()))
                        rl_clear_pwd.setVisibility(View.VISIBLE);
                }

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userphone = et_login_phone.getText().toString();
                pwd = et_pwd.getText().toString();
                if (!TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(pwd)
                ) {
                    loginButton.setClickable(false);
                    runlogin();
                } else {
                    if (TextUtils.isEmpty(userphone)) {
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "手机不能为空！");
                    } else {
                        DialogUtils.showAlertDialog(LoginActivity.this,
                                "密码不能为空！");
                    }
                }
            }
        });
        versionName = getAppVersionName(this);
        getVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //获取验证码
//    public void runGetGode() {
//        loadingDialog.show();
//        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
//        GetCodeRequest getCodeRequest = new GetCodeRequest();
//        String url = getCodeProtocol.getApiFun();
//        getCodeRequest.map.put("phoneNumber", userphone);
//        getCodeRequest.map.put("type", "1");
//        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
//            @Override
//            public void dealWithJson(String address, String json) {
//
//                Gson gson = new Gson();
//                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
//                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
//                if (getCodeResponse.code == 0) {
//
//                    loadingDialog.dismiss();
////                    auth_code_id = getCodeResponse.auth_code_id;
//                    Countdowmtimer(60000);
//                } else {
//                    tv_get_code.setClickable(true);
//                    loadingDialog.dismiss();
//                    DialogUtils.showAlertDialog(LoginActivity.this,
//                            getCodeResponse.msg);
//                }
//
//
//            }
//
//            @Override
//            public void dealWithError(String address, String error) {
//                tv_get_code.setClickable(true);
//                loadingDialog.dismiss();
//                DialogUtils.showAlertDialog(LoginActivity.this, error);
//            }
//
//            @Override
//            public void dealTokenOverdue() {
//
//            }
//        });
//    }

    /**
     * 计时器
     */
//    public void Countdowmtimer(long dodate) {
//        new CountDownTimer(dodate, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                time = time - 1;
//                tv_get_code.setText(time + "s后重发");
//            }
//
//            @Override
//            // 计时结束
//            public void onFinish() {
//                time = 60;
//                tv_get_code.setText("获取验证码");
//                tv_get_code.setClickable(true);
////                SpannableStringBuilder style=new SpannableStringBuilder(tv_time.getText());
////                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink_text)), 8, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
////                tv_time.setText(style);
//            }
//        }.start();
//    }
    public void runlogin() {
        loadingDialog.show();
        LoginProtocol loginProtocol = new LoginProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = loginProtocol.getApiFun();
        loginRequest.map.put("phoneNumber", userphone);
        loginRequest.map.put("pwd", MD5Utils.MD5(pwd));
        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                loginButton.setClickable(true);
                Gson gson = new Gson();
                LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
                LogUtils.e("loginresponse:" + loginresponse.toString());
                if (loginresponse.code.equals("0")) {

                    SharedPrefrenceUtils.setString(LoginActivity.this, "userphone", userphone);
                    SharedPrefrenceUtils.setString(LoginActivity.this, "token", loginresponse.getAuthorization());
                    if (loginresponse.salesmanObject != null) {
                        setTagAndAlias("salesman" + loginresponse.salesmanObject.getId());
                        SharedPrefrenceUtils.setString(LoginActivity.this, "alias", "salesman" + loginresponse.salesmanObject.getId());
                        SPUtils.saveBean2Sp(LoginActivity.this, loginresponse.salesmanObject, "salesmanObject", "SalesmanBean");
                        Intent intent = new Intent(LoginActivity.this, MyselfActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                    } else if (loginresponse.businessObject != null) {
                        setTagAndAlias("business" + loginresponse.businessObject.getId());
                        SharedPrefrenceUtils.setInt(LoginActivity.this, "master", loginresponse.getBusinessLoginObject().getMaster());
                        SharedPrefrenceUtils.setString(LoginActivity.this, "alias", "business" + loginresponse.businessObject.getId());
                        LogUtils.e("alias设置:" + SharedPrefrenceUtils.getString(LoginActivity.this, "alias"));
                        SPUtils.saveBean2Sp(LoginActivity.this, loginresponse.businessObject, "businessObject", "MerchantBean");
                        Intent intent = new Intent(LoginActivity.this, MerchantMyselfActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                    }


                }else if(loginresponse.code.equals("-3")){
                    if (loginresponse.businessObject != null) {
                        SPUtils.saveBean2Sp(LoginActivity.this, loginresponse.businessObject, "businessObject", "MerchantBean");
                        Intent intent=new Intent(LoginActivity.this,AddMerchantInfoActivity.class);
                        UIUtils.startActivityNextAnim(intent);
                        finish();
                    }
                } else {
                    DialogUtils.showAlertDialog(LoginActivity.this,
                            loginresponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                loginButton.setClickable(true);
                DialogUtils.showAlertDialog(LoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    /**
     * 删除标签与别名
     */
    private void deleteAlias() {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了*/
        //上下文、别名【Sting行】、标签【Set型】、回调
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.alias = SharedPrefrenceUtils.getString(LoginActivity.this, "alias");
        sequence++;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
    }

    //获取版本信息
    public void getVersion() {
        GetVersionProtocol getVersionProtocol = new GetVersionProtocol();
        LoginRequest loginRequest = new LoginRequest();
        String url = getVersionProtocol.getApiFun();
        loginRequest.map.put("platform", "android");
        MyVolley.uploadNoFile(MyVolley.POST, url, loginRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                GetVersionResponse getVersionResponse = gson.fromJson(json, GetVersionResponse.class);
                LogUtils.e("getVersionResponse:" + getVersionResponse.toString());
                if (getVersionResponse.code.equals("0")) {
                    try {
                        double newVersionName = Double.parseDouble(getVersionResponse.getData().getVersion());
                        if (newVersionName > Double.parseDouble(versionName)) {
                            downloadUrl = getVersionResponse.getData().getUrl();
                            showUpdataDialog();
                        }else{
                            String token = SharedPrefrenceUtils.getString(LoginActivity.this, "token");
                            if (!TextUtils.isEmpty(token)) {

                                SalesmanBean salesmanBean = SPUtils.getBeanFromSp(LoginActivity.this, "salesmanObject", "SalesmanBean");
                                MerchantBean merchantBean = SPUtils.getBeanFromSp(LoginActivity.this, "businessObject", "MerchantBean");
                                if (salesmanBean != null) {
                                    setTagAndAlias("salesman" + salesmanBean.getId());
                                    Intent intent = new Intent(LoginActivity.this, MyselfActivity.class);
                                    UIUtils.startActivityNextAnim(intent);
                                    finish();
                                } else if (merchantBean != null) {
                                    setTagAndAlias("business" + merchantBean.getId());
                                    Intent intent = new Intent(LoginActivity.this, MerchantMyselfActivity.class);
                                    UIUtils.startActivityNextAnim(intent);
                                    finish();
                                }

                            }
                        }
                    } catch (Exception e) {

                    }


                } else {
                    DialogUtils.showAlertDialog(LoginActivity.this,
                            getVersionResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {

                DialogUtils.showAlertDialog(LoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }


        });
    }

    /**
     * 弹出对话框
     */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage("软件更新");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                downLoadApk();
                Uri uri = Uri.parse(downloadUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dialog.cancel();
            }
        });
        //当点取消按钮时不做任何举动
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    protected void downLoadApk() {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("正在下载更新");
        pd.show();

        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(downloadUrl, pd);
                    //安装APK
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    protected void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(LoginActivity.this, UIUtils.getPackageName() + ".provider_paths", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }


    //获取App版本
    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        return versionName;
    }


    @Override
    public void CallBackCityCodeList(List<CityCodeBean> list) {
        SharedPrefrenceUtils.setBoolean(LoginActivity.this,"getCode",true);
        List<CityCodeBean> cityCodeBeanList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(i!=0){
                cityCodeBeanList.add(list.get(i));
            }
        }
        listDataSave.setDataList("cityCodeBeanList",cityCodeBeanList);
    }
}
