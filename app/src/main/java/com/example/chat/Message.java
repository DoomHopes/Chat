package com.example.chat;

//ORM

import android.annotation.SuppressLint;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Message {

    private static SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
    private static SimpleDateFormat messageFormat = new SimpleDateFormat("dd.MM hh.mm");

    //Model
    private String text;
    private String author;
    private Date moment;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    //Interface
    public Message(String text, String author, Date moment) {
        this.text = text;
        this.author = author;
        this.moment = moment;
    }

    public Message(String text, String author) {
        this.text = text;
        this.author = author;
        this.moment = new Date();
    }

    @Override
    public String toString(){
        return this.author + " - " + this.text + " <" + messageFormat.format(this.moment) +">";
    }

    public String toGETRequest() {
        return "author=" + this.author + "&text="+ this.text;
    }

    //Fabric
    public static Message fromJSONString(String json){
        // {"id":22, "author":"Name", "text":"Message", "moment":"2020-11-11 12:12:12"}
        try{
            JSONObject jo = new JSONObject(json);
            return new Message(jo.getString("author"), jo.getString("text"),
                    sqlFormat.parse(jo.getString("moment")));
        }
        catch (Exception ignored){
            return null;
        }
    }

    public static Message fromJSONObject(JSONObject jo){
        // {"id":22, "author":"Name", "text":"Message", "moment":"2020-11-11 12:12:12"}
        try{
            return new Message(jo.getString("author"), jo.getString("text"),
                    sqlFormat.parse(jo.getString("moment")));
        }
        catch (Exception ignored){
            return null;
        }
    }


}
