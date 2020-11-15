package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<Message> messages;

    private EditText etAuthor;
    private TextView tvChatBox;
    private TextView tvAuthor;
    private EditText etMessage;

   // private final Runnable loadMessages;

    private SendAndLoadMessage exchanger;

    private final Runnable updateChat;
    private Runnable timerTick;
    private Handler handler;
    private String jsonResponse;

    public MainActivity() {
        super();
        messages = new ArrayList<>();
        handler = new Handler();
        exchanger = new SendAndLoadMessage();

        updateChat = () -> {
            StringBuilder txt = new StringBuilder();
            for(Message m:messages){
                txt.append(m.toString()).append("\n");
            }
            tvChatBox.setText(txt.toString());
        };

        timerTick = () -> {
            exchanger.setMessage(null);
            new Thread(exchanger).start();
            handler.postDelayed(timerTick, 1000);
        };
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAuthor = findViewById(R.id.editTextAuthor);
        tvChatBox = findViewById(R.id.textViewChatBox);
        tvAuthor = findViewById(R.id.textViewAuthor);
        etMessage = findViewById(R.id.editTextPersonAuthor);

        tvAuthor.setText(etAuthor.getText());
        tvChatBox.setMovementMethod(new ScrollingMovementMethod());

       // new Thread(exchanger).start();
        handler.post(timerTick);
    }

    @SuppressLint("SetTextI18n")
    public void onClickSetAuthor(View view) {
        CharSequence authorName = etAuthor.getText();
        if(authorName.equals("")){
            Toast.makeText(this,"Please, enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        tvAuthor.setText(authorName + ":");
    }

    public void sendMessage(View view) {
        String message = etMessage.getText().toString();
        if(message.equals("")){
            Toast.makeText(this,"Please, enter message", Toast.LENGTH_SHORT).show();
            return;
        }
        exchanger.setMessage(new Message(etAuthor.getText().toString(), message));
        //tvChatBox.append(tvAuthor.getText() + " - " + message + "\n");
        etMessage.setText("");
        new Thread(exchanger).start();
    }

    class ParseJson implements Runnable{

        private JSONArray messagesArray;
        private final String src;

        public ParseJson(String src) {
            this.src = src;
        }

        public JSONArray getMessages() {
            return messagesArray;
        }

        @Override
        public void run() {
            if(this.src == null){
                return;
            }
            try{
                JSONObject json = new JSONObject(this.src);
                int status = json.getInt("status");
                if(status!=1) {
                    messagesArray = null;
                    return;
                }

                this.messagesArray = json.getJSONArray("data");
                messages.clear();
                for(int i= 0;i<this.messagesArray.length();i++){
                    messages.add(Message.fromJSONObject(this.messagesArray.getJSONObject(i)));
                }
                runOnUiThread(updateChat);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    class SendAndLoadMessage implements Runnable{

        private Message message;

        public void setMessage(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            //http://chat.momentfor.fun

            String url = "http://chat.momentfor.fun";

            if(this.message != null){
                url+="?" + this.message.toGETRequest();
            }

            String response = null;

            try{
                InputStream http = new URL(url).openStream();
                StringBuilder html = new StringBuilder();
                int sym;
                while ((sym = http.read())!=-1){
                    html.append((char)sym);
                }
                response = html.toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    jsonResponse = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                }
                ParseJson parser = new ParseJson(jsonResponse);
                new Thread(parser).start();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
























