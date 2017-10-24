package com.example.chun.notes;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by per6 on 10/20/17.
 */

public class Note {
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

}
