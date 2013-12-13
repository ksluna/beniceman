package com.beniceman.mathfacts;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;


public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Handler x = new Handler();
		x.postDelayed(new SplashHandler(), 1000);
	}

	class SplashHandler implements Runnable{
    	public void run(){
    		startActivity(new Intent(getApplication(), Activity_Computation.class));
    		Splash.this.finish();
    	}
    }

}
