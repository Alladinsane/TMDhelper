package com.example.tmdhelper;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/*This class functions in the same way as FullTMD, except that LoproTMDs have only 4 shelves
 * 
 */
public class FullTMD extends TMDactivity implements OnClickListener{
	private int tmdTotal=0, multiple, loproTMD=0, fullTMD=0, counter=1;
	private final String TMDname="fullTMD";
	private final String TITLE="Full TMD";
	ArrayList<String> brands = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_tmd);
		
		tmdPrefs = getMySharedPreferences();
		brands = getIntent().getStringArrayListExtra("brands");

		initializePlanogram();
		super.setBrandsArray(brands);

		instantiateInstanceVariables();
		
		if(tmdTotal == 1)
		{
			setButtonToFinish();
		}
		setButtonOnClickListeners();
		
		counter=tmdPrefs.getInt("counter", 1);
		
		String thisTMD= TMDname + counter;
		
		if(planogramExists(thisTMD))
		{
			restorePlanogram(thisTMD);
		}
		setHeading();
		setContext(FullTMD.this);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.planogram, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.action_reset) {
			initializePlanogram();
			resetScreen();
			return true;
		}
		else if(id == R.id.action_restart)
		{
			showRestartAlertDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initializePlanogram()
	{
		planogram = new String[5];
		for(int i=0; i<planogram.length; i++)
		{
			planogram[i] = "empty";
		}
		super.setPlanogram(planogram);
	}
	public int[] getButtonResources()
	{
		int[] resources = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
				R.id.next_button, R.id.checkbox};
		return resources;
	}
	public void setButtonOnClickListeners()
	{
		resources = getButtonResources();
		for (int i=0; i <resources.length; i++)
		{
			Button b = (Button)findViewById(resources[i]);
			b.setOnClickListener(this);
		}
	}
	public int getShelfNumber(Button b)
	{
		int shelfNumber=-1;
		switch(b.getId())
		{
		case R.id.button1:
			shelfNumber=0;
			break;
		case R.id.button2:
			shelfNumber=1;
			break;
		case R.id.button3:
			shelfNumber=2;
			break;
		case R.id.button4:
			shelfNumber=3;
			break;
		case R.id.button5:
			shelfNumber=4;
			break;
		}
		return shelfNumber;
	}
	protected void storePlanogram()
	{
		String thisTMD = TMDname + counter;
		super.storePlanogram(thisTMD, planogram);
	}
	protected void nextButtonAction()
	{
		storePlanogram();
		
		Button b = getButtonByID(R.id.next_button);
		
		counter++;
		if(!(counter > fullTMD))
		{
			String thisTMD= TMDname + counter;
			if(counter < fullTMD)
			{
				b.setText("Next-->");

			}
			else if(counter==fullTMD)
			{
				if(loproTMD == 0)
					b.setText("Finish-->");
			}
			if(planogramExists(thisTMD))
				restorePlanogram(thisTMD);
			else
				resetScreen();
			setHeading();
		}
		else 
		{
			if(loproTMD>0)
			{
				//there are no more Full TMDs in this build, but Low Profile TMDs were selected
				Intent intent = new Intent(FullTMD.this, LoproTMD.class);
				intent.putExtra("brands", brands);
				startActivity(intent);
				finish();
			}
			else
			{
				//this build is done, and the results activity is called
				Intent intent = new Intent(FullTMD.this, Results.class);
				intent.putExtra("brands", brands);
				startActivity(intent);
				finish();
			}
		}
		
	}
	public void onBackPressed() {
		storePlanogram();
		counter--;
		Button b = (Button) findViewById(R.id.next_button);
		b.setText("Next-->");
		String thisTMD= TMDname + counter;
		if(counter>0)
		{
			restorePlanogram(thisTMD);
			setHeading();
		}
		else
		{
			super.onBackPressed();
			finish();
		}
	}
	public void instantiateInstanceVariables()
	{
		if(tmdPrefs.contains("fullTMD"))
		{
			fullTMD = tmdPrefs.getInt("fullTMD", 0);
		}
		if(tmdPrefs.contains("loproTMD"))
		{
			loproTMD = tmdPrefs.getInt("loproTMD", 0);
		}
		tmdTotal = fullTMD + loproTMD;
	}
	public void setHeading()
	{
		TextView header = (TextView) findViewById(R.id.TMDnumber);
		String heading = TITLE +" #" +  counter + " of " + fullTMD;
		header.setText(heading);
	}
	public void showRestartAlertDialog()
	{
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage(getResources().getString(R.string.restart_alert_message));
		builder1.setCancelable(true);
		builder1.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				Intent intent = new Intent(FullTMD.this, TMDmenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}});
		builder1.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				return;
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	
public Context getContext()
{
	return FullTMD.this;
}
public void executeMultipleTMDs(int multiple)
{
	this.multiple = multiple;
	new MultipleTMD().execute();
}


private class MultipleTMD extends AsyncTask <Void, Void, String>
{
	private ProgressDialog dialog;
	
	@Override
	protected void onPreExecute()
	{
		dialog = ProgressDialog.show(
				FullTMD.this,
				"Filling Multiple Towers",
				"Please wait...", 
				true);
	}

	@Override
	protected String doInBackground(Void... params)
	{
		Log.d("Mine", "applyMultiplePlanograms(" + multiple + ")");
		for(int i=1; i<multiple; i++)
		{
			storePlanogram();
			counter++;
		}

		return "";
	}

	@Override
	protected void onPostExecute(String result)
	{
		nextButtonAction();
		dialog.dismiss();
	}
}
public int getMaxValue()
{
	return ((fullTMD-counter)+1);
}
}