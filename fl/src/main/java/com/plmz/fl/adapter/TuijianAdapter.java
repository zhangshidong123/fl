package com.plmz.fl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plmz.fl.FuliApp;
import com.plmz.fl.PlayIndexActivity;
import com.plmz.fl.R;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TuijianAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Movie> list;
	private Intent intent;
	private ImageLoader imageLoader;
	private int fromType;

//	public TuijianAdapter(Context mContext, ArrayList<Movie> list) {
//		this.mContext = mContext;
//		this.list = list;
//		imageLoader = ImageLoader.getInstance();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
//	}

	public TuijianAdapter(Context mContext, int fromType, ArrayList<Movie> list) {
		this.mContext = mContext;
		this.fromType = fromType;
		this.list = list;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	public int getCount() {
		return list.size();
	}

	public Movie getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView iv;
		TextView tv;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_tuijian, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView
					.findViewById(R.id.iv_tuijian_item);
			holder.tv = (TextView) convertView
					.findViewById(R.id.tv_tuijian_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Movie movie = list.get(position);
		holder.tv.setText(movie.getName());
		holder.iv.setOnClickListener(new ItemClickListener(movie));
		holder.tv.setOnClickListener(new ItemClickListener(movie));
		imageLoader.displayImage(movie.getSrc(), holder.iv,
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
		return convertView;
	}

	class ItemClickListener implements OnClickListener {
		private Movie movie;

		public ItemClickListener(Movie movie) {
			// TODO Auto-generated constructor stub
			this.movie = movie;
		}

		@Override
		public void onClick(View v) {
			intent = new Intent(mContext, PlayIndexActivity.class);
			intent.putExtra(Constant.FROM_TYPE, fromType);
			intent.putExtra(Constant.URL_PLAY_INDEX, movie.getLink());
			intent.putExtra(Constant.IMGURL_PLAY_INDEX, movie.getSrc());
			intent.putExtra(Constant.NAME_PLAY_INDEX, movie.getName());
			mContext.startActivity(intent);
		}
	}


}
