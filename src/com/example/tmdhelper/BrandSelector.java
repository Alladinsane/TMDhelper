package com.example.tmdhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BrandSelector extends MainActivity implements OnClickListener{
	//this activity will ask the user to select which product lines will be used
	//in this build. This info will be used to determine which product arrays
	//need to be loaded to model this display
	//
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brand_selector_layout);
		
		hideActionBar();
		
		ListView selectBrand = (ListView)findViewById(R.id.add_list_view);

		setupBrandsListView(selectBrand);
		
		setOnClickListenerForButtons();
		
	}
	@Override
	public void onClick(View v) {
		int index = v.getId();
		
		switch(index)
		{
		case R.id.add_brands:
			
			addAllSelectedBrandsToBrandsArray();
			
			if(noBrandsWereSelected())
			{
				popNoBrandsSelectedToast();
        		break;
			}
			else
			{
			loadData(brands);
			startNextActivity();
			}
		}
	}
	
	public void setupBrandsListView(ListView selectBrand)
	{
		ArrayAdapter<String> adapt;
		adapt = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_multiple_choice,
				(getResources().getStringArray(R.array.brands)));
		selectBrand.setItemsCanFocus(false);
		selectBrand.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//allows user to make multiple selections
		registerForContextMenu(selectBrand);
		selectBrand.setAdapter(adapt);
	}
	public void setOnClickListenerForButtons()
	{
		int[] button_resources={R.id.add_brands};
		for (int i=0; i < button_resources.length; i++)
		{
			Button b = (Button)findViewById(button_resources[i]);
			b.setOnClickListener(this);
		}
	}
	public void addAllSelectedBrandsToBrandsArray()
	{
		ListView selectedBrands = (ListView)findViewById(R.id.add_list_view);
		
		brands.clear();
		int cntChoice = selectedBrands.getCount();
		String checked;
		SparseBooleanArray sparseBooleanArray = selectedBrands.getCheckedItemPositions();
		for(int i = 0; i < cntChoice; i++)
		{
			if(sparseBooleanArray.get(i) == true) 
			{
				checked = selectedBrands.getItemAtPosition(i).toString();
				brands.add(checked);
			}
		}
	}
	public boolean noBrandsWereSelected()
	{
		if(brands.size()==0)
			return true;
		else
			return false;
	}
	public void popNoBrandsSelectedToast()
	{
		Context context = getApplicationContext();
		CharSequence text = "Build must contain at least one product line.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
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
			startActivity(new Intent(BrandSelector.this, TMDmenu.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void startNextActivity()
	{
		Intent intent = new Intent(BrandSelector.this, DisplaySize.class);
		intent.putExtra("brands", brands);
		startActivity(intent);
	}
}