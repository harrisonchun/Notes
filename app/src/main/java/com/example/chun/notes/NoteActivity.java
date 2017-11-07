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
    private EditText contentText,nameText;
    public static final String TAG="NoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        wireWidgets();

        Intent i = getIntent();//gets intent
        note=i.getParcelableExtra(MainActivity.EXTRA_NOTE);//gets note from intent
        note.setDateAccessed(new Date());
        note.setName(new StringBuffer("untitled"+note.getDateCreated()));
        nameText.setText(note.getName());
        contentText.setText(note.getContent());
        contentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                writeToFile("" + charSequence,NoteActivity.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                note.setName(new StringBuffer(nameText.getText()));
            }
        });
    }



    private void wireWidgets() {
        nameText= findViewById(R.id.edittext_nametext);
        contentText=findViewById(R.id.edittext_notetext);
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(note.getName()+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
