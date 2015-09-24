package com.waps.wanpay;

import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener;

import android.content.Context;

public class PayTools {
	// 应用或游戏商自定义的支付订单(每条支付订单数据不可相同)
	static String orderId = "";
	// 用户标识
	static String userId = "";// userid
	// 支付商品名称
	static String goodsName = "测试商品";
	// 支付金额
	static float price = 0.0f;
	// 支付描述
	static String goodsDesc = "";
	// 应用或游戏商服务器端回调接口（无服务器可不填写）
	static String notifyUrl = "";
	static Context mContext;
	static String APP_ID;
	static String APP_PID;

	public static void init(String appid, String app_pid) {
		APP_ID = appid;
		APP_PID = app_pid;
	}

	public static void Pay(float number, Context context, final PayCallBack palycallback) {
		// 初始化统计器(必须调用)
		PayConnect.getInstance(APP_ID, APP_PID, context);

		mContext = context;
		goodsDesc = "购买" + goodsName;

		userId = PayConnect.getInstance(context).getDeviceId(context);

		try {
			// // 游戏商自定义支付订单号，保证该订单号的唯一性，建议在执行支付操作时才进行该订单号的生成
			orderId = System.currentTimeMillis() + "";

			String amountStr = String.valueOf(number);
			if (!"".equals(amountStr)) {
				price = Float.valueOf(amountStr);
			} else {
				price = 0.0f;
			}

			PayConnect.getInstance(context).pay(context, orderId, userId, price, goodsName, goodsDesc, notifyUrl,
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

	public interface PayCallBack {
		public void onSuccess();

		public void onError();
	}

}
