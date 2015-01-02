package com.devcab.piredux;

//Import android OS features
import android.app.Application;
import android.os.Bundle;

//Import android Application features
import android.app.Activity;
import android.app.AlertDialog;

//Import Android View Features
import android.preference.PreferenceManager;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

//Import android intent features
import android.content.Intent;

//Import android content features
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

//Import Button
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

//Import RadioGroup
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class resizeSettings extends Activity
{
	
	//Global variable for choice selection
    //private String aspectRatioSelected = "CHECKED";

    //variables for initial values of width and height, in case user cancels
    private int initialWidth = 0;
    private int initialHeight = 0;

    //layout elements
    private EditText customWidthTextBox, customHeightTextBox;
    private TextView seekBarValue;
    private RelativeLayout widthHeightLayout;
    private SeekBar seekBar;
    private CheckBox aspectCheckBox;
    private RadioGroup selectedSize;
    private RadioButton mobileRadio, tabletRadio, HDRadio, customRadio;
    private Button okayClicked;

	//Global variable Intent
    private Intent selectedAcceptedSize;
	
	//Global final int
    private final int MAX_WIDTH_HEIGHT = 5001;
    private final int MIN_WIDTH_HEIGHT = 200; //todo find acceptable value
		
	protected void onCreate(Bundle savedInstanceState)
	{
		//create Bundle package (required before anything else)
		super.onCreate(savedInstanceState);
		//display Content View (required before anything else)
		setContentView(R.layout.resize_layout);

        //initialize layout elements
        widthHeightLayout = (RelativeLayout)findViewById(R.id.widthHeightLayout);
        seekBarValue = (TextView) findViewById(R.id.qualityText);
        customWidthTextBox = (EditText)findViewById(R.id.customWidth);
        customHeightTextBox = (EditText)findViewById(R.id.customHeight);
        aspectCheckBox = (CheckBox)findViewById(R.id.checkboxAspectRatio);
        okayClicked = (Button)findViewById(R.id.btnOK);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        //screenSizeText = (TextView)findViewById(R.id.screenSizeText);

        selectedSize = (RadioGroup)findViewById(R.id.SizeGroupButtons);
        mobileRadio = (RadioButton)findViewById(R.id.mobileDeviceSizeRadio);
        tabletRadio = (RadioButton)findViewById(R.id.tabletSizeRadio);
        HDRadio = (RadioButton)findViewById(R.id.largeSizeRadio);
        customRadio = (RadioButton)findViewById(R.id.customSizeRadio);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();

        int height = settings.getInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_HD_HEIGHT);
        int width = settings.getInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_HD_WIDTH);
        initializeSeekBar();



        //set initial values of displays
        seekBar.setProgress(settings.getInt(MainActivity.SETTING_QUALITY, MainActivity.DEFAULT_QUALITY));
        aspectCheckBox.setChecked(settings.getBoolean(MainActivity.SETTING_ASPECT_RATIO, true));

        //get set values, and display them as necessary
        if( height == MainActivity.BUTTON_MOBILE_HEIGHT &&
            width == MainActivity.BUTTON_MOBILE_WIDTH)
        {
            findSetRadio(R.id.mobileDeviceSizeRadio);
        }
        else if(height == MainActivity.BUTTON_TABLET_HEIGHT &&
                width == MainActivity.BUTTON_TABLET_WIDTH)
        {
            findSetRadio(R.id.tabletSizeRadio);
        }
        else if(height == MainActivity.BUTTON_HD_HEIGHT &&
                width == MainActivity.BUTTON_HD_WIDTH)
        {
            findSetRadio(R.id.largeSizeRadio);
        }
        else
        {
            findSetRadio(R.id.customSizeRadio);

        }

        aspectCheckBox.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox box = (CheckBox) v;
                //is aspectCheckBox checked
                if (box.isChecked())
                {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putBoolean(MainActivity.SETTING_ASPECT_RATIO, true);
                    editor.apply();
                }
                else
                {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putBoolean(MainActivity.SETTING_ASPECT_RATIO, false);
                    editor.apply();
                }
            }
        });

        selectedSize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findSetRadio(checkedId);
            }
        });

        okayClicked.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettings();
            }
        });
	}

    private void findSetRadio(int id)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();

        RadioButton button = (RadioButton)findViewById(id);
        button.setChecked(true);

        customHeightTextBox = (EditText)findViewById(R.id.customHeight);
        customWidthTextBox = (EditText)findViewById(R.id.customWidth);

        switch (id) {
            case R.id.mobileDeviceSizeRadio:
                editor.putInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_MOBILE_HEIGHT);
                editor.putInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_MOBILE_WIDTH);
                editor.apply();
                setVisibleForCustom(false);
                break;
            case R.id.tabletSizeRadio:
                editor.putInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_TABLET_HEIGHT);
                editor.putInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_TABLET_WIDTH);
                editor.apply();
                setVisibleForCustom(false);
                break;
            case R.id.largeSizeRadio:
                editor.putInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_HD_HEIGHT);
                editor.putInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_HD_WIDTH);
                editor.apply();
                setVisibleForCustom(false);
                break;
            case R.id.customSizeRadio:
                setVisibleForCustom(true);
                customHeightTextBox.setText(String.valueOf(settings.getInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_HD_HEIGHT)));
                customWidthTextBox.setText(String.valueOf(settings.getInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_HD_WIDTH)));
                break;
        }
    }
	
	public void initializeSeekBar()
	{
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        int progress = settings.getInt(MainActivity.SETTING_QUALITY, MainActivity.DEFAULT_QUALITY);

		seekBar.setProgress(0);
		seekBar.incrementProgressBy(50);
		seekBar.setMax(150);
        seekBar.setKeyProgressIncrement(10);
        seekBarValue.setText("Quality: " + String.valueOf(progress));
        
        int quality = settings.getInt(MainActivity.SETTING_QUALITY, 50);
        seekBar.setProgress(quality);
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
		        //why?
		        /*progress = progress / 50;
		        progress = progress * 50;*/
		        seekBarValue.setText("Quality: " + String.valueOf(progress));

                editor.putInt(MainActivity.SETTING_QUALITY, progress);
                editor.apply();


		        /*if(progress >= 0 && progress <= 49)
		        {
		        	qualityCompressValue = 50;
		        }
		        else if(progress >= 50 && progress <= 64)
		        {
		        	qualityCompressValue = 65;
		        }
		        else if(progress >= 65 && progress <= 100)
		        {
		        	qualityCompressValue = 80;
		        }
		        else
		        {
		        	qualityCompressValue = 80;
		        }*/
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar)
		    {
		    }

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    }
		});
	}
	
	public void setVisibleForCustom(boolean option)
	{
		if(option){
            widthHeightLayout.setVisibility(View.VISIBLE);
        }
		else{
            widthHeightLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	public void onBackPressed() {
        setSettings();
    }

    private void setSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        selectedAcceptedSize = new Intent();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        int selectedWidth = settings.getInt(MainActivity.SETTING_WIDTH, MainActivity.BUTTON_HD_WIDTH);
        int selectedHeight = settings.getInt(MainActivity.SETTING_HEIGHT, MainActivity.BUTTON_HD_HEIGHT);

        if (selectedWidth > MIN_WIDTH_HEIGHT && selectedWidth < MAX_WIDTH_HEIGHT && selectedHeight > MIN_WIDTH_HEIGHT && selectedHeight < MAX_WIDTH_HEIGHT) {
            setResult(RESULT_OK, selectedAcceptedSize);
            finish();
        }
        else
        {
            //set title
            alertDialogBuilder.setTitle("Error!");
            //set dialog message
            alertDialogBuilder
                    .setMessage("Width and height must be between " + MIN_WIDTH_HEIGHT + " and " + MAX_WIDTH_HEIGHT)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancel dialog
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }
}