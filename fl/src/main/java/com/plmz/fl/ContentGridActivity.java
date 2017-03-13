package com.plmz.fl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.plmz.fl.http.ParseResult;
import com.plmz.fl.http.URLClass;
import com.plmz.fl.adapter.TuijianAdapter;
import com.plmz.fl.entity.Movie;
import com.plmz.fl.http.CodeSwap;
import com.plmz.fl.http.Constant;
import java.util.ArrayList;

public class ContentGridActivity extends AppCompatActivity {
	private int fromType;
	private TextView back;
	private PullToRefreshGridView xGistView_content;
	private AbHttpUtil mAbHttpUtil;
	private ArrayList<Movie> list;
	private ArrayList<Movie> newList;
	private TuijianAdapter adapter;
	private int currentIndex = 1;
	private String downloadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_grid);
		initView();
		fromType = getIntent().getIntExtra(Constant.FROM_TYPE,1);
		downloadUrl = getIntent().getStringExtra(Constant.XG_MOVIE_TYPE);
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		list = new ArrayList<Movie>();
		adapter = new TuijianAdapter(this,fromType, list);
		xGistView_content.setAdapter(adapter);
		xGistView_content.setMode(PullToRefreshBase.Mode.BOTH);
		xGistView_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(getTime());
				//加载数据
				currentIndex = 1;
				newList = new ArrayList<Movie>();
				list.clear();
				getData(2, currentIndex);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				currentIndex++;
				newList = new ArrayList<Movie>();
				getData(3, currentIndex);
			}
		});
		getData(1, currentIndex);
	}

	private void initView() {
		back = (TextView) findViewById(R.id.back);
		xGistView_content = (PullToRefreshGridView) findViewById(R.id.xGistView_content);
	}

	private String getTime() {
		String label = DateUtils.formatDateTime(
				getApplicationContext(),
				System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		return label;
	}


	private void getData(final int type, int pageIndex) {
		String url = "";
		if(fromType == 1){
			url = String.format(URLClass.XG_URL + downloadUrl+URLClass.XG_URL_PAGE, pageIndex + "");// +
		}else if(fromType == 2){
			if(pageIndex==1){
				url = String.format(URLClass.XF_URL + downloadUrl + URLClass.XF_URL_GROUP, "");
			}else{
				url = String.format(URLClass.XF_URL + downloadUrl + URLClass.XF_URL_GROUP, "_"+pageIndex);
			}
		}
		//获取Http工具类
		mAbHttpUtil.setEncode("utf-8");
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			//获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				switch (fromType){
					case 1:
						String str = CodeSwap.Unicode2GBK(content);
						newList = ParseResult.parseXG(str);
						break;
					case 2:
						newList = ParseResult.parseXF(content);
						break;
				}
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					adapter.notifyDataSetChanged();
					newList.clear();
				}
				xGistView_content.onRefreshComplete();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
								  Throwable error) {
				AbToastUtil.showToast(ContentGridActivity.this, error.getMessage());
				xGistView_content.onRefreshComplete();
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
