package com.plmz.fl.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.plmz.fl.ContentGridActivity;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.Constant;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private Activity activity;
	private ArrayList<Movie> groupList;
	private ArrayList<ArrayList<Movie>> childList;
	private Intent intent;

	public MyExpandableListAdapter(Activity activity, ArrayList<Movie> groupList, ArrayList<ArrayList<Movie>> childList) {
		this.activity = activity;
		this.groupList = groupList;
		this.childList = childList;
	}

	/*-----------------Child */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		Movie movie = childList.get(groupPosition).get(childPosition);
		View view = getGenericView(movie , 2);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Movie movie = (Movie)getChild(groupPosition,childPosition);
				intent = new Intent(activity, ContentGridActivity.class);
				intent.putExtra(Constant.FROM_TYPE,2);
				intent.putExtra(Constant.XG_MOVIE_TYPE,movie.getSrc());
				activity.startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).size();
	}

	/* ----------------------------Group */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return getGroup(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		Movie movie = groupList.get(groupPosition);
		return getGenericView(movie , 1);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private TextView getGenericView(Movie movie , int type) {
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		TextView textView = new TextView(activity);
		textView.setLayoutParams(layoutParams);

		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		if(type == 1){
			textView.setPadding(20, 20, 20, 20);
			textView.setTextSize(18);
			textView.setTextColor(Color.BLACK);
		}else if(type == 2){
			textView.setPadding(30, 15, 20, 15);
			textView.setTextSize(15);
		}
		textView.setText(movie.getName());
		return textView;
	}
}
