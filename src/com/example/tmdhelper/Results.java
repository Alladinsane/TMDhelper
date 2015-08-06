package com.example.tmdhelper;

import java.util.ArrayList;
import java.util.StringTokenizer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Results extends MainActivity implements OnClickListener {

	ArrayList<String> productsUsedInThisBuild;
	ArrayList<String> results = new ArrayList<String>();
	ArrayList<String> allItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		allItems = getAllItems();
		loadButtonResources();
		
		productsUsedInThisBuild = loadItemArray();
		processRawData();
		
		displayResults();
	}
	
	
	public ArrayList<String> loadItemArray()
	{
		ArrayList<String> brands = getIntent().getStringArrayListExtra("brands");
		ArrayList<String> itemArray = new ArrayList<String>();
		//pulls out the name of each item, and adds it to an itemArray
		for(int i=0; i<brands.size(); i++)
		{
			String myBrand = brands.get(i).toString();
			int id = this.getResources().getIdentifier(myBrand, "array", this.getPackageName());
			String[] tempArray = getResources().getStringArray(id);
			for(int j=0; j<tempArray.length; j++)
			{
				String temp = tempArray[j].toString();
				StringTokenizer st = new StringTokenizer(temp);
				
				String item = st.nextToken();
				itemArray.add(item);
			}
		}
		brands.clear();
		return itemArray;
	}
	public void loadButtonResources()
	{
		int[] resources = {R.id.start_over, R.id.exit};
		for (int i=0; i <resources.length; i++)
		{
			Button b = (Button)findViewById(resources[i]);
			b.setOnClickListener(this);
		}
	}
	public void processRawData()
	{
		ArrayList<String> itemTallies = new ArrayList<String>();
		
		for(int i=0; i<productsUsedInThisBuild.size(); i++)
		{
			String itemName = productsUsedInThisBuild.get(i);
			int count = totalShelvesUsingThisProduct(itemName);
			
			if(count == 0)
			{
				continue;
			}
			
			String item = itemName + " " + count;
			itemTallies.add(item);
		}
		calculateResults(itemTallies);
		allItems.clear();
	}
	public int totalShelvesUsingThisProduct(String thisProduct)
	{
		int count = 0;
		for(int j=0; j<allItems.size(); j++)
		{
			String productOnCurrentShelf = allItems.get(j);
			if(thisProduct.equals(productOnCurrentShelf))
			{
				count++;
			}
		}
		return count;
	}
	public void calculateResults(ArrayList<String> itemTallies)
	{
		for(int i=0; i<itemTallies.size(); i++)
		{
			
			String item = itemTallies.get(i);
			StringTokenizer st = new StringTokenizer(item);
			int tally = 0;
			String itemName = " ";
			while(st.hasMoreTokens())
			{
				itemName = st.nextToken();
				tally = Integer.parseInt(st.nextToken());
			}
			int caseCount = calculateCaseCount(itemName, tally);
			addToResults(itemName, caseCount);
		}
	}
	public int calculateCaseCount(String itemName, int shelves)
	{
		int totalCases=0;
		int caseCount=0;
		int shelfCount=0;	
		String productData = getProductData(itemName);
		StringTokenizer st = new StringTokenizer(productData);
		while(st.hasMoreTokens())
		{
			//unpack product data from array 
			st.nextToken();
			st.nextToken();
			caseCount = Integer.parseInt(st.nextToken());
			shelfCount = Integer.parseInt(st.nextToken());	
		}
		int eaches = shelfCount * shelves;//the total number of shelves to be filled by this 
		//item multiplied by the number of bags of this item required to fill a shelf
		int mod = 0;
		if(eaches%caseCount>0)//if total needed bags is not evenly divisible by caseCount
			mod = 1;          //an extra case will be needed to completely fill
		totalCases = eaches/caseCount + mod;
		mod = 0;
		return totalCases;
	}
	public void addToResults(String itemName, int total)
	{
		String phrase = itemName + ": " + total + " cases.";
		results.add(phrase);
	}
	
	public void displayResults()
	{
		ListView selectItem = (ListView)findViewById(R.id.results_view);

		ArrayAdapter<String> adapt = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1,
				results );
		selectItem.setAdapter(adapt); 
		selectItem.setItemsCanFocus(false);
		selectItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		registerForContextMenu(selectItem);
		selectItem.setAdapter(adapt);

	}
	@Override
	public void onClick(View v) {
		int index = v.getId();
		
		if(index== R.id.start_over)
		{
			clearUserData();
			startOver();
		}
	}
	public void clearUserData()
	{
		wipeDatabase();
		results.clear();
		productsUsedInThisBuild.clear();
	}
	public void startOver()
	{
		Intent intent = new Intent(Results.this, TMDmenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
	public void onBackPressed()
	{
		tmdPrefs = getMySharedPreferences();
		int loproTMD=0;
		if(tmdPrefs.contains("loproTMD"))
		{
			loproTMD = tmdPrefs.getInt("loproTMD", 0);
		}
		if(loproTMD>0)
		{
			SharedPreferences.Editor prefEditor = tmdPrefs.edit();
			prefEditor.putInt("counter", loproTMD);
			prefEditor.commit();
			startActivity(new Intent(Results.this, LoproTMD.class));
		}
		else
		{
			int fullTMD = tmdPrefs.getInt("fullTMD", 0);
			SharedPreferences.Editor prefEditor = tmdPrefs.edit();
			prefEditor.putInt("counter", fullTMD);
			prefEditor.commit();
			startActivity(new Intent(Results.this, FullTMD.class));
		}
		finish();
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
			startActivity(new Intent(Results.this, TMDmenu.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
