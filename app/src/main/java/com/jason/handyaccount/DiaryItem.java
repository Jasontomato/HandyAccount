package com.jason.handyaccount;

public class DiaryItem {
    private int id;
    private String CurStory;
    private String Writing_date;
    private String Location;

    public DiaryItem() {
        super();
        CurStory = "";
        Writing_date = "";

    }


    public DiaryItem(String curStory, String writing_date) {
        CurStory = curStory;
        Writing_date = writing_date;
        Location = "";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
    public String getCurStory() {
        return CurStory;
    }

    public void setCurStory(String curStory) {
        CurStory = curStory;
    }

    public String getWriting_date() {
        return Writing_date;
    }

    public void setWriting_date(String writing_date) {
        Writing_date = writing_date+"          成都.西南财经大学柳林校区";
    }
}
