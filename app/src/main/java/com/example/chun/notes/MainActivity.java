package com.example.chun.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> notes;
    private FloatingActionButton newNoteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
    }

    private void wireWidgets() {
        newNoteButton=(FloatingActionButton)findViewById(R.id.floatingActionButton_new_note);
    }
}
