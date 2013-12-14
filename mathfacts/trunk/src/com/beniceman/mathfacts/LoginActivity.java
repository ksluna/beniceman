package com.beniceman.mathfacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.beniceman.mathfacts.util.CommonIce;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well. Initially created using android sdk template
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail = "";
	private String mPassword = "";
	private String mDisplayName = "";

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mDisplayNameView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private TextView mLoginMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);

		if (mEmail.length() > 0) {
			mEmailView.setText(mEmail);
		} else {
			mEmailView.setText(sharedPrefs.getString("prefUsername", ""));
		}
		mDisplayNameView = (EditText) findViewById(R.id.displayname);
		mDisplayNameView.setText(mDisplayName);

		mPasswordView = (EditText) findViewById(R.id.password);

		if (mPassword.length() > 0) {
			mPasswordView.setText(mPassword);
		} else {
			mPasswordView
					.setText(sharedPrefs.getString("prefPasswordname", ""));
		}

		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							try {
								attemptLogin();
							} catch (NoSuchAlgorithmException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		mLoginMessageView = (TextView) findViewById(R.id.login_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							attemptLogin();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_change_password:
			Intent intent = new Intent(this, ChangePasswordActivity.class);
			this.startActivity(intent);
			break;
		case R.id.action_forgot_password:
			// Intent intent = new Intent(this, ForgotPasswordActivity.class);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public void attemptLogin() throws NoSuchAlgorithmException {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mDisplayNameView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPassword = CommonIce.hashIt(mPassword);
		mDisplayName = mDisplayNameView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		String mURL = getString(R.string.ben_iceman_base_url)
				+ getString(R.string.ben_iceman_login_service);
		String result = "";
		String error = "";
		String p = mPassword;
		String email = mEmail;
		String displayName = mDisplayName;

		@Override
		protected Boolean doInBackground(Void... params) {

			return callWebService();
			// Process Results

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				decider();
				// finish();
			} else {
				mPasswordView.setError("WTF");
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		public Boolean callWebService() {
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;

			try {

				URL url = new URL(mURL + "?p=" + mPassword + "&email=" + mEmail
						+ "&displayname=" + mDisplayName);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				InputStream is = null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = conn.getInputStream();
				} else {
					InputStream err = conn.getErrorStream();
					error = err.toString();
					result = "0";
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
							result = "0";
							return false;

						}
					}
				}
				result = sb.toString();
				conn.disconnect();

			} catch (MalformedURLException e) {
				error += e.getMessage();
				result = "0";
				return false;
			} catch (IOException e) {
				error += e.getMessage();
				result = "0";
				return false;
			}
			return true;
		} // end callWebService()

		public void decider() {
			String[] response_array = result.split(",");
			int indicator = 0;
			if (response_array != null) {
				indicator = Integer.parseInt(response_array[0]);
			}

			String username = "";
			if (response_array.length > 1) {
				username = response_array[1];
			}

			String userid = "";
			if (response_array.length > 2) {
				userid = response_array[2];
			}
			String message = "";
			switch (indicator) {
			case 1:// Account is locked
				message = "Account is locked!";
				mLoginMessageView.setText(message);
				break;
			case 2:// Login successful Welcome back
				message = "Welcome back, " + username + "!";
				mLoginMessageView.setText(message);
				SavePreferences("userid",userid);
				// setResult(0,'ADD');
				break;
			case 3:// email found password incorrect
				message = "Incorrect password, try again.";
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
				mLoginMessageView.setText(message);
				break;
			case 4:// Try again we need a display name
				message = "Please enter a display name.";
				mDisplayNameView.setError("Enter a Display Name");
				mDisplayNameView.requestFocus();
				mLoginMessageView.setText(message);
				break;
			case 5:// Inserted new record Welcome
				message = "Welcome, " + username + "!";
				mLoginMessageView.setText(message);
				SavePreferences("userid",userid);
				// setResult(0,'ADD');
				break;
			case 8:// Could not find user
				message = "Could Not Find User!";
				mLoginMessageView.setText(message);
				break;
			default:
				message = "What happened there!.";
				break;
			}
		}

		private void SavePreferences(String key, String value) {
			SharedPreferences sharedPreferences = getSharedPreferences("prefs",0);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
			
		}

	}
}
