package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Message> messages;

    private EditText etAuthor;
    private TextView tvChatBox;
    private TextView tvAuthor;
    private EditText etMessage;

    private Runnable loadMessages;
    private Runnable showChat;
    private String forChatBox;

    public MainActivity() {
        super();
        messages = new ArrayList<>();
        loadMessages = () ->{
            //http://chat.momentfor.fun

            String response = null;

            try{
                InputStream http = new URL("http://chat.momentfor.fun").openStream();
                StringBuilder html = new StringBuilder("");
                int sym;
                while ((sym = http.read())!=-1){
                    html.append((char)sym);
                }
                response = html.toString();
                forChatBox = response;
                runOnUiThread(showChat);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        };

        showChat = () -> {
            tvChatBox.setText(forChatBox);
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAuthor = findViewById(R.id.editTextAuthor);
        tvChatBox = findViewById(R.id.textViewChatBox);
        tvAuthor = findViewById(R.id.textViewAuthor);
        etMessage = findViewById(R.id.editTextPersonAuthor);

        tvAuthor.setText(etAuthor.getText() + ":");
        tvChatBox.setMovementMethod(new ScrollingMovementMethod());

        new Thread(loadMessages).start();
    }

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
        tvChatBox.append(tvAuthor.getText() + " - " + message + "\n");
        etMessage.setText("");
    }
}