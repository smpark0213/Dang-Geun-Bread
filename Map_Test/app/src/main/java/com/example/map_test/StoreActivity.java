package com.example.map_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StoreActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        TextView textView=findViewById(R.id.textView);
        textView.setText(id);
    }
}
