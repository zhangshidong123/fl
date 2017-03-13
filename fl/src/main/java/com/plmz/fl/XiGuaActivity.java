package com.plmz.fl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.plmz.fl.http.Constant;
import java.util.ArrayList;

public class XiGuaActivity extends AppCompatActivity {
	private Intent intent;
	private TextView back;
	private ListView xListView_xg;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	private String [] names;
	private String [] links;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xi_gua);
		initView();
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
		xListView_xg.setAdapter(adapter);
		xListView_xg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intent = new Intent(XiGuaActivity.this,ContentGridActivity.class);
				intent.putExtra(Constant.FROM_TYPE,1);
				intent.putExtra(Constant.XG_MOVIE_TYPE,links[position]);
				startActivity(intent);
			}
		});
		getData();
	}
	private void initView(){
		back = (TextView)findViewById(R.id.back);
		xListView_xg = (ListView) findViewById(R.id.xListView_xg);
	}

	private void getData(){
		list.clear();
		names = getResources().getStringArray(R.array.xg_main_name);
		links = getResources().getStringArray(R.array.xg_main_link);
		for (int i=0;i<names.length;i++){
			list.add(names[i]);
		}
		adapter.notifyDataSetChanged();
	}
}
