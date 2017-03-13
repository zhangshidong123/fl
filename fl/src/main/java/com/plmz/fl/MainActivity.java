package com.plmz.fl;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmz.fl.http.Constant;
import com.plmz.fl.myview.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * http://42.121.90.220:9101/UrlProxy.ashx?u=http://www.dhfun.cn/list.php*cat=0$year=$lang=$area=$actor=$state=$order=1$page=1
 */
public class MainActivity extends ActivityGroup {
	private LinearLayout linearLayout;
	private MyViewPager mViewPager;
	private MyPagerAdapter myPagerAdapter;
	private HorizontalScrollView horizontalScrollView;
	private List<String> mTitleList;//页卡标题集合
	private View[] views;//页卡视图
	private ArrayList<View> mViewList;//页卡视图集合
	private EditText et;
	private Intent intent;
	private ArrayList<TextView> textViews;
	private int H_width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		mTitleList = new ArrayList<>();
		mViewList = new ArrayList<>();
		getClassInfo();
		InItTitle();
		setSelector(0);
		InItChildViews();
		myPagerAdapter = new MyPagerAdapter(mViewList);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.clearAnimation();
		mViewPager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				setSelector(arg0);
			}

			public void onPageScrolled(int arg0,
									   float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = et.getText().toString();
				if (str.equals("1024")) {
					intent = new Intent(MainActivity.this, XianFengActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	private void initView() {
		et = (EditText) findViewById(R.id.et);
		mViewPager = (MyViewPager) findViewById(R.id.vp_view);
		linearLayout = (LinearLayout) findViewById(R.id.ll_main);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
	}

	private void getClassInfo() {
		String[] classArr = getResources().getStringArray(R.array.video_class);
		for (String str : classArr) {
			mTitleList.add(str);
		}
	}

	/***
	 * init title
	 */
	// 根据luntan.xml公告的高确定textHeight的高度
	void InItTitle() {
		textViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		H_width = width / 5;
		float scale = this.getResources().getDisplayMetrics().density;
		int textheight = 0;
		if (width > 719) {
			textheight = (int) (50 * scale + 0.5f);// 高分辨率为公告50
		} else if (width > 479 && width < 720) {
			textheight = (int) (50 * scale + 0.5f);
		} else if (width > 319 && width < 480) {
			textheight = (int) (35 * scale + 0.5f);
		}
		for (int i = 0; i < mTitleList.size(); i++) {
			TextView textView = new TextView(this);
			textView.setText(mTitleList.get(i));
			textView.setTextSize(14);
			textView.setTextColor(Color.BLACK);
			textView.setLayoutParams(new LinearLayout.LayoutParams(H_width,
					textheight));
			textView.setGravity(Gravity.CENTER);
			textView.setSingleLine(true);
			textView.setId(i);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelector(v.getId());
				}
			});
			textViews.add(textView);
			linearLayout.addView(textView);
		}
	}

	/***
	 * 选中效果
	 */
	public void setSelector(int id) {
		for (int i = 0; i < mTitleList.size(); i++) {
			if (id == i) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.mipmap.titlebg);
				textViews.get(id).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				textViews.get(id).setTextColor(Color.WHITE);
				if (i > 2) {
					horizontalScrollView.smoothScrollTo((textViews.get(i)
							.getWidth() * i - 270), 0);
				} else {
					horizontalScrollView.smoothScrollTo(0, 0);
				}
				mViewPager.setCurrentItem(i);
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.mipmap.titlebgtwo);
				textViews.get(i).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				textViews.get(i).setTextColor(Color.BLACK);
			}
		}
	}

	private void InItChildViews() {
		mViewList = new ArrayList<View>();
		for (int i = 0; i < mTitleList.size(); i++) {
			TextView tv = new TextView(MainActivity.this);
			mViewList.add(tv);
		}
	}

	public void search(View view) {
		String str = et.getText().toString();
		if (str.equals("1024")) {
			intent = new Intent(MainActivity.this, XianFengActivity.class);
			startActivity(intent);
		}
	}


	class MyPagerAdapter extends PagerAdapter {
		private ArrayList<View> myPageViews;

		public MyPagerAdapter(ArrayList<View> pageViews) {
			this.myPageViews = pageViews;
		}

		// 显示数目
		@Override
		public int getCount() {
			return mViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// @Override
		// public int getItemPosition(Object object) {
		// return super.getItemPosition(object);
		// }

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((MyViewPager) arg0).removeView(myPageViews.get(arg1));
		}

		/***
		 * 获取每一个item， 类于listview中的getview
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			mViewList.remove(arg1);
			if (arg1 == 0 || arg1 == 1) {
				Intent intent = new Intent(MainActivity.this,
						TabOneActivity.class);
				intent.putExtra("activityId", arg1);
				View view = getLocalActivityManager().startActivity(
						"activity" + arg1, intent).getDecorView();
				mViewList.add(arg1, view);
			}
			if (arg1 == 2 || arg1 == 3 || arg1 == 4) {
				Intent intent = new Intent(MainActivity.this,
						MovieTabActivity.class);
				intent.putExtra("activityId", arg1);
				intent.putExtra(Constant.FROM_TYPE, arg1);
				View view = getLocalActivityManager().startActivity(
						"activity" + arg1, intent).getDecorView();
				mViewList.add(arg1, view);
			}
			((MyViewPager) arg0).addView(myPageViews.get(arg1));
			return myPageViews.get(arg1);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(android.os.Parcelable state, ClassLoader loader) {

		}

		;

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}
}
