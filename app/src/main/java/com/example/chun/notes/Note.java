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
    private Date dateCreated, dateAccessed;
    private List<String> recentChanges;
    private StringBuffer title;


    public Note(String name, String content) {
        this.name = new StringBuffer(name);
        this.content = new StringBuffer(content);
        SimpleDateFormat dF = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
        dateCreated = new Date();
        dateAccessed = null;
        recentChanges = new LinkedList();
        if (name.toString().equals("Untitled")){
            this.name.insert(this.name.length()," "+ new StringBuffer(dF.format(dateCreated)).toString());
            title = new StringBuffer(this.name.toString().toLowerCase().replace(":","").replace(" ","_").replace(",",""));
        }
    }

    public String toString(){
        return name+ "\n"+dateCreated;
    }

    public StringBuffer getName() {
        return name;
    }

    public void setTitle() {
        SimpleDateFormat dF = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
        title = new StringBuffer(new StringBuffer(name).insert(this.name.length()," "+ new StringBuffer(dF.format(dateCreated)).toString()).toString().toLowerCase().replace(":","").replace(" ","_"));
    }

    public StringBuffer getTitle() {
        return title;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateAccessed() {
        return dateAccessed;
    }

    public void setDateAccessed(Date dateAccessed) {
        this.dateAccessed=dateAccessed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.name);
        dest.writeSerializable(this.title);
        dest.writeSerializable(this.content);
        dest.writeSerializable(this.dateCreated);
        dest.writeSerializable(this.dateAccessed);
        dest.writeStringList(this.recentChanges);
    }

    protected Note(Parcel in) {
        this.name = (StringBuffer) in.readSerializable();
        this.title = (StringBuffer) in.readSerializable();
        this.content = (StringBuffer) in.readSerializable();
        this.dateCreated = (Date) in.readSerializable();
        this.dateAccessed = (Date) in.readSerializable();
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