package com.example.chun.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    private Note note;
    private TextView nameText;
    private EditText contentText;
    public static final String TAG="NoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        wireWidgets();

        Intent i = getIntent();//gets intent
        note=i.getParcelableExtra(MainActivity.EXTRA_NOTE);//gets note from intent

        Log.d(TAG, "onCreate: note Content received");//logs
        Log.d(TAG, note.getName()+note.getContent());//logs

        nameText.setText(note.getName());
        contentText.setText(note.getContent());

    }

    private void wireWidgets() {
        nameText=(TextView) findViewById(R.id.textview_notename);
        contentText=(EditText) findViewById(R.id.edittext_notetext);
    }
}
