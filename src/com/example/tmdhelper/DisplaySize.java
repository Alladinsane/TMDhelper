package com.example.tmdhelper;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DisplaySize extends MainActivity implements OnClickListener{
	//This Activity will gather user input to determine how many TMDs will be built
	private int fullTMD, loproTMD, defaultEntry=0;
	SharedPreferences tmdPrefs;
	private EditText fullInput, loproInput;
	MyDatabaseAdapter myDatabaseAdapter;
	ArrayList<String> brands;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_size_layout);
		hideActionBar();

		loadRelevantData();
		findViews();

		final Button next = (Button) findViewById(R.id.next_button);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				gatherUserInput();
				launchNextActivity();

			}
		});
	}


	public void loadRelevantData()
	{
		tmdPrefs = getMySharedPreferences();
		myDatabaseAdapter = getDatabaseAdapter();
		brands = getIntent().getStringArrayListExtra("brands");
	}
	public void findViews()
	{
		fullInput = (EditText) findViewById(R.id.fullTMD_view);
		loproInput = (EditText) findViewById(R.id.lopro_view);
	}
	public void launchNextActivity()
	{
		if(fullTMD>0)
		{
			Intent intent = new Intent(DisplaySize.this, FullTMD.class);
			intent.putExtra("brands", brands);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(DisplaySize.this, LoproTMD.class);
			intent.putExtra("brands", brands);
			startActivity(intent);
		}
	}



	public void gatherUserInput()
	{
		if(userEnteredNothingInThisField(fullInput))
			fullTMD = defaultEntry;                               
		else
			fullTMD = getThisInput(fullInput);

		if(userEnteredNothingInThisField(loproInput))
			loproTMD = defaultEntry;
		else
			loproTMD = getThisInput(loproInput);

		if(userMakesNoSelections())
			showBuildMustContainTMDsToast();
		else
			storeDataInSharedPreferences();
	}
	public boolean userEnteredNothingInThisField(EditText thisField)
	{
		if(thisField.getText().toString().matches(""))
			return true;
		else
			return false;
	}
	public int getThisInput(EditText thisField)
	{
		int input = Integer.parseInt(thisField.getText().toString());
		return input;
	}
	public boolean userMakesNoSelections()
	{
		if(fullTMD + loproTMD <= 0)
			return true;
		else
			return false;
	}
	public void showBuildMustContainTMDsToast()
	{
		Context context = getApplicationContext();
		CharSequence text = "Build must contain at least one TMD.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	public void storeDataInSharedPreferences()
	{
		SharedPreferences.Editor prefEditor = tmdPrefs.edit();
		prefEditor.putInt("fullTMD", fullTMD);
		prefEditor.putInt("loproTMD", loproTMD);
		prefEditor.putInt("counter", 1);
		prefEditor.commit();
	}



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
		if (id == R.id.action_restart)
		{
			brands.clear();
			startActivity(new Intent(DisplaySize.this, TMDmenu.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
