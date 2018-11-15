package com.bergstrandbrycke.android.quicknote2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;

/**
 * Created by Mattias on 2017-02-07.
 */

public class SettingsActivity  extends AppCompatActivity
{

    CheckBox askWhichApp;
    Boolean isChecked;
    String askWhichAppToSendTo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        askWhichApp = (CheckBox) findViewById(R.id.checkBox);
        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        String settingsAreSet = mPrefs.getString("settingsAreSet", "");
        askWhichAppToSendTo = mPrefs.getString("askWhichAppToSendTo","askWhichAppToSendTo_noInfo");
        if(askWhichAppToSendTo == "true"){
            askWhichApp.setChecked(true);
        }

        if(askWhichApp.isChecked()) {
            saveTonApplicationData("true");
        }else{
            saveTonApplicationData("false");
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        /*actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);*/




    }


    public void saveTonApplicationData(String askWhichAppToSendTo)
    {
        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("askWhisAppToSendTo", askWhichAppToSendTo).commit();

    }


}
