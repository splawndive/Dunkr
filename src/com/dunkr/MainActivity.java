package com.dunkr;

import com.example.dunkr.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener, View.OnClickListener {

	private TextView tv;
	private Button count_btn;
	
	//vars used by the countdown timer
	private CountDownTimer timer;
	private int countdown = 5000;
	private final int interval = 1000;
	private boolean isCounting = false;
	private float max_accel;
	
	
	//vars used by the accelerometer
	private long lastUpdate;
	private final float alpha = (float) 0.8;
	private float prev_x, prev_y, prev_z, max_accelx, max_accely, max_accelz = (float) 0.0;
	
	Sensor senAccelerometer;
	SensorManager senSensorManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.maintext);
		count_btn = (Button) findViewById(R.id.countdown_button);
		this.timer = new CountDownTimer(this.countdown,this.interval){
			
			@Override
			public void onTick(long mills_left){
				count_btn.setText(Long.toString(mills_left/1000));
			}
			
			@Override
			public void onFinish(){
				
				count_btn.setText("Reset Countdown!");
				isCounting = false;
				
				if(max_accelx >= max_accely && max_accelx >= max_accelz) max_accel = max_accelx;
				if(max_accely >= max_accelx && max_accely >= max_accelz) max_accel = max_accely;
				if(max_accelz >= max_accelx && max_accelz >= max_accely) max_accel = max_accelz;
				
				tv.setText(Float.toString(max_accel));
				
			}
			
		};
		
		
		tv.setText("0,0,0");
		count_btn.setText("Begin CountDown!");
		count_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isCounting = true;
				timer.start();
			}
		});
	
		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
	    
	} //end of oncreate method
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		
		if(isCounting){
			Sensor sensor = sensorEvent.sensor;
			float[] gravity = new float[3];
			float[] linear_acceleration = new float[3];
			
			if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				float x = sensorEvent.values[0];
				float y = sensorEvent.values[1];
				float z = sensorEvent.values[2];
				
				long currTime = System.currentTimeMillis();
				
				gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
		        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
		        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;
		
		        linear_acceleration[0] = x - gravity[0];
		        linear_acceleration[1] = y - gravity[1];
		        linear_acceleration[2] = z - gravity[2];
		        
		        if(linear_acceleration[0] > max_accelx) max_accelx = linear_acceleration[0];
		        if(linear_acceleration[1] > max_accely) max_accely = linear_acceleration[1];
		        if(linear_acceleration[2] > max_accelz) max_accelz = linear_acceleration[2];
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
