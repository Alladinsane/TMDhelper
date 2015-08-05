package com.example.tmdhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TMDmenu extends MainActivity {
	
	MyDatabaseAdapter myDatabaseAdapter;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		hideActionBar();
		myDatabaseAdapter = getDatabaseAdapter();
		wipeDatabase();

		ListView menuList = (ListView) findViewById(R.id.menu_list);

		setupMenu(menuList);
		listenForAndHandleMenuClicks(menuList);
	}

	public void setupMenu(ListView menuList)
	{
		String[] items = { getResources().getString(R.string.menu_item_start)};
		//, getResources().getString(R.string.menu_item_edit)};

		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, 
				R.layout.menu_item, items);
		menuList.setAdapter(adapt);

	}
	public void listenForAndHandleMenuClicks(ListView menuList)
	{
		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				TextView textview = (TextView) itemClicked;
				String strText = textview.getText().toString();
				if (strText.equalsIgnoreCase(getResources().getString(
						R.string.menu_item_start))) {
					//this menu button launches the first activity in the Display 
					//modeling process
					startActivity(new Intent(TMDmenu.this, BrandSelector.class));
				}
				else if (strText.equalsIgnoreCase(getResources().getString(
						R.string.menu_item_edit))) {
					//this button accesses the activities for user editing of
					//array of products as well as individual product arrays
					startActivity(new Intent(TMDmenu.this, ProductEditor.class));
				}
			}
		});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_reset) {
			return true;
		}
		else if(id == R.id.action_restart)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
