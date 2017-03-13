package com.plmz.fl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.plmz.fl.adapter.MyExpandableListAdapter;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.Constant;

import java.util.ArrayList;

public class XianFengActivity extends AppCompatActivity {
	private Intent intent;
	private TextView back;
	private ExpandableListView xEListView_xf;
	private ArrayList<Movie> groupList;
	private ArrayList<ArrayList<Movie>> childList;
	private MyExpandableListAdapter adapter;
	private String [] groupArr,childArr,childValueArr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xian_feng);
		initView();
		groupList = new ArrayList<Movie>();
		childList = new ArrayList<ArrayList<Movie>>();
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initData();
		adapter = new MyExpandableListAdapter(XianFengActivity.this,groupList,childList);
		xEListView_xf.setAdapter(adapter);
		xEListView_xf.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				intent = new Intent(XianFengActivity.this,ContentGridActivity.class);
				intent.putExtra(Constant.FROM_TYPE,2);
				String src = ((Movie)adapter.getChild(groupPosition,childPosition)).getSrc();
				intent.putExtra(Constant.XG_MOVIE_TYPE,src);
				startActivity(intent);
				return false;
			}
		});
	}
	private void initView(){
		back = (TextView)findViewById(R.id.back);
		xEListView_xf = (ExpandableListView) findViewById(R.id.xEListView_xf);
	}
	private void initData(){
		ArrayList<Movie> list = null;
		groupArr = getResources().getStringArray(R.array.xf_group);
		for (int i=0;i<groupArr.length;i++){
			switch (i){
				case 0:
					childArr = getResources().getStringArray(R.array.xf_child1);
					childValueArr = getResources().getStringArray(R.array.xf_child_value1);
					list = new ArrayList<Movie>();
					for (int j=0;j<childArr.length;j++){
						Movie movie = new Movie();
						movie.setName(childArr[j]);
						movie.setSrc(childValueArr[j]);
						list.add(movie);
					}
					break;
				case 1:
					childArr = getResources().getStringArray(R.array.xf_child2);
					childValueArr = getResources().getStringArray(R.array.xf_child_value2);
					list = new ArrayList<Movie>();
					for (int j=0;j<childArr.length;j++){
						Movie movie = new Movie();
						movie.setName(childArr[j]);
						movie.setSrc(childValueArr[j]);
						list.add(movie);
					}
					break;
			}
			childList.add(list);
			Movie groupMovie = new Movie();
			groupMovie.setName(groupArr[i]);
			groupList.add(groupMovie);
		}
	}
}
