package com.plmz.fl.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * MyAsyncTask请求异步类,使用前判断网络是否可用
 */
public abstract class MyAsyncTask extends AsyncTask<Void, Void, String> {
	private ProgressDialog dialog;
	private boolean isProgressBarEnable;
	private String url;
	private Context con;
	private String msg;

	public MyAsyncTask(Context con, boolean isProgressBarEnable, String url, String msg) {
		this.con = con;
		this.isProgressBarEnable = isProgressBarEnable;
		this.url = url;
		this.msg = msg;
	}

	@Override
	protected void onPreExecute() {
		if (isProgressBarEnable) {
			dialog = new ProgressDialog(con);
			dialog.setIndeterminate(true);
			dialog.setMessage(msg);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		// 发送
		try {
			return HttpUtil.connect(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		onPost(result);
		super.onPostExecute(result);
	}

	/**
	 * 主线程运行，处理服务器返回的json数据
	 *
	 * @param jsonBack
	 */
	public abstract void onPost(String jsonBack);

}
