package com.example.chun.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        notes.add(new Note("Sample B","This is a sample note."));
        notes.add(new Note("Sample A","This is a sample note."));

        notesAdapter=new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,notes);
        notesListView.setAdapter(notesAdapter);
        registerForContextMenu(notesListView);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //case R.id.menu_sort:
            case R.id.sort_by_accessed:
                sortByAccessed();
                return true;
            case R.id.sort_by_created:
                sortByCreated();
                return true;
            case R.id.sort_by_name:
                sortByName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






    private void sortByName() {
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                return note.getName().toString().compareToIgnoreCase(t1.getName().toString());
            }
        });
        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "Sorted by name");
    }
    private void sortByCreated() {
//        Collections.sort(notes, new Comparator<Note>() {
//            @Override
//            public int compare(Note note, Note t1) {
//                return note.getDateCreated().compareTo(t1.getDateCreated());
//            }
//        });
//        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "Sorted by date created");
    }
    private void sortByAccessed() {
//        boolean noNull=true;
//        for (Note note:notes){
//            if (note.getDateAccessed()==null){noNull = false;}
//        }
//        if (noNull) {
            Collections.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return note.getDateAccessed().
                    Log.d(TAG, "compare: ");
                }
            });
            notesAdapter.notifyDataSetChanged();
            Log.d(TAG, "Sorted by last accessed");
            
        }
//    }

}
