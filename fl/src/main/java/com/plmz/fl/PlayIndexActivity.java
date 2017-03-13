package com.plmz.fl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plmz.fl.adapter.PlayIndexAdapter;
import com.plmz.fl.entity.PlayList;
import com.plmz.fl.http.Constant;
import com.plmz.fl.http.ParseResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlayIndexActivity extends Activity {
	private ImageView iv_play_index;
	private TextView back, tv_play_index;
	private ListView lv_play_index;
	private String url, imgUrl, name;
	private ImageLoader imageLoader;
	private ArrayList<PlayList> list;
	private PlayIndexAdapter adapter;
	private int fromType;
	private AbHttpUtil mAbHttpUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_index);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		fromType = getIntent().getIntExtra(Constant.FROM_TYPE, 1);
		initView();
		list = new ArrayList<PlayList>();
		url = getIntent().getStringExtra(Constant.URL_PLAY_INDEX);
		imgUrl = getIntent().getStringExtra(Constant.IMGURL_PLAY_INDEX);
		name = getIntent().getStringExtra(Constant.NAME_PLAY_INDEX);
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		getUrlDataHttps();
		AdView adView = (AdView) findViewById(R.id.adView_bottom);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	private void initView() {
		back = (TextView) findViewById(R.id.back);
		iv_play_index = (ImageView) findViewById(R.id.iv_play_index);
		tv_play_index = (TextView) findViewById(R.id.tv_play_index);
		lv_play_index = (ListView) findViewById(R.id.lv_play_index);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getUrlDataHttps() {
		//获取Http工具类
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			//获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				//不能写在完成中，因为完成在有重定向的情况会提前结束
				switch (fromType) {
					case 1:
						list = ParseResult.parsePlayIndex(content);
						break;
					case 2:
						list = ParseResult.parseXFPlayIndex(content);
						break;
				}
				if (!list.isEmpty()) {
					adapter = new PlayIndexAdapter(PlayIndexActivity.this, list, fromType);
					lv_play_index.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				setData();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
								  Throwable error) {
				AbToastUtil.showToast(PlayIndexActivity.this, error.getMessage());
			}

			// 开始执行前
			@Override
			public void onStart() {
			}

			// 完成后调用，失败，成功
			@Override
			public void onFinish() {
			}

			;
		});
	}

	private void setData() {
		imageLoader.displayImage(imgUrl, iv_play_index,
				FuliApp.OPTION, new SimpleImageLoadingListener() {
					List<String> displayedImages = Collections
							.synchronizedList(new LinkedList<String>());

					@Override
					public void onLoadingComplete(String imageUri, View view,
												  Bitmap loadedImage) {
						// TODO Auto-generated method stub
						if (loadedImage != null) {
							ImageView imageView = (ImageView) view;
							imageView.setScaleType(ImageView.ScaleType.CENTER);
							boolean firstDisplay = !displayedImages
									.contains(imageUri);
							if (firstDisplay) {
								FadeInBitmapDisplayer.animate(imageView, 500);
								displayedImages.add(imageUri);
							}
						}
					}
				});
		tv_play_index.setText(name == null ? "" : name);
	}

}
