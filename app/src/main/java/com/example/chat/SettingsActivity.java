package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText etAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etAuthor= findViewById(R.id.editTextAuthor);
    }

    public void onClickSetAuthor(View view) {
        Intent intent = new Intent();
        String authorName = etAuthor.getText().toString();
        if(authorName.equals("")){
            Toast.makeText(this,"Please, enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("name", authorName);
        setResult(RESULT_OK, intent);
        finish();
    }
}