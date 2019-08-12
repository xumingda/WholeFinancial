package com.ciba.wholefinancial.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.LoginActivity;
import com.ciba.wholefinancial.activity.SettingActivity;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.callback.OnClassNameCallBack;

import java.util.Calendar;

import static com.ciba.wholefinancial.base.BaseActivity.finishAll;

/**
 * 退出对话框(系统类型)
 * 
 * @author C's
 * @date 2014-4-1 下午5:59:10
 * @version V1.0
 */
public class DialogUtils {

	/** 自定义单按钮对话框 */
	public static void showAlertDialog(Context ctx, String alertContent) {
		String temp;
		temp = alertContent;
		final AlertDialog alertDialog = new Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		// *** 主要就是在这里实现这种效果的
		// 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
		View dialogView = View.inflate(ctx, R.layout.alert_dialog_main_title,
				null);
		window.setContentView(dialogView);
		// 设置对应提示框内容
		TextView alert_dialog_content = (TextView) dialogView
				.findViewById(R.id.alert_dialog_content);
		alert_dialog_content.setText(temp);
		// 点击空白处不消失
		alertDialog.setCanceledOnTouchOutside(false);
		
		
		// 为知道了按钮添加事件，执行退出应用操作
		TextView tv_roger = (TextView) dialogView.findViewById(R.id.tv_roger);
		tv_roger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});
	}
	/** 自定义单按钮对话框
	 * 	用登陆使用
	 *  +点击事件
	 *  */
	public static Dialog showAlertToLoginDialog(Context ctx, String alertContent) {
		Builder builder = new Builder(ctx);
		
		final AlertDialog alertDialog =  builder.create();
//		final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		// *** 主要就是在这里实现这种效果的
		// 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
		View dialogView = View.inflate(ctx, R.layout.alert_dialog_main_title,
				null);
		window.setContentView(dialogView);
		
		// 设置对应提示框内容
		TextView alert_dialog_content = (TextView) dialogView
				.findViewById(R.id.alert_dialog_content);
		alert_dialog_content.setText(alertContent);
		// 点击空白处不消失
		alertDialog.setCanceledOnTouchOutside(false);
		// 为知道了按钮添加事件，执行退出应用操作
		Button tv_roger = (Button) dialogView.findViewById(R.id.tv_roger);
		tv_roger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseActivity.finishAll();
				SPUtils.saveBean2Sp(UIUtils.getContext(),null,"salesmanObject","SalesmanBean");
				SPUtils.saveBean2Sp(UIUtils.getContext(),null,"businessObject","MerchantBean");
				SharedPrefrenceUtils.setString(UIUtils.getContext(),"token","");
				Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
				UIUtils.startActivityNextAnim(intent);
				alertDialog.cancel();
			}
		});
		return alertDialog; 
	}
	/** 自定义双按钮对话框 */
	//type0退出登录，1结账
	public static AlertDialog showAlertDoubleBtnDialog(final Context ctx, String alertContent, String title, final int type,OnClickListener onClickListener) {
		String temp;
		temp = alertContent;
		final AlertDialog alertDialog = new Builder(ctx).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		// *** 主要就是在这里实现这种效果的
		// 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
		View dialogView = View.inflate(ctx, R.layout.alert_dialog_double_btn,
				null);
		window.setContentView(dialogView);
		// 设置对应提示框内容
		TextView alert_dialog_content = (TextView) dialogView
				.findViewById(R.id.alert_dialog_content);
		alert_dialog_content.setText(temp);

		// 设置对应提示框内容
		TextView tv_title = (TextView) dialogView
				.findViewById(R.id.alert_dialog_title);
		tv_title.setText(title);
		// 点击空白处不消失
		alertDialog.setCanceledOnTouchOutside(false);
		// 为知道了按钮添加事件，执行退出应用操作
		TextView tv_ensure = (TextView) dialogView.findViewById(R.id.tv_ensure);
		tv_ensure.setOnClickListener(onClickListener);
//		tv_ensure.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(type==0){
//					BaseActivity.finishAll();
//					SharedPrefrenceUtils.setString(ctx,"token","");
//					Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
//					UIUtils.startActivityNextAnim(intent);
//				}
//				alertDialog.cancel();
//
//			}
//		});
		TextView tv_cancle = (TextView) dialogView.findViewById(R.id.tv_cancle);
		tv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();

			}
		});
		return alertDialog;
	}

	/**
	 * 创建普通双按钮对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon[不想显示就写0] 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnPositiveName
	 *            第一个按钮名称 必填
	 * @param listener_Positive
	 *            第一个监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param btnNegativeName
	 *            第二个按钮名称 必填
	 * @param listener_Negative
	 *            第二个监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return 对话框实例
	 */
