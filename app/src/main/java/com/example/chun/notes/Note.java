package com.example.chun.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by per6 on 10/20/17.
 */

public class Note implements Parcelable {
    private StringBuffer name,content;
    private StringBuffer dateCreated, dateAccessed;
    private List<String> recentChanges;



    public Note(String name, String content) {
        this.name = new StringBuffer(name);
        this.content = new StringBuffer(content);
        SimpleDateFormat dF = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        Date date = new Date();
        dateCreated = new StringBuffer(dF.format(date));
        dateAccessed = null;
        recentChanges = new LinkedList();
        if (name.toString().equals("Untitled")){
            this.name.insert(this.name.capacity()-1,dateCreated.toString());

        }
    }

    public String toString(){
        return name+ "\n"+dateCreated;
    }

    public StringBuffer getName() {
        return name;
    }

    public void setName(StringBuffer name) {
        this.name = name;
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }

    public StringBuffer getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(StringBuffer dateCreated) {
        this.dateCreated = dateCreated;
    }

    public StringBuffer getDateAccessed() {
        return dateAccessed;
    }

    public void setDateAccessed(Date dateAccessed) {
        SimpleDateFormat dF = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        this.dateAccessed = new StringBuffer(dF.format(dateAccessed));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.name);
        dest.writeSerializable(this.content);
        dest.writeSerializable(this.dateCreated);
        dest.writeSerializable(this.dateAccessed);
        dest.writeStringList(this.recentChanges);
    }

    protected Note(Parcel in) {
        this.name = (StringBuffer) in.readSerializable();
        this.content = (StringBuffer) in.readSerializable();
        this.dateCreated = (StringBuffer) in.readSerializable();
        this.dateAccessed = (StringBuffer) in.readSerializable();
        this.recentChanges = in.createStringArrayList();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}