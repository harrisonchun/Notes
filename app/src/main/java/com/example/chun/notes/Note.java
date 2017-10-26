package com.example.chun.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by per6 on 10/20/17.
 */

public class Note implements Parcelable {
    private String name,content;
    private Date dateCreated, dateAccessed;
    private List<String> recentChanges;


    public Note(String name, String content) {
        this.name = name;
        this.content = content;
        dateCreated = new Date();
        dateAccessed = null;
        recentChanges = new LinkedList();
    }

    public String toString(){
        return "Created: "+dateCreated+", Content: "+content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateAccessed() {
        return dateAccessed;
    }

    public void setDateAccesed(Date dateAccessed) {
        this.dateAccessed = dateAccessed;
    }

    public String newChange(String change){
        recentChanges.add(change);
        if (recentChanges.size()>20){
            return recentChanges.remove(0);
        }
        return null;
    }


    protected Note(Parcel in) {
        if (in.readByte() == 0x01) {
            recentChanges = new ArrayList<String>();
            in.readList(recentChanges, String.class.getClassLoader());
        } else {
            recentChanges = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (recentChanges == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recentChanges);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}