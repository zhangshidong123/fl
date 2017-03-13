package com.plmz.fl.adapter;

import android.content.Context;

import com.plmz.fl.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends CommonAdapter<Movie>{
	private List<Integer> mPos = new ArrayList<Integer>();
	public MyAdapter(Context context,List<Movie> datas,int layoutId){
		super(context, datas,layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, Movie movie) {
//		viewHolder.setText(R.id.tv_tuijian_item,movie.getName()).setNetImageView(R.id.iv_tuijian_item,movie.getSrc());
		if(movie.getIsChecked()){
			mPos.add(viewHolder.getPosition());
		}else{
			mPos.remove((Integer)viewHolder.getPosition());
		}
	}
}
