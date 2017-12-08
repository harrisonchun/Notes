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
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE="note";
    public static final String TAG="MainActivity";
    public static final int NEW_NOTE_REQUEST = 1;
    private List<String> notes;
    private List<Note> notesList;
    private FloatingActionButton addNewNoteButton;
    private ListView notesListView;
    private ArrayAdapter<Note> notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
        notes = new ArrayList<>();
        notesList = new ArrayList<>();
        Note SampleNote = new Note("Sample","" + "This is a sample note.");
        //SampleNote.setTitle();
        notes = readFromFile("stringdirectory_ja7abmy663g87dk.txt",this);
        //notesList.add(SampleNote);
        //notes.add(SampleNote.getTitle().toString());
        fillNotesList();

        notesAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,notesList);
        notesListView.setAdapter(notesAdapter);

        notesListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {

                Intent i = new Intent(MainActivity.this,NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE,notesList.get(pos));//puts note into extra

//                Log.d(TAG, "onItemClick: notes position = "+pos);//logs
//                Log.d(TAG, notes.get(pos).getContent()+notes.get(pos).getName());//logs

                startActivityForResult(i,1);//start activity i

            }
        });
        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE, new Note("Untitled" ,""));
                startActivityForResult(i, 1);
            }
        });
    }

    private void fillNotesList() {
        for (String n : notes){
            Gson gson = new Gson();
            String text;
            try {
                InputStream inputStream = this.openFileInput(n+".txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    text = stringBuilder.toString();
                    if (text.equals("")==false){
                        Note noteFromJson = gson.fromJson(text,Note.class);
                        notesList.add(noteFromJson);
                    }
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            catch (NullPointerException e) {
                Log.e(TAG, "fillNotesList: " + e.toString());
            }
        }
    }


    @Override
    protected void onDestroy() {
        Gson gson = new Gson();
        String noteJson = gson.toJson(notes);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("stringdirectory_ja7abmy663g87dk.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(noteJson);
            outputStreamWriter.close();
            Log.d(TAG, "onDestroy: "+noteJson);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: result code "+resultCode + " request code "+ requestCode + " data "+data);
        if (resultCode == RESULT_OK){
            Note n = data.getParcelableExtra("Note");
            writeToFile(n,this);
            if (!notes.contains(n.getTitle().toString())){
            notes.add(n.getTitle().toString());
            Log.d(TAG, "onActivityResult: "+notes.add(n.getTitle().toString()));
            }
            notesAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void wireWidgets() {
        addNewNoteButton=findViewById(R.id.floatingActionButton_new_note);
        notesListView=findViewById(R.id.listview_notes);
    }
    private void writeToFile(Note a, Context context) {
        Gson gson = new Gson();
        String noteJson = gson.toJson(a);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(a.getTitle()+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(noteJson);
            Log.d(TAG, "writeToFile: "+noteJson);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private ArrayList readFromFile(String name, Context context) {
    //private String readFromFile(String name, Context context) {
        Gson gson = new Gson();
        String text = "";

        try {
            InputStream inputStream = context.openFileInput("stringdirectory_ja7aIlbmy663G87dk.txt");

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
        ArrayList<String> notes = (ArrayList<String>) gson.fromJson(text,List.class);
        //return text;
        return notes;
    }
}
