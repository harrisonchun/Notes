package com.example.chun.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private void wireWidgets() {
        nameText = findViewById(R.id.edittext_nametext);
        contentText = findViewById(R.id.edittext_notetext);
    }
}

