package com.donkka.planviewer;

import com.donkka.planviewer.connection.Worker;

import android.app.Activity;
import android.os.Bundle;

public class WorkerActivity extends Activity{
	Worker worker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		worker = new Worker();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		worker.finish();
	}
}
