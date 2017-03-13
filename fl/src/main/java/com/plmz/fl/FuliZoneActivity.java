package com.plmz.fl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class FuliZoneActivity extends AppCompatActivity {
	private TextView back;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuli_zone);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void goXG(View view) {
		intent = new Intent(this, XiGuaActivity.class);
		startActivity(intent);
	}

	public void goXF(View view) {
		intent = new Intent(this, XianFengActivity.class);
		startActivity(intent);
	}
}
