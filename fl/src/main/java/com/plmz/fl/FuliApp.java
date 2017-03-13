package com.plmz.fl;

import android.app.Application;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FuliApp extends Application {
	public static DisplayMetrics DM = new DisplayMetrics();
	 // 图片option
	public static DisplayImageOptions OPTION = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.ic_blank)
			.showImageForEmptyUri(R.mipmap.ic_blank)
			.showImageOnFail(R.mipmap.ic_blank).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(10)).build();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

}
