package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.MerchantInfoActivity;
import com.ciba.wholefinancial.adapter.MerchantAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetMerchantInfoProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class MerchantInfoPager extends ViewTabBasePager {

    private Dialog loadingDialog;
    private String url;
    private GetMerchantListResponse getMerchantListResponse;
    private Gson gson;
    private List<MerchantBean> merchantBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private boolean isFirst;
    private SalesmanBean salesmanBean;
    private int businessId;
    private GetMerchantInfoResponse getMerchantInfoResponse;
    private ImageLoader imageLoader;
    private TextView tv_shop_name, tv_merchantshortname, tv_storeAddressCode, tv_servicePhone, tv_productDesc;
    private TextView tv_address;
    private ImageView iv_business_license;
    private ImageView iv_shop_pic;
    private ImageView iv_shop_in_pic;
    private ImageView iv_shop_in_pic_two;
    private ImageView iv_shop_in_pic_three,iv_specialPic,iv_bank_card_front,iv_bank_card_back;
    private TextView tv_manager_name;
    private TextView tv_tellphone, tv_idCardNumber, tv_idCardName;
    private TextView tv_title_idcard;
    private ImageView iv_id_card_front;
    private ImageView iv_id_card_back;
    private TextView tv_bank_card;
    private TextView tv_bank_user;
    private TextView tv_bank,tv_accountBank,tv_bankAddressCode;
    private TextView tv_alipay;
    private TextView tv_weixin;
    private TextView tv_weixinPayRate,tv_alipayRate,tv_xqRate;
    private TextView tv_remark,tv_creditCode,tv_business_scope;

    public MerchantInfoPager(Context context, boolean isFirst, int businessId) {
        super(context);
        this.isFirst = isFirst;
        this.businessId = businessId;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.merchant_info_pager, null);
        ViewUtils.inject(this, view);
        tv_accountBank= (TextView) view.findViewById(R.id.tv_accountBank);
        tv_idCardName = (TextView) view.findViewById(R.id.tv_idCardName);
        tv_idCardNumber = (TextView) view.findViewById(R.id.tv_idCardNumber);
        tv_servicePhone = (TextView) view.findViewById(R.id.tv_servicePhone);
        tv_productDesc = (TextView) view.findViewById(R.id.tv_productDesc);
        tv_merchantshortname = (TextView) view.findViewById(R.id.tv_merchantshortname);
        tv_storeAddressCode = (TextView) view.findViewById(R.id.tv_storeAddressCode);
        tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        iv_business_license = (ImageView) view.findViewById(R.id.iv_business_license);
        iv_shop_pic = (ImageView) view.findViewById(R.id.iv_shop_pic);
        iv_shop_in_pic_two = (ImageView) view.findViewById(R.id.iv_shop_in_pic_two);
        iv_shop_in_pic = (ImageView) view.findViewById(R.id.iv_shop_in_pic);
        iv_bank_card_front= (ImageView) view.findViewById(R.id.iv_bank_card_front);
        iv_bank_card_back= (ImageView) view.findViewById(R.id.iv_bank_card_back);
        iv_shop_in_pic_three = (ImageView) view.findViewById(R.id.iv_shop_in_pic_three);
        tv_manager_name = (TextView) view.findViewById(R.id.tv_manager_name);
        tv_bankAddressCode= (TextView) view.findViewById(R.id.tv_bankAddressCode);
        tv_tellphone = (TextView) view.findViewById(R.id.tv_tellphone);
        tv_title_idcard = (TextView) view.findViewById(R.id.tv_title_idcard);
        tv_bank_user = (TextView) view.findViewById(R.id.tv_bank_user);
        iv_id_card_front = (ImageView) view.findViewById(R.id.iv_id_card_front);
        iv_id_card_back = (ImageView) view.findViewById(R.id.iv_id_card_back);
        iv_specialPic = (ImageView) view.findViewById(R.id.iv_specialPic);
        tv_bank_card = (TextView) view.findViewById(R.id.tv_bank_card);
        tv_bank = (TextView) view.findViewById(R.id.tv_bank);
        tv_alipay = (TextView) view.findViewById(R.id.tv_alipayNumber);
        tv_weixin = (TextView) view.findViewById(R.id.tv_weixinNumber);
        tv_weixinPayRate = (TextView) view.findViewById(R.id.tv_weixinPayRate);
        tv_alipayRate= (TextView) view.findViewById(R.id.tv_alipayRate);
        tv_xqRate= (TextView) view.findViewById(R.id.tv_xqRate);
        tv_remark = (TextView) view.findViewById(R.id.tv_remark);
        tv_business_scope= (TextView) view.findViewById(R.id.tv_business_scope);
        tv_creditCode= (TextView) view.findViewById(R.id.tv_creditCode);
        return view;
    }

    @Override
    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));
        if (businessId != 0) {
            getMerchantInfo();
        }
    }

    public void setDate() {
        MerchantBean merchantBean = getMerchantInfoResponse.getData();
        tv_creditCode.setText("社会统一信用代码:"+merchantBean.getCreditCode());
        tv_business_scope.setText("经营范围:"+merchantBean.getBusinessScope());
        tv_servicePhone.setText("客服电话：" + merchantBean.getServicePhone());
        tv_productDesc.setText("服务描述：" + merchantBean.getProductDesc());
        tv_merchantshortname.setText("店铺简称：" + merchantBean.getMerchantShortname());
        tv_shop_name.setText("店铺名称：" + merchantBean.getStoreName());
        tv_storeAddressCode.setText("店铺省市编：" + merchantBean.getStoreAddressCode());
        tv_address.setText("店铺地址：" + merchantBean.getStoreStreet());
        tv_manager_name.setText("姓名：" + merchantBean.getContact());
        tv_tellphone.setText("手机：" + merchantBean.getContactPhone());
        tv_idCardName.setText("身份证姓名：" + merchantBean.getIdCardName());
        tv_idCardNumber.setText("身份证号：" + merchantBean.getIdCardNumber());
        tv_title_idcard.setText("身份证有效期：" + merchantBean.getIdCardValidStartTime()+"至"+merchantBean.getIdCardValidEndTime());
        tv_bank_card.setText("银行卡号：" + merchantBean.getAccountNumber());
        tv_bank_user.setText("开户人：" + merchantBean.getAccountName());
        tv_accountBank.setText("开户行：" + merchantBean.getAccountBank());
        tv_bankAddressCode.setText("开户银行省市编码：" + merchantBean.getBankAddressCode());
        tv_bank.setText("开户行全称（含支行）：" + merchantBean.getBankName());
        tv_alipay.setText("支付宝账号：" + merchantBean.getAlipayNumber());
        tv_weixin.setText("微信账号：" + merchantBean.getWeixinNumber());
        tv_weixinPayRate.setText("微信官网利率：" + merchantBean.getWeixinPayRate() + "%/笔");
        tv_alipayRate.setText("支付宝官网利率：" + merchantBean.getAlipayRate() + "%/笔");
        tv_xqRate.setText("享钱利率：" + merchantBean.getXqRate() + "%/笔");
        tv_remark.setText(merchantBean.getRemark());
        imageLoader.displayImage(merchantBean.getLicensePic(), iv_business_license, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getStoreEntrancePic(), iv_shop_pic, PictureOption.getSimpleOptions());
//        imageLoader.displayImage(merchantBean.getDoorInPics(), iv_shop_in_pic, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIdCardCopy(), iv_id_card_front, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIdCardNational(), iv_id_card_back, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getBankPic1(), iv_bank_card_front, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getBankPic2(), iv_bank_card_back, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getIndoorPic(), iv_shop_in_pic, PictureOption.getSimpleOptions());
        imageLoader.displayImage(merchantBean.getSpecialPic(), iv_specialPic, PictureOption.getSimpleOptions());
//        if(merchantBean.getDoorInPics().indexOf("[")!=-1){
//            char delChar = '[';
//            String temp=deleteString(merchantBean.getDoorInPics(),delChar);
//            char delChar_two = ']';
//            String last=deleteString(temp,delChar_two);
//            switch (convertStrToArray(last).length){
//                case 1:{
//                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_shop_in_pic, PictureOption.getSimpleOptions());
//                    break;
//                }
//                case 2:{
//                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_shop_in_pic, PictureOption.getSimpleOptions());
//                    imageLoader.displayImage(convertStrToArray(last)[1].substring(1,convertStrToArray(last)[1].length()-1), iv_shop_in_pic_two, PictureOption.getSimpleOptions());
//                    break;
//                }
//                case 3:{
//                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_shop_in_pic, PictureOption.getSimpleOptions());
//                    imageLoader.displayImage(convertStrToArray(last)[1].substring(1,convertStrToArray(last)[1].length()-1), iv_shop_in_pic_two, PictureOption.getSimpleOptions());
//                    imageLoader.displayImage(convertStrToArray(last)[2].substring(1,convertStrToArray(last)[2].length()-1), iv_shop_in_pic_three, PictureOption.getSimpleOptions());
//                    break;
//                }
//            }
//        }
    }

    public static String deleteString(String str, char delChar) {
        String delStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != delChar) {
                delStr += str.charAt(i);
            }
        }
        return delStr;
    }

    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    public void getMerchantInfo() {
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
                    DialogUtils.showAlertDialog(mContext, getMerchantInfoResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


}