package com.example.chun.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private Note note;
    private EditText contentText, nameText;
    public static final String TAG = "NoteActivity";
    public static final int OK = 2;
    public static final int CANCELLED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        wireWidgets();

        Intent i = getIntent();//gets intent
        note = i.getParcelableExtra(MainActivity.EXTRA_NOTE);//gets note from intent
        note.setDateAccessed(new Date());
        nameText.setText(note.getName());
        contentText.setText(note.getContent());
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: activity paused");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (contentText.getText() != null) {
            Intent i = new Intent(NoteActivity.this, MainActivity.class);
            i.putExtra("Note", note);
            setResult(OK, i);
        } else setResult(CANCELLED);
        super.onDestroy();
    }

    private void wireWidgets() {
        nameText = findViewById(R.id.edittext_nametext);
        contentText = findViewById(R.id.edittext_notetext);
    }
}

