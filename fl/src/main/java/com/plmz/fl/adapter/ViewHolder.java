package com.plmz.fl.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plmz.fl.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ViewHolder {
	// 图片option
	public static final DisplayImageOptions OPTION = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.ic_blank)
			.showImageForEmptyUri(R.mipmap.ic_blank)
			.showImageOnFail(R.mipmap.ic_blank).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(10)).build();
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private ImageLoader imageLoader;
	public ViewHolder(Context context,ViewGroup parent,int layoutId,int position){
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
		mConvertView.setTag(this);
	}
	public  static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView==null){
			return new ViewHolder(context,parent,layoutId,position);
		}else{
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			viewHolder.mPosition = position;
			return viewHolder;
		}
	}

	/**
	 * 通过viewId获取控件
	 * @param viewId
	 * @param <T>
	 * @return
	 */
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId,view);
		}
		return (T)view;
	}

	public View getConvertView(){
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	/**
	 * 设置TextView值
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId,String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	/**
	 * 设置ImageView图片
	 * @param viewId
	 * @param imageSrc
	 * @return
	 */
	public ViewHolder setImageView(int viewId,int imageSrc){
		ImageView iv = getView(viewId);
		iv.setImageResource(imageSrc);
		return this;
	}
	/**
	 * 加载网络ImageView图片
	 * @param viewId
	 * @param imgUrl
	 * @return
	 */
	public ViewHolder setNetImageView(int viewId,String imgUrl){
		ImageView iv = getView(viewId);
		imageLoader.displayImage(imgUrl, iv,
				OPTION, new SimpleImageLoadingListener() {
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
		return this;
	}
}
