package com.example.chun.notes;

import android.app.Activity;
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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class NoteActivity extends AppCompatActivity{
    private Note note;
    private EditText contentText, nameText;
    public static final String TAG = "NoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        wireWidgets();

        Intent i = getIntent();//gets intent
        note = i.getParcelableExtra(MainActivity.EXTRA_NOTE);//gets note name from intent
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
        note.setContent(new StringBuffer(contentText.getText().toString()));
        note.setName(new StringBuffer(nameText.getText().toString()));
        writeToFile(note,this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        note.setContent(new StringBuffer(contentText.getText().toString()));
        note.setName(new StringBuffer(nameText.getText().toString()));
        Intent i = getIntent();
        if (nameText.getText().toString().length()>30){
            note.setName(new StringBuffer(nameText.getText().toString().substring(0,29)));
        }
        if (contentText.getText().toString().length()>0) {
            Log.d(TAG, "onPause: "+note);
            i.putExtra("Note", note);
            if (getParent() == null) {
                setResult(Activity.RESULT_OK,i);
                Log.d(TAG, "onPause: " + (getParent()==null));
            }
            else {
                getParent().setResult(Activity.RESULT_OK,i);
            }
            writeToFile(note, NoteActivity.this);
            finish();
        } else {
            Log.d(TAG, "onPause: else statement");
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void writeToFile(Note a, Context context) {
        Gson gson = new Gson();
        String noteJson = gson.toJson(a);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(a.getTitle()+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(noteJson);
            outputStreamWriter.close();
            Log.d(TAG, "writeToFile: write successful: "  + noteJson);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

