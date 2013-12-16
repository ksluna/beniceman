package com.beniceman.mathfacts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
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
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View.OnClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.beniceman.mathfacts.util.CommonIce;

public class Activity_Computation extends Activity {
	private String numberDisplayed = "";
	private EditText result;
	private static final int RESULT_SETTINGS = 1;
	private String mUserId;
	private String mDomain = "ADD";
	private int mResult = 0;
	private String mReturnedResult;
	private ResultTask mResultTask = null;

	Button enterButton, clearButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.computation);
		// Show the Up button in the action bar.
		setupActionBar();

		SharedPreferences sharedPreferences = getSharedPreferences("prefs", 0);
		mUserId = sharedPreferences.getString("userid", "");

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
					try {
						if (calculate(Integer.parseInt(first),
								Integer.parseInt(second),
								Integer.parseInt(result))) {
							score++;
							tvScore.setText(String.valueOf(score));
							tvScore.setTextColor(getResources().getColor(
									R.color.green_good));
						} else {
							score--;
							tvScore.setText(String.valueOf(score));
							tvScore.setTextColor(Color.RED);
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

	private void updateResultField() {
		result.setText(this.numberDisplayed);
	}

	/*
	 * Calculate the variables
	 */
	public boolean calculate(int first, int second, int answer)
			throws NoSuchAlgorithmException {
		boolean ret = false;
		int total = first + second;
		if (total == answer) {
			ret = true;
			mResult = 1;
		} else {
			mResult = -1;
		}
		clearAll();
		attemptResult();
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

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public void attemptResult() throws NoSuchAlgorithmException {
		if (mResultTask != null) {
			return;
		}
		// Use if errors found
		// boolean cancel = true;
		if (mUserId != null || mUserId != "") {
			mResultTask = new ResultTask();
			mResultTask.execute((Void) null);
		}
	}

	/*
	 * Represents an asynchronous total task used to update user result.
	 */
	public class ResultTask extends AsyncTask<Void, Void, Boolean> {
		String mURL = getString(R.string.ben_iceman_base_url)
				+ getString(R.string.ben_iceman_set_result_service);
		String error = "";
		String domanin = mDomain;
		String userid = mUserId;

		@Override
		protected Boolean doInBackground(Void... params) {

			return callWebService();
			// Process Results

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mResultTask = null;

			if (success) {
				this.SavePreferences("addresult", mReturnedResult);
			}
			// else {
			// mPasswordView.setError("WTF");
			// mPasswordView.requestFocus();
			// }
		}

		@Override
		protected void onCancelled() {
			mResultTask = null;
		}

		public Boolean callWebService() {
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;

			try {

				URL url = new URL(mURL + "?domain=" + mDomain + "&result="
						+ mResult + "&userid=" + mUserId);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				InputStream is = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = conn.getInputStream();
				} else {
					InputStream err = conn.getErrorStream();
					error = err.toString();
					return false;
				}
				try {

					br = new BufferedReader(new InputStreamReader(is));
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							error += e.getMessage();
							return false;

						}
					}
				}
				mReturnedResult = sb.toString();
				conn.disconnect();

			} catch (MalformedURLException e) {
				error += e.getMessage();
				return false;
			} catch (IOException e) {
				error += e.getMessage();
				return false;
			}
			return true;
		} // end callWebService()

		private void SavePreferences(String key, String value) {
			SharedPreferences sharedPreferences = getSharedPreferences("prefs",
					0);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
			
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor2 = sharedPrefs.edit();
			editor2.putString("addingtotal", value);
			editor2.commit();
			
		}
	}

}
