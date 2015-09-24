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
	// 支付时间
	static String time = "";
	// 支付描述
	static String goodsDesc = "";
	// 应用或游戏商服务器端回调接口（无服务器可不填写）
	static String notifyUrl = "";
	static Context mContext;

	public static void Pay(float number, Context context) {
		// 初始化统计器(必须调用)
		PayConnect.getInstance("d275369741d9d5afbca9af87e9a3c143", "WAPS", context);

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
					new MyPayResultListener());

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private static class MyPayResultListener implements PayResultListener {

		@Override
		public void onPayFinish(Context payViewContext, String orderId, int resultCode, String resultString,
				int payType, float amount, String goodsName) {
			// 可根据resultCode自行判断
			if (resultCode == 0) {
				// 支付成功时关闭当前支付界面
				PayConnect.getInstance(mContext).closePayView(payViewContext);

				// TODO 在客户端处理支付成功的操作

				// 未指定notifyUrl的情况下，交易成功后，必须发送回执
				PayConnect.getInstance(mContext).confirm(orderId, payType);
			} else {

			}
		}
	}
	public interface PayCallBack{
		public void onSuccess();
		public void onError();
	}
	
}
