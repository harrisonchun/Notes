package com.example.chun.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE="note";
    public static final String TAG="MainActivity";
    public static final int NEW_NOTE_REQUEST = 1;
    private List<Note> notes;
    private FloatingActionButton addNewNoteButton;
    private ListView notesListView;
    private ArrayAdapter<Note> notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
        notes=new ArrayList<Note>();
        notes.add(new Note("Sample","This is a sample note."));

        notesAdapter=new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,notes);
        notesListView.setAdapter(notesAdapter);

        notesListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {

                Intent i = new Intent(MainActivity.this,NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE,notes.get(pos));//puts note into extra

//                Log.d(TAG, "onItemClick: notes position = "+pos);//logs
//                Log.d(TAG, notes.get(pos).getContent()+notes.get(pos).getName());//logs

                startActivity(i);//start activity i

            }
        });
        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE, new Note("Untitled " ,""));
                startActivityForResult(i, NEW_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NoteActivity.OK){
            Note n = data.getParcelableExtra("Note");
            notes.add(n);
            notesAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void wireWidgets() {
        addNewNoteButton=(FloatingActionButton)findViewById(R.id.floatingActionButton_new_note);
        notesListView=(ListView)findViewById(R.id.listview_notes);
    }


}
