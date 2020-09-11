package com.java.ChenYuanYong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ClusterEventDetails extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cluster_event_details);

        TextView event_date = findViewById(R.id.event_date);
        TextView event_title = findViewById(R.id.event_title);


        Intent intent = getIntent();
        String date = intent.getStringExtra("DATE");
        String title = intent.getStringExtra("TITLE");

        event_date.setText(date);
        event_title.setText(title);
    }
}