//	public static Dialog createDialog(Context ctx, int iconId, String title,
//			String message, String btnPositiveName,
//			android.content.DialogInterface.OnClickListener listener_Positive,
//			String btnNegativeName,
//			android.content.DialogInterface.OnClickListener listener_Negative) {
//		Dialog dialog = null;
//		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
//				ctx);
//		// 设置对话框的图标
//		builder.setIcon(iconId);
//		// 设置对话框的标题
//		builder.setTitle(title);
//		// 设置对话框的显示内容
//		builder.setMessage(message);
//		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
//		builder.setPositiveButton(btnPositiveName, listener_Positive);
//		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
//		builder.setNegativeButton(btnNegativeName, listener_Negative);
//		// 创建一个普通对话框
//		dialog = builder.create();
//		// 点击空白处不消失
//		dialog.setCanceledOnTouchOutside(false);
//		return dialog;
//	}

	/**
	 * 创建列表对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createListDialog(Context ctx, int iconId,
			String title, int itemsId,
			DialogInterface.OnClickListener listener) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
		builder.setItems(itemsId, listener);
		// 创建一个列表对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建单选按钮对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param listener
	 *            单选按钮项监听器，需实现android.content.DialogInterface.OnClickListener接口
	 *            必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener2
	 *            按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createRadioDialog(Context ctx, int iconId,
			String title, int itemsId,
			DialogInterface.OnClickListener listener,
			String btnName,
			DialogInterface.OnClickListener listener2) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		// 0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(itemsId, 0, listener);
		// 添加一个按钮
		builder.setPositiveButton(btnName, listener2);
		// 创建一个单选按钮对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建复选对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param itemsId
	 *            字符串数组资源id 必填
	 * @param flags
	 *            初始复选情况 必填
	 * @param listener
	 *            单选按钮项监听器，需实现android.content.DialogInterface.
	 *            OnMultiChoiceClickListener接口 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener2
	 *            按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createCheckBoxDialog(
			Context ctx,
			int iconId,
			String title,
			int itemsId,
			boolean[] flags,
			DialogInterface.OnMultiChoiceClickListener listener,
			String btnName,
			DialogInterface.OnClickListener listener2) {
		Dialog dialog = null;
		Builder builder = new Builder(
				ctx);
		// 设置对话框的图标
		builder.setIcon(iconId);
		// 设置对话框的标题
		builder.setTitle(title);
		builder.setMultiChoiceItems(itemsId, flags, listener);
		// 添加一个按钮
		builder.setPositiveButton(btnName, listener2);
		// 创建一个复选对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 日期对话框
	 * 
	 * @param context
	 *            上下文
	 * @param v
	 *            带.setText的控件
	 * @return 对话框实例
	 */
	public static Dialog createDateDialog(Context context, final View v) {
		Dialog dialog = null;
		Calendar calender = Calendar.getInstance();
		dialog = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						if (v instanceof TextView) {

							((TextView) v).setText(year + "年"
									+ (monthOfYear + 1) + "月" + dayOfMonth
									+ "日");
						}
						if (v instanceof EditText) {
							((EditText) v).setText(year + "年"
									+ (monthOfYear + 1) + "月" + dayOfMonth
									+ "日");
						}

					}
				}, calender.get(calender.YEAR), calender.get(calender.MONTH),
				calender.get(calender.DAY_OF_MONTH));

		return dialog;
	}

	// /**
	// * 加载对话框
	// *
	// * @param context
	// * 上下文
	// * @param msg
	// * 内容
	// * @return
	// */
	// public static ProgressDialog createLoadDialog(Context context, String
	// msg) {
	//
	// android.app.ProgressDialog dialog = new ProgressDialog(context);
	// // 设置风格为圆形进度条
	// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// // 设置内容
	// dialog.setMessage(msg);
	// // 设置进度条是否为不明确
	// dialog.setIndeterminate(false);
	// // 设置空白出不关闭
	// dialog.setCanceledOnTouchOutside(false);
	// // 设置进度条是否可以按退回键取消
	// dialog.setCancelable(true);
	// return dialog;
	// }

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog createLoadDialog2(Context context, boolean isClickable) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		layout.getBackground().setAlpha(100);
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		
		loadingDialog.setCancelable(isClickable);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;
	}
	public static Dialog createLoadDialog(Context context, boolean isClickable) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_layout, null);// 得到加载view
		Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(isClickable);// 不可以用“返回键”取消
		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}

	/**
	 * @return 创建一个提示网络没有打开的dialog
	 */
	public static void createNetWorkNotOpenDialog(Context ctx) {
		 showAlertDialog(ctx, "网络连接已经断开,请稍后重试");
	}
	
	/**
	 * @return 创建带进度条的对话框
	 */
	public static ProgressDialog createProgressDialog(Context context,DialogInterface.OnClickListener listener){
		
		
		ProgressDialog dialog  = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,"取消", listener);
		return dialog;
	}


	// 提示对话框
