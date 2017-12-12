package com.example.chun.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

public class NoteActivity extends AppCompatActivity{
    private Note note;
    private EditText contentText, nameText;
    public static final String TAG = "NoteActivity";
    public static final int OK = 2;
    public static final int CANCELLED = 3;
// yeetx4
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        wireWidgets();

        Intent i = getIntent();//gets intent
        String name = i.getStringExtra(MainActivity.EXTRA_NOTE);//gets note name from intent
        if (readFromFile(name,this)!= null)
        note = readFromFile(name, this);
        else note = new Note("","");
        note.setDateAccessed(new Date());
        nameText.setText(note.getName());
        contentText.setText(note.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    private void wireWidgets() {
        nameText = findViewById(R.id.edittext_nametext);
        contentText = findViewById(R.id.edittext_notetext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.italics_button:
                italic();
                return true;
            case R.id.bold_button:
                bold();
                return true;
            case R.id.underline_button:
                underline();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bold(){
        int startSelection = contentText.getSelectionStart();
        int endSelection = contentText.getSelectionEnd();
        if (startSelection == endSelection) {contentText.setTypeface(Typeface.DEFAULT_BOLD);}
        else{
        SpannableString contentString = SpannableString.valueOf(contentText.getText());
        contentString.setSpan(new StyleSpan(Typeface.BOLD), startSelection, endSelection, 0);
        contentText.setText(contentString);
        contentText.setSelection(endSelection   );}
    }

    private void italic(){
        int startSelection = contentText.getSelectionStart();
        int endSelection = contentText.getSelectionEnd();
        SpannableString contentString = SpannableString.valueOf(contentText.getText());
        contentString.setSpan(new StyleSpan(Typeface.ITALIC), startSelection, endSelection, 0);
        contentText.setText(contentString);
        contentText.setSelection(endSelection   );
    }

    private void underline(){
        int startSelection = contentText.getSelectionStart();
        int endSelection = contentText.getSelectionEnd();
        SpannableString contentString = SpannableString.valueOf(contentText.getText());
        contentString.setSpan(new UnderlineSpan(), startSelection, endSelection, 0);
        contentText.setText(contentString);
        contentText.setSelection(endSelection   );
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: activity paused");
        if (contentText.getText() != null)
        writeToFile(note, this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (nameText.getText().length()>30){
            note.setName(new StringBuffer(nameText.getText().toString().substring(0,29)));
        }
        if (contentText.getText() != null) {
            Intent i = new Intent(NoteActivity.this, MainActivity.class);
            i.putExtra("Note", note);
            setResult(OK, i);
        } else setResult(CANCELLED);
        writeToFile(note, this);
        super.onDestroy();
    }

    private void writeToFile(Note a, Context context) {
        Gson gson = new Gson();
        String noteJson = gson.toJson(a);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(a.getName()+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(noteJson);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private Note readFromFile(String name, Context context) {
        Gson gson = new Gson();
        String text = "";

        try {
            InputStream inputStream = context.openFileInput(name+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                text = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Note noteJson = gson.fromJson(text,Note.class);

        return noteJson;
    }
}

