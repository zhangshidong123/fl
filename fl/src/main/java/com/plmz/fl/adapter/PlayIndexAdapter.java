package com.plmz.fl.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.plmz.fl.R;
import com.plmz.fl.entity.PlayList;
import com.plmz.fl.http.MyAsyncTask;
import com.plmz.fl.http.ParseResult;
import com.plmz.fl.tool.Jump;

import java.util.ArrayList;


public class PlayIndexAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<PlayList> list;
	private Intent intent;
	private String xiGua,xianFeng;
	private AbHttpUtil mAbHttpUtil;
	private int fromType;

	public PlayIndexAdapter(Context mContext, ArrayList<PlayList> list,int fromType) {
		this.mContext = mContext;
		this.list = list;
		this.fromType = fromType;
		mAbHttpUtil = AbHttpUtil.getInstance(mContext);
		mAbHttpUtil.setTimeout(10000);
	}

	public int getCount() {
		return list.size();
	}

	public PlayList getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView tv;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_playindex, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView
					.findViewById(R.id.tv_playindex_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PlayList play = list.get(position);
		holder.tv.setText(play.getTitle());
		holder.tv.setOnClickListener(new ItemClickListener(play));
		return convertView;
	}

	class ItemClickListener implements OnClickListener {
		private PlayList play;

		public ItemClickListener(PlayList play) {
			// TODO Auto-generated constructor stub
			this.play = play;
		}

		@Override
		public void onClick(View v) {
			switch (fromType){
				case 1:
					getXGPlayUrl(play.getLink());
					break;
				case 2:
					getXFPlayUrl(play.getLink());
					break;
			}
			//先锋
//			if(xgIndex!=-1){
//				String xiGua = mContext.getResources().getString(R.string.xigua);
//				//已安装，打开程序，需传入参数包名："com.skype.android.verizon"
//				if (Jump.isAvilible(mContext, xiGua)) {
//					getPlayUrl(play.getLink(),xiGua);
//				} else {
//					Toast.makeText(mContext, "请先下载西瓜影音APP", Toast.LENGTH_LONG).show();
//				}
//			}
		}
	}

	//获取playurl
	private void getXGPlayUrl(String url) {
		xiGua = mContext.getResources().getString(R.string.xigua);
		int httpsIndex = url.indexOf("https");
		if (httpsIndex != -1) {//福利篇
			mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
				//获取数据成功会调用这里
				@Override
				public void onSuccess(int statusCode, String content) {
					String playUrl = ParseResult.parsePlayUrl(content);
					playType(playUrl);
				}

				// 失败，调用
				@Override
				public void onFailure(int statusCode, String content,
									  Throwable error) {
					AbToastUtil.showToast(mContext, error.getMessage());
				}

				// 开始执行前
				@Override
				public void onStart() {
				}

				// 完成后调用，失败，成功
				@Override
				public void onFinish() {
				};
			});
		} else {//普通篇
			try {
				new MyAsyncTask(mContext, true, url, "正在加载资源...") {
					@Override
					public void onPost(String jsonBack) {
						// TODO Auto-generated method stub
						String playUrl = ParseResult.parsePlayUrl(jsonBack);
						playType(playUrl);
					}
				}.execute();
			} catch (Exception e) {
				Toast.makeText(mContext, "获取资源异常：" + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	//获取playurl
	private void getXFPlayUrl(String url) {
		xianFeng = mContext.getResources().getString(R.string.xianfeng);
			mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
				//获取数据成功会调用这里
				@Override
				public void onSuccess(int statusCode, String content) {
					String playUrl = ParseResult.parseXFPlayUrl(content);
					playType(playUrl);
				}

				// 失败，调用
				@Override
				public void onFailure(int statusCode, String content,
									  Throwable error) {
					AbToastUtil.showToast(mContext, error.getMessage());
				}

				// 开始执行前
				@Override
				public void onStart() {
				}

				// 完成后调用，失败，成功
				@Override
				public void onFinish() {
				};
			});
	}

	private void playType(String playUrl) {
		if (!TextUtils.isEmpty(playUrl)) {
			int qvodIndex = playUrl.indexOf("qvod");
			if (qvodIndex != -1) {//快播
				//请复制地址到电脑快播下载
				Toast.makeText(mContext, "请复制地址到电脑快播下载:" + playUrl, Toast.LENGTH_LONG).show();
				return;
			}
			int xgIndex = playUrl.indexOf("ftp");
			if (xgIndex != -1) {//西瓜
				//已安装，打开程序，需传入参数包名："com.skype.android.verizon"
				if (Jump.isAvilible(mContext, xiGua)) {
					String uriStr = playUrl.replace("ftp://", "xg://");
					Jump.doStartApplicationWithPackageName(mContext, xiGua, uriStr,1);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setTitle("提示");
					builder.setMessage("请先下载西瓜影音APP");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Jump.downloadApp(mContext, 1);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}
			}else{
				int xfIndex = playUrl.indexOf("xfplay:");
				if(xfIndex!=-1){
					if (Jump.isAvilible(mContext, xianFeng)) {
						Jump.doStartApplicationWithPackageName(mContext,xianFeng,playUrl,2);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setTitle("提示");
						builder.setMessage("请先下载先锋影音播放器APP");
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								Jump.downloadApp(mContext,2);
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
				}
			}
		}
	}
}
