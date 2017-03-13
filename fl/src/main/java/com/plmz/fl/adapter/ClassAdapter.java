package com.plmz.fl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.plmz.fl.MovieTabActivity;
import com.plmz.fl.R;
import com.plmz.fl.http.Constant;

import java.util.ArrayList;

public class ClassAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> list;
	private Intent intent;
	private int fromType = 0;

	public ClassAdapter(Context mContext, ArrayList<String> list,int fromType) {
		this.mContext = mContext;
		this.list = list;
		this.fromType = fromType;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolderGame {
		TextView tv;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolderGame holderGame;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_class, null);
			holderGame = new ViewHolderGame();
			holderGame.tv = (TextView) convertView
					.findViewById(R.id.tv_fragmentclass_item);
			convertView.setTag(holderGame);
		} else {
			holderGame = (ViewHolderGame) convertView.getTag();
		}
		String classSmallLevel = list.get(position);
		holderGame.tv.setText(classSmallLevel);
		holderGame.tv.setOnClickListener(new ItemClickListener(position));
		switch (position){
			case 0:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie1);
				break;
			case 1:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie2);
				break;
			case 2:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie3);
				break;
			case 3:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie4);
				break;
			case 4:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie5);
				break;
			case 5:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie6);
				break;
			case 6:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie7);
				break;
			case 7:
				holderGame.tv.setBackgroundResource(R.drawable.corner_movie8);
				break;
			default:
				break;
		}
		return convertView;
	}

	class ItemClickListener implements OnClickListener {
		private int position;

		public ItemClickListener( int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			intent = new Intent(mContext, MovieTabActivity.class);
			intent.putExtra(Constant.FROM_TYPE,fromType);
			intent.putExtra(Constant.MOVIE_TYPE,position);
			mContext.startActivity(intent);
		}
	}
}
