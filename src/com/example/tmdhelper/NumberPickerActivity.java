package com.example.tmdhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NumberPickerActivity extends MainActivity implements OnClickListener
{
	android.widget.NumberPicker numberPickerWidget;
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.number_picker);
	  hideActionBar();
	  
	  setButtonListeners();
	  
	  setupNumberPickerWidget();
   }
  
  
  public void setupNumberPickerWidget()
  {
	  numberPickerWidget = (android.widget.NumberPicker)findViewById(R.id.number_picker);
	  int max = getIntent().getIntExtra("maxValue", 1);

	  numberPickerWidget.setMinValue(1);
	  numberPickerWidget.setMaxValue(max);
	  numberPickerWidget.setWrapSelectorWheel(true); 

	  numberPickerWidget.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() 
	  {
		  public void onValueChange(android.widget.NumberPicker picker, int oldVal,
				  int newVal) {
			  // TODO Auto-generated method stub
		  }
	  });
  }
@Override
public void onClick(View v) {
	int index = v.getId(); 
	
	if(index==R.id.apply_button)
	{
		int value = numberPickerWidget.getValue();
		
		Log.d("Mine", "value = " +value);
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", value);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
}
public void setButtonListeners()
{
	int[] resources = {R.id.apply_button};
	  
	  for (int i=0; i <resources.length; i++)
	  {
		  Button b = (Button)findViewById(resources[i]);
		  b.setOnClickListener(this);
	  }
}

}/* NumberPickerActivity */