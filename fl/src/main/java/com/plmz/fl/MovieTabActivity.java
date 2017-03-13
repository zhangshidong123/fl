package com.plmz.fl;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.plmz.fl.adapter.TuijianAdapter;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.CodeSwap;
import com.plmz.fl.http.Constant;
import com.plmz.fl.http.ParseResult;
import com.plmz.fl.http.URLClass;

import java.util.ArrayList;

public class MovieTabActivity extends Activity {
	private RelativeLayout title_layout;
	private AbHttpUtil mAbHttpUtil;
	private int position;
	private TextView back, movieTab;
	private PullToRefreshGridView xGridView_movieTab;
	private ArrayList<Movie> movieList;
	private ArrayList<Movie> newMovieList;
	private TuijianAdapter adapter;
	private int fromType = 0, currentPageIndex = 1, loadType = 1;
	private String[] movieTypeArr;
	private String[] areaArr = {"%CF%E3%B8%DB", "%CC%A8%CD%E5", "%B4%F3%C2%BD", "%C8%D5%B1%BE", "%BA%AB%B9%FA", "%C5%B7%C3%C0", "%CC%A9%B9%FA", "%D3%A1%B6%C8"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_tab);
		initView();
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		loadType = 1;
		fromType = getIntent().getIntExtra(Constant.FROM_TYPE, 0);
		position = getIntent().getIntExtra(Constant.MOVIE_TYPE, 0);
		switch (fromType) {
			case 0:
				setTitleAndParamsMovie();
				break;
			case 1:
				setTitleAndParamsDSJ();
				break;
			case 2:
				setTitleAndParamsDM();
				break;
			case 3:
				setTitleAndParamsTVB();
				break;
			case 4:
				setTitleAndParamsZY();
				break;
		}
		movieList = new ArrayList<Movie>();
		newMovieList = new ArrayList<Movie>();
		adapter = new TuijianAdapter(this,1, movieList);
		xGridView_movieTab.setAdapter(adapter);
		xGridView_movieTab.setMode(PullToRefreshBase.Mode.BOTH);
		xGridView_movieTab.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
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
				currentPageIndex = 1;
				newMovieList = new ArrayList<Movie>();
				loadType = 2;
				getData(position, loadType, currentPageIndex);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				//加载数据
				currentPageIndex++;
				newMovieList = new ArrayList<Movie>();
				loadType = 3;
				getData(position, loadType, currentPageIndex);
			}
		});
		getData(position, loadType, currentPageIndex);
	}

	private void initView() {
		title_layout = (RelativeLayout) findViewById(R.id.title_layout);
		back = (TextView) findViewById(R.id.back);
		movieTab = (TextView) findViewById(R.id.movieTab);
		xGridView_movieTab = (PullToRefreshGridView) findViewById(R.id.xGridView_movieTab);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	//电影
	private void setTitleAndParamsMovie() {
		title_layout.setVisibility(View.VISIBLE);
		String[] videoClass = getResources().getStringArray(R.array.video_small_class);
		movieTab.setText(videoClass[position]);
		movieTypeArr = getResources().getStringArray(R.array.movie_type);
	}

	//电视剧
	private void setTitleAndParamsDSJ() {
		title_layout.setVisibility(View.VISIBLE);
		String[] dsjClass = getResources().getStringArray(R.array.video_dianshiju_class);
		movieTab.setText(dsjClass[position]);
		movieTypeArr = new String[8];
		for (int i = 0; i < areaArr.length; i++) {
			movieTypeArr[i] = areaArr[i];
		}
	}

	//动漫
	private void setTitleAndParamsDM() {
		title_layout.setVisibility(View.GONE);
		String dmTitle = getResources().getString(R.string.dongman);
		movieTab.setText(dmTitle);
		movieTypeArr = getResources().getStringArray(R.array.dongmanparam);
	}

	//TVB
	private void setTitleAndParamsTVB() {
		title_layout.setVisibility(View.GONE);
		movieTypeArr = new String[1];
		movieTypeArr[0] = "_%s.html&page=%s";
	}

	//综艺
	private void setTitleAndParamsZY() {
		title_layout.setVisibility(View.GONE);
		movieTypeArr = getResources().getStringArray(R.array.zongyi);
	}

	private void getData(int position, final int loadType, int currentPageIndex) {
		String url = "";
		boolean isShowLoading = false;
		switch (loadType) {
			case 1:
				isShowLoading = true;
				switch (fromType) {
					case 0:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
					case 1:
						url = String.format(URLClass.DIANSHIJU_TAB_URL, movieTypeArr[position], currentPageIndex + "");
						break;
					case 2:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
					case 3:
						url = URLClass.TVB_URL;
						break;
					case 4:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
				}
				break;
			case 2:
				isShowLoading = false;
				switch (fromType) {
					case 0:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
					case 1:
						url = String.format(URLClass.DIANSHIJU_TAB_URL, movieTypeArr[position], currentPageIndex + "");
						break;
					case 2:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
					case 3:
						url = URLClass.TVB_URL + String.format(movieTypeArr[position], currentPageIndex + "", currentPageIndex + "");
						break;
					case 4:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position]);
						break;
				}
				break;
			case 3:
				isShowLoading = false;
				switch (fromType) {
					case 0:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position] + "_" + currentPageIndex);
						break;
					case 1:
						url = String.format(URLClass.DIANSHIJU_TAB_URL, movieTypeArr[position], currentPageIndex + "");
						break;
					case 2:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position] + "_" + currentPageIndex);
						break;
					case 3:
						url = URLClass.TVB_URL + String.format(movieTypeArr[position], currentPageIndex + "", currentPageIndex + "");
						break;
					case 4:
						url = String.format(URLClass.TAB_URL, movieTypeArr[position] + "_" + currentPageIndex);
						break;
				}
				break;
		}


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
						switch (loadType) {
							case 1:
								movieList.clear();
								movieList.addAll(newMovieList);
								adapter.notifyDataSetChanged();
								break;
							case 2:
								movieList.clear();
								if (newMovieList != null && newMovieList.size() > 0) {
									movieList.addAll(newMovieList);
									adapter.notifyDataSetChanged();
									newMovieList.clear();
								}
								break;
							case 3:
								if (newMovieList != null && newMovieList.size() > 0) {
									movieList.addAll(newMovieList);
									adapter.notifyDataSetChanged();
									newMovieList.clear();
								}
								break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(MovieTabActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				xGridView_movieTab.onRefreshComplete();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
								  Throwable error) {
				AbToastUtil.showToast(MovieTabActivity.this, error.getMessage());
				xGridView_movieTab.onRefreshComplete();
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
