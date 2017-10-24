package com.example.chun.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> notes;
    private FloatingActionButton addNewNoteButton;
    private ListView notesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wireWidgets();
        ListAdapter notesAdapter=new ArrayAdapter<Note>(this,android.R.layout.simple_list_item_1,notes);
        notesListView.setAdapter(notesAdapter);
        notesListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {

                //extract the name, desc, and id of the one you clicked on

                //make intent to open the new detail activity, putting extras
//                Intent i = new Intent(MainActivity.this,adsfadsf.class);
//
//                startActivity(i);
            }
        });

    }

    private void wireWidgets() {
        addNewNoteButton=(FloatingActionButton)findViewById(R.id.floatingActionButton_new_note);
        notesListView=(ListView)findViewById(R.id.listview_notes);
    }
}
