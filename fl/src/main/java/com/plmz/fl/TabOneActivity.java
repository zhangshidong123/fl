package com.plmz.fl;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.GridView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.plmz.fl.adapter.ClassAdapter;
import com.plmz.fl.adapter.TuijianAdapter;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.CodeSwap;
import com.plmz.fl.http.ParseResult;
import com.plmz.fl.http.URLClass;

import java.util.ArrayList;

public class TabOneActivity extends Activity {
	private int tabIndex;
	private GridView xGridView;
	private ArrayList<String> list;
	private ClassAdapter adapter;
	private PullToRefreshGridView xGridView_tuijian;
	private ArrayList<Movie> movieList;
	private ArrayList<Movie> newMovieList;
	private TuijianAdapter tuijianAdapter;
	private AbHttpUtil mAbHttpUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_one);
		initView();
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		tabIndex = getIntent().getIntExtra("activityId", 0);
		list = new ArrayList<String>();
		adapter = new ClassAdapter(this, list, tabIndex);
		xGridView.setAdapter(adapter);
		movieList = new ArrayList<Movie>();
		newMovieList = new ArrayList<Movie>();
		tuijianAdapter = new TuijianAdapter(this,1, movieList);
		xGridView_tuijian.setAdapter(tuijianAdapter);
		tabType();
		setData();
	}

	private void tabType() {
		switch (tabIndex) {
			case 0:
				getTabOneClassData();
				getTabOne();
				break;
			case 1:
				getTabTwoClassData();
				getTabTwo();
				break;
			case 2:

				break;
			case 3:
				break;
			case 4:
				break;
		}
	}

	private void initView() {
		xGridView = (GridView) findViewById(R.id.xGridView);
		xGridView_tuijian = (PullToRefreshGridView) findViewById(R.id.xGridView_tuijian);
	}

	private void setData() {
		xGridView_tuijian.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				//加载数据
				newMovieList = new ArrayList<Movie>();
				switch (tabIndex) {
					case 0:
						getTabOne();
						break;
					case 1:
						getTabTwo();
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			}
		});
	}

	//tab one
	private void getTabOneClassData() {
		String[] data = getResources().getStringArray(
				R.array.video_small_class);
		for (String str : data) {
			list.add(str);
		}
		adapter.notifyDataSetChanged();
	}

	//tabone
	private void getTabOne() {
		String url = URLClass.MAIN_URL;
		//获取Http工具类
		mAbHttpUtil.setEncode("gbk");
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			//获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				String str="";
				try {
					str = CodeSwap.Unicode2GBK(content);
					newMovieList = ParseResult.parseLi(str);
					if (!newMovieList.isEmpty()) {
						movieList.clear();
						if (newMovieList != null && newMovieList.size() > 0) {
							movieList.addAll(newMovieList);
							tuijianAdapter.notifyDataSetChanged();
							newMovieList.clear();
						}
					}
				}catch (Exception e){
					Toast.makeText(TabOneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
				}
				xGridView_tuijian.onRefreshComplete();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
								  Throwable error) {
				AbToastUtil.showToast(TabOneActivity.this, error.getMessage());
				xGridView_tuijian.onRefreshComplete();
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
	//tabtwo
	private void getTabTwoClassData() {
		String[] data = getResources().getStringArray(
				R.array.video_dianshiju_class);
		for (String str : data) {
			list.add(str);
		}
		adapter.notifyDataSetChanged();
	}

	private void getTabTwo() {
		String url = URLClass.DIANSHIJU_MAIN_URL;
		//获取Http工具类
		mAbHttpUtil.setEncode("gbk");
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			//获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				String str = "";
				try {
					str = CodeSwap.Unicode2GBK(content);
					newMovieList = ParseResult.parseMovieTab(str);
					if (!newMovieList.isEmpty()) {
						movieList.clear();
						if (newMovieList != null && newMovieList.size() > 0) {
							movieList.addAll(newMovieList);
							tuijianAdapter.notifyDataSetChanged();
							newMovieList.clear();
						}
					}
				} catch (Exception e) {
					Toast.makeText(TabOneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				xGridView_tuijian.onRefreshComplete();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
								  Throwable error) {
				AbToastUtil.showToast(TabOneActivity.this, error.getMessage());
				xGridView_tuijian.onRefreshComplete();
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
}
