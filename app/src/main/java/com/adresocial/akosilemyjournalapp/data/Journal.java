package com.adresocial.akosilemyjournalapp.data;


public class Journal {

    private String id;
    private String title;
    private String entry;
    private String created;
    private long time;
    private String mood;


    public Journal() {
        // Default constructor required for calls to DataSnapshot.getValue(Journal.class)
    }

    public Journal(String title, String entry, String created, long time, String mood){
        this.title = title;
        this.entry= entry;
        this.created = created;
        this.time = time;
        this.mood = mood;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
