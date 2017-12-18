package com.example.chun.notes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "note";
    public static final String TAG = "MainActivity";
    public static final int NEW_NOTE_REQUEST = 1;
    private List<String> notesTitles;
    private List<Note> notesList;
    private FloatingActionButton addNewNoteButton;
    private ListView notesListView;
    private ArrayAdapter<Note> notesAdapter;
    private Note newNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
        notesTitles = new ArrayList<>();
        notesList = new ArrayList<>();
        Log.d(TAG, "onCreate: file directory exists " + !fileExist("stringdirectory_ja7abmy663g87dk.txt"));
        if (!fileExist("stringdirectory_ja7abmy663g87dk.txt")) {
            fileNoExist("stringdirectory_ja7abmy663g87dk.txt", R.raw.stringdirectory_ja7abmy663g87dk);
        }
        Log.d(TAG, "onCreate: sample note exists " + !fileExist("sample_dec_06_2017_0149_pm.txt"));
        if (!fileExist("sample_dec_06_2017_0149_pm.txt")) {
            fileNoExist("sample_dec_06_2017_0149_pm.txt", R.raw.sample_dec_06_2017_0149_pm);
        }


        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(notesAdapter);

        notesListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {

                Intent i = new Intent(MainActivity.this, NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE, notesList.get(pos));//puts note into extra
                startActivityForResult(i, 1);//start activity i

            }
        });
        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NoteActivity.class);//create intent
                i.putExtra(EXTRA_NOTE, new Note("Untitled", ""));
                startActivityForResult(i, 1);
            }
        });
    }

    private void fileNoExist(String name, int res) {
        String noteJson;
        try {
            InputStream is = this.getResources().openRawResource(res);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((noteJson = bufferedReader.readLine()) != null) {
                sb.append(noteJson);
            }
            noteJson = sb.toString();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(name, Context.MODE_PRIVATE));
            Log.d(TAG, "fileNoExist: " + name + "  ");

            outputStreamWriter.write(noteJson);
            outputStreamWriter.close();
            Log.d(TAG, "fileNoExist: " + noteJson);

        } catch (IOException e) {
            Log.e("Exception", "File No Exist File write failed: " + e.toString());
        }
    }

    private void fillNotesList() {
        for (String n : notesTitles) {
            Gson gson = new Gson();
            String text;
            try {
                String yourFilePath = this.getFilesDir() + "/" + n + ".txt";
                File yourFile = new File(yourFilePath);
                FileInputStream inputStream = new FileInputStream(yourFile);

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    text = stringBuilder.toString();
                    if (text.equals("") == false) {
                        Note noteFromJson = gson.fromJson(text, Note.class);
                        Log.d(TAG, "fillNotesList: " + noteFromJson);
                        notesList.add(noteFromJson);
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "fillNotesList File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            } catch (NullPointerException e) {
                Log.e(TAG, "fillNotesList: " + e.toString());
            }
        }
    }

    @Override
    protected void onPause() {
        saveFileDirectory();
        super.onPause();
    }

    private void saveFileDirectory() {
        Gson gson = new Gson();
        String noteJson = gson.toJson(notesTitles);
        try {
            String yourFilePath = this.getFilesDir() + "/" + "stringdirectory_ja7abmy663g87dk.txt";
            File yourFile = new File(yourFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(yourFile);
            fileOutputStream.write(noteJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.d(TAG, "onPause: " + noteJson);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    @Override
    protected void onResume() {
        notesTitles = readFromFile("stringdirectory_ja7abmy663g87dk.txt", this);
        Log.d(TAG, "onResume: titles in notesTitles" + notesTitles.toString());
        //notesAdapter.clear();
        notesList.clear();
        Log.d(TAG, "onResume: notes list after clear " + notesList);
        fillNotesList();
        if (newNote != null) {
            if (!notesTitles.contains(newNote.getTitle().toString())) {
                notesTitles.add(newNote.getTitle().toString());
                notesList.add(newNote);
                newNote = null;
            }
        }
        //check if newNote is not null and add to the list, then set newNote back to null
        notesAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(notesAdapter);
//        notesAdapter.addAll(notesList);
        // notesAdapter.notifyDataSetChanged();
//        new LoadNotesTask().execute();
        Log.d(TAG, "onResume: " + notesList.toString());
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: result code " + resultCode + " request code " + requestCode + " data " + data);
        if (resultCode == RESULT_OK) {
            Note n = data.getParcelableExtra("Note");
            Log.d(TAG, "onActivityResult: if statements ResultCodeOK " + (!notesTitles.contains(n.getTitle().toString())));
            Log.d(TAG, "onActivityResult: titles in notesTitles[] " + n.getTitle().toString() + " " + notesTitles);
            if (!notesTitles.contains(n.getTitle().toString())) {
                notesTitles.add(n.getTitle().toString());
                //notesList.add(n);
                newNote = n;
                Log.d(TAG, "onActivityResult: noteslist contents " + notesList.toString());
                //writeToFile(n,this);
                //notesAdapter.notifyDataSetChanged();
                //saveFileDirectory();
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    private void wireWidgets() {
        addNewNoteButton = findViewById(R.id.floatingActionButton_new_note);
        notesListView = findViewById(R.id.listview_notes);
    }

    private void writeToFile(Note a, Context context) {
        Gson gson = new Gson();
        String noteJson = gson.toJson(a);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(a.getTitle() + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(noteJson);
            Log.d(TAG, "writeToFile: " + noteJson);
            outputStreamWriter.close();
            notesTitles.clear();
            fillNotesList();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private ArrayList readFromFile(String name, Context context) {
        Gson gson = new Gson();
        String text = "";

        try {
            String yourFilePath = context.getFilesDir() + "/" + name;
            File yourFile = new File(yourFilePath);
            InputStream inputStream = new FileInputStream(yourFile);
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                text = stringBuilder.toString();
                Log.d(TAG, "readFromFile: " + text);
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "readFromFile File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        ArrayList<String> notes = (ArrayList<String>) gson.fromJson(text, List.class);
        return notes;
    }

//    public boolean fileExist(String fname){
//        File file = getBaseContext().getFileStreamPath(fname);
//        return file.exists();
//    }

    public boolean fileExist(String fileName) {
        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
        Log.d(TAG, "fileExist: " + path);
        File file = new File(path);
        return file.exists();
    }


    private class LoadNotesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            fillNotesList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateAdapter();
        }
    }

    private void updateAdapter() {
        notesAdapter.clear();
        notesAdapter.addAll(notesList);
        notesAdapter.notifyDataSetChanged();
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
        Collections.sort(notesList, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                return note.getName().toString().compareToIgnoreCase(t1.getName().toString());
            }
        });
        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "Sorted by name");
    }

    private void sortByCreated() {

        Collections.sort(notesList, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                return (note.getDateCreated().compareTo(t1.getDateCreated())) * (-1);
            }
        });
        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "Sorted by date created");
    }

    private void sortByAccessed() {
        Collections.sort(notesList, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                return (note.getDateAccessed().compareTo(t1.getDateAccessed())) * (-1);
            }
        });
        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "Sorted by date created");
    }
}