//	public static void showUpdateDialog(Context context, String alertContent,final String url) {
//		final Context mContext=context;
//		try {
//			String temp;
//			temp = alertContent;
//			final AlertDialog alertDialog = new Builder(mContext)
//					.create();
//			// Alertdialog对话框，设置点击其他位置不消失 ,必须先AlertDialog.Builder.create()之后才能调用
//			alertDialog.setCanceledOnTouchOutside(false);
//			alertDialog.show();
////				设置对话框宽度
//			WindowManager manager=alertDialog.getWindow().getWindowManager();
//			Display d=manager.getDefaultDisplay();
//			WindowManager.LayoutParams params=alertDialog.getWindow().getAttributes();
//			params.width=(int) (d.getWidth()*0.85);
////				params.height=(int) (d.getHeight()*0.28);
//			alertDialog.getWindow().setAttributes(params);
//			Window window = alertDialog.getWindow();
//			// *** 主要就是在这里实现这种效果的.
//			// 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
//			View dialogView = View.inflate(context,
//					R.layout.alert_dialog_update, null);
//			window.setContentView(dialogView);
//			// 设置对应提示框内容
//			TextView alert_dialog_content = (TextView) dialogView
//					.findViewById(R.id.alert_dialog_content);
//			alert_dialog_content.setText(temp);
//			// 为知道了按钮添加事件，执行退出应用操作
//			TextView tv_roger = (TextView) dialogView
//					.findViewById(R.id.tv_cancel);
//			tv_roger.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					alertDialog.cancel();
//				}
//			});
//
//			TextView tv_update = (TextView) dialogView
//					.findViewById(R.id.tv_update);
//			tv_update.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					alertDialog.cancel();
//					Uri uri = Uri.parse(url);
//					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//					mContext.startActivity(intent);
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	public interface AlertConfirm {
		public void onConfirm(String content);

		public void onCancel();

		public void onsend();
	}

	//包含输入框的弹出对话框,title和subtitle如果不设置的话，可以为null,type=1，则为默认的情况，type=2，是如果用户输入密码，程序会保存密码
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static void createInputDialog(final Context context, String title, String name, int type, final OnClassNameCallBack onClassNameCallBack) {
		final AlertDialog updateNameDialog = new Builder(
				context).create();
		// Alertdialog对话框，设置点击其他位置不消失 ,必须先AlertDialog.Builder.create()之后才能调用
		updateNameDialog.setCanceledOnTouchOutside(true);
		updateNameDialog.show();
		// 设置对话框宽度
		WindowManager manager = updateNameDialog.getWindow().getWindowManager();
		Display d = manager.getDefaultDisplay();
		WindowManager.LayoutParams params = updateNameDialog.getWindow()
				.getAttributes();
		params.width = (int) (d.getWidth() * 0.85);
		// params.height=(int) (d.getHeight()*0.28);
		Window window = updateNameDialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,alert_dialog_main_title.xml文件中定义view内容
		View dialogView = View.inflate(context,
				R.layout.alert_input_dialog, null);// 就是这个布局弄成之前一直错错错，空指针异常，老方法
		window.setContentView(dialogView);
		// 下面灰常有用，用于使键盘强制弹出
		window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		TextView titleView = (TextView) dialogView
				.findViewById(R.id.altert_title);
		if (title != null) {
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
		} else {
			titleView.setVisibility(View.GONE);
		}

		titleView.setText(title);
		// 确定
		final TextView tv_commit = (TextView) dialogView.findViewById(R.id.tv_commit);


		RelativeLayout rl_cancel = (RelativeLayout) dialogView.findViewById(R.id.rl_cancel);
		// 输入框
		final EditText et_myIbeaconName = (EditText) dialogView
				.findViewById(R.id.alert_input);
		et_myIbeaconName.setText(name);
//		TextPaint textPaint=tv_send.getPaint();
//		textPaint.setColor(UIUtils.getColor(R.color.colorAccent));
//		textPaint.setFlags(Paint.UNDERLINE_TEXT_FLAG);

		tv_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!TextUtils.isEmpty(et_myIbeaconName.getText().toString())){
					onClassNameCallBack.CallBackClassName(et_myIbeaconName.getText().toString());
					updateNameDialog.cancel();
				}

			}
		});
		//0添加，1修改
		if(type==0){
			tv_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_commit_normal));
			tv_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
		}
		else{
			tv_commit.setBackground(UIUtils.getDrawable(R.mipmap.btn_clock_success));
			tv_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
		}


		et_myIbeaconName.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL);
		InputMethodManager imm;
		imm = (InputMethodManager) et_myIbeaconName.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		et_myIbeaconName.addTextChangedListener(new TextWatcher() {
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
					tv_commit.setBackground(UIUtils.getDrawable(R.mipmap.btn_clock_success));
					tv_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
				}

			}
		});

	}
}
