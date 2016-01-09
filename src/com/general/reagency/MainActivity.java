package com.general.reagency;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private TextView mTVTime = null; // 事件控件
	private TextView mTVCount = null; // 点击次数控件
	private TextView mTVTitle = null; // 标题，用于显示历史最高
	private Button mBtnStart = null; // 点击按钮
	SharedPreferences sp = null; // 存储数据
	Editor editor = null;
	protected final Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what)
			{
			case 2015:
				// maxCount();
				break;
			}
		}
	};
	private int sTime = 10; // 时间为10秒
	private int msTime = 0; // 时间毫秒
	private int count = 0; // 点击次数
	private int historyMaxCount = 0; // 历史最高次数

	private Runnable timeRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if (msTime == 0)
			{
				if (sTime == 0)
				{
					// 游戏结束
					updateUI();
					maxCount();
					btnState();
					Message message = new Message();
					message.what = 2015;

					handler.sendMessage(message);

					return;
				} else
				{
					sTime--;
					msTime = 99;
				}
			} else
			{
				msTime--;
			}

			updateUI();
			handler.postDelayed(timeRunnable, 10); // 每一毫秒执行一次
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData()
	{
		sp = this.getSharedPreferences("SP", MODE_PRIVATE);
		editor = sp.edit();
		mTVTime = (TextView) findViewById(R.id.m_time);
		mTVCount = (TextView) findViewById(R.id.m_count);
		mBtnStart = (Button) findViewById(R.id.m_btn_start);
		mTVTitle = (TextView) findViewById(R.id.m_title);
		mTVTime.setText(sTime + " 秒");
		mTVCount.setText("0 次");
		historyMaxCount = sp.getInt("MAX_COUNT", 0);
		maxCount();

		mBtnStart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 按钮点击事件
				if (10 == sTime)
				{
					handler.postDelayed(timeRunnable, 1); // 每一毫秒执行一次
				}

				count++;
				mTVCount.setText(count + " 次");
			}
		});
	}

	/**
	 * 更新ui
	 */
	private void updateUI()
	{
		String msStr = msTime + " 秒";
		if (msTime < 10)
		{
			msStr = "0" + msStr;
		}

		String strTime = sTime + "." + msStr;
		mTVTime.setText(strTime);
	}

	/**
	 * 历史最高次数存档
	 */
	private void maxCount()
	{

		if (historyMaxCount < count)
		{
			historyMaxCount = count;
			editor.putInt("MAX_COUNT", count);
			editor.commit();
		}
		mTVTitle.setText("历史最高 " + historyMaxCount + " 次");
		// btnState();
	}

	/**
	 * 按钮状态
	 */
	private void btnState()
	{
		mBtnStart.setEnabled(false);
		mBtnStart.setText(R.string.s_end);
		int time = 5000;
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				mBtnStart.setEnabled(true);
				mBtnStart.setText(R.string.s_btn_info);
				sTime = 10;
				msTime = 0;
				count = 0;
			}
		}, time);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
		}

		return false;
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
