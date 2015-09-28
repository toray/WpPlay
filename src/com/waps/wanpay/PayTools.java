package com.waps.wanpay;

import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener;

import android.content.Context;

public class PayTools {
	static Context mContext;
	static String APP_ID;
	static String APP_PID;

	public static void init(String appid, String app_pid,Context context) {
		APP_ID = appid;
		APP_PID = app_pid;
		PayConnect.getInstance(APP_ID, APP_PID, context);
	}

	public static void Pay(final PayBean bean, Context context, final PayCallBack palycallback) {
		// 初始化统计器(必须调用)
		PayConnect.getInstance(APP_ID, APP_PID, context);
		mContext = context;
		try {
			PayConnect.getInstance(context).pay(context, bean.getOrderId(), bean.getUserid(), bean.getPrice(), bean.getGoodsName(), bean.getGoodsDesc(), bean.getNotifyUrl(),
					new PayResultListener() {

						@Override
						public void onPayFinish(Context payViewContext, String orderId, int resultCode,
								String resultString, int payType, float amount, String goodsName) {
							if (resultCode == 0) {
								PayConnect.getInstance(mContext).closePayView(payViewContext);
								PayConnect.getInstance(mContext).confirm(orderId, payType);
								palycallback.onSuccess();
							} else {
								palycallback.onError();
							}
						}
					});

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	public static void Destory(Context context){
		PayConnect.getInstance(context).close();
	}

	public interface PayCallBack {
		public void onSuccess();

		public void onError();
	}

}
