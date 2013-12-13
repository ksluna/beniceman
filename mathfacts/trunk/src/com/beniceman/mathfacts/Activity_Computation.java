package com.beniceman.mathfacts;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View.OnClickListener;
import java.util.Random;

public class Activity_Computation extends Activity {
	private String numberDisplayed = "";
	private EditText result;
	private static final int RESULT_SETTINGS = 1;

	Button enterButton, clearButton;

	private void updateResultField() {
		result.setText(this.numberDisplayed);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.computation);
		// Show the Up button in the action bar.
		setupActionBar();

		if (savedInstanceState != null) {
			String score = savedInstanceState.getString("Score");
			TextView tvScore = (TextView) findViewById(R.id.score);
			tvScore.setText(score);
		}
		result = (EditText) findViewById(R.id.result);

		// Numbers
		final Button button0 = (Button) findViewById(R.id.buttonzero);
		button0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "0";
				updateResultField();
			}
		});
		final Button button1 = (Button) findViewById(R.id.buttonone);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "1";
				updateResultField();
			}
		});
		final Button button2 = (Button) findViewById(R.id.buttontwo);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "2";
				updateResultField();
			}
		});
		final Button button3 = (Button) findViewById(R.id.buttonthree);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "3";
				updateResultField();
			}
		});
		final Button button4 = (Button) findViewById(R.id.buttonfour);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "4";
				updateResultField();
			}
		});
		final Button button5 = (Button) findViewById(R.id.buttonfive);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "5";
				updateResultField();
			}
		});
		final Button button6 = (Button) findViewById(R.id.buttonsix);
		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "6";
				updateResultField();
			}
		});
		final Button button7 = (Button) findViewById(R.id.buttonseven);
		button7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "7";
				updateResultField();
			}
		});
		final Button button8 = (Button) findViewById(R.id.buttoneight);
		button8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "8";
				updateResultField();
			}
		});
		final Button button9 = (Button) findViewById(R.id.buttonnine);
		button9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				numberDisplayed += "9";
				updateResultField();
			}
		});

		// Enter Button
		enterButton = (Button) findViewById(R.id.enterButton);
		enterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPrefs = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				int min = Integer.parseInt(sharedPrefs.getString("lowerRange",
						"1"));
				int max = Integer.parseInt(sharedPrefs.getString("upperRange",
						"120"));

				TextView tvFirst = (TextView) findViewById(R.id.first);
				String first = tvFirst.getText().toString();
				TextView tvSecond = (TextView) findViewById(R.id.second);
				String second = tvSecond.getText().toString();
				EditText etResult = (EditText) findViewById(R.id.result);
				String result = etResult.getText().toString();
				TextView tvScore = (TextView) findViewById(R.id.score);
				int score = Integer.parseInt(tvScore.getText().toString());
				if (!result.equals("")) {
					if (calculate(Integer.parseInt(first),
							Integer.parseInt(second), Integer.parseInt(result))) {
						score++;
						tvScore.setText(String.valueOf(score));
						tvScore.setTextColor(getResources().getColor(
								R.color.green_good));
					} else {
						score--;
						tvScore.setText(String.valueOf(score));
						tvScore.setTextColor(Color.RED);
					}
				} else {
					score--;
					tvScore.setText(String.valueOf(score));
					tvScore.setTextColor(Color.RED);
				}
				int firstRand = randInt(min, max);
				tvFirst.setText(String.valueOf(firstRand));
				int secondRand = randInt(min, max);
				tvSecond.setText(String.valueOf(secondRand));

			}
		});

		// Clear Button
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAll();
			}
		});
		// Cancel keyboard popup on touch of result field
		result.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		TextView tvScore = (TextView) findViewById(R.id.score);
		savedInstanceState.putString("Score", tvScore.getText().toString());
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		case R.id.menu_settings:
			i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		case R.id.menu_about:
			i = new Intent(this, About.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;

		}

		return true;
	}

	/*
	 * Calculate the variables
	 */
	public boolean calculate(int first, int second, int answer) {
		boolean ret = false;
		int total = first + second;
		if (total == answer) {
			ret = true;
		}
		clearAll();
		return ret;
	}

	// clear result and numberDisplayed
	public void clearAll() {
		result.setText("");
		this.numberDisplayed = "";
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimim value
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
