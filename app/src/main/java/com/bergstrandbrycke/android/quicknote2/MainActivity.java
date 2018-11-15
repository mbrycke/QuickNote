package com.bergstrandbrycke.android.quicknote2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText note;
    String askWhichAppToSendTo;
    private static final int SPEECH_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Load settings
        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        String settingsAreSet = mPrefs.getString("settingsAreSet", "");
        askWhichAppToSendTo = mPrefs.getString("askWhichAppToSendTo","askWhichAppToSendTo_noInfo");
        //password = mPrefs.getString("password","password_not_stored");


        note = (EditText) findViewById(R.id.id_note);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Toast.makeText(getApplication(), "settingsAreSet_value:"+settingsAreSet, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplication(), "askWhichAppToSendTo:"+askWhichAppToSendTo, Toast.LENGTH_LONG).show();


        if(settingsAreSet.length()>0) {
            displaySpeechRecognizer();
        }else{
            goToSettings();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        goToSettings();

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false); // In dictation mode speechRecognizer would still call onPartialResults() however you should treat the partials as final results.
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, Long.valueOf(10000));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start speaking...");
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
        //Test showing partial results i.e. realtime
       /*
        List<String> results = intent.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);
        String spokenText = results.get(0);
        note.setText(spokenText, TextView.BufferType.EDITABLE);
        */
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK)
        {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            note.setText(spokenText, TextView.BufferType.EDITABLE);
            //Try to send text
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, currentDateTimeString);
            sendIntent.putExtra(Intent.EXTRA_TEXT, spokenText);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void goToSettings(){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }


}
