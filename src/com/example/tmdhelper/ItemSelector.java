package com.example.tmdhelper;

import java.util.ArrayList;
import java.util.StringTokenizer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ItemSelector extends MainActivity {
	
	ArrayList<String> itemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_selector);
		hideActionBar();
		
		loadItemArray();
		
		listenForAndHandleClickEvents();
	}
	
	public void listenForAndHandleClickEvents()
	{
		ListView selectItem = (ListView)findViewById(R.id.item_list_view);
		setupListView(selectItem);
		
		selectItem.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			//the user has selected an item from the ListView
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				TextView textview = (TextView) itemClicked;
				
				String itemSelected = textview.getText().toString();
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", itemSelected);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
			});
	}
	public void setupListView(ListView selectItem)
	{
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(
                this, 
                android.R.layout.simple_list_item_1,
                itemArray );

        selectItem.setAdapter(adapt); 
		selectItem.setItemsCanFocus(false);
		selectItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//allows user to make multiple selections
		registerForContextMenu(selectItem);
		selectItem.setAdapter(adapt);
	}
	public void loadItemArray()
	{
		//generates a ListView from the array of brands selected for use in this build
		ArrayList<String> brands = getIntent().getStringArrayListExtra("brands");
		Log.d("Mine", "ItemSelector brands = " + brands);
		itemArray = new ArrayList<String>();
		
		for(int i=0; i<brands.size(); i++)
		{
			String selectedBrand = brands.get(i).toString();
			addThisBrandToArray(selectedBrand);
		}
		addEmptyBrandToArray();
	}
	public void addThisBrandToArray(String selectedBrand)
	{
		int id = this.getResources().getIdentifier(selectedBrand, "array", this.getPackageName());
		String[] tempArray = getResources().getStringArray(id);
		for(int j=0; j<tempArray.length; j++)
		{
			String temp = tempArray[j].toString();
			Log.d("Mine", "temp = " + temp);
			StringTokenizer st = new StringTokenizer(temp);
			
			String item = st.nextToken();
			itemArray.add(item);
		}
	}
	public void addEmptyBrandToArray()
	{
		itemArray.add("empty");
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
			wipeDatabase();
			startActivity(new Intent(ItemSelector.this, TMDmenu.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
