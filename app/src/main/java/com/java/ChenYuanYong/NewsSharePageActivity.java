package com.java.ChenYuanYong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsSharePageActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_share_page);

        Intent intent = getIntent();
        TextView textView = findViewById(R.id.share_page_title);
        String title = intent.getStringExtra("TITLE");
        if (title.length()>60){
            title = title.substring(0,60)+"...";
        }
        textView.setText(title);

        ImageView weChat = findViewById(R.id.share_WeChat);
        ImageView sina = findViewById(R.id.share_Sina);

        weChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsSharePageActivity.this,"分享到微信",Toast.LENGTH_SHORT).show();
            }
        });
        sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsSharePageActivity.this,"分享到新浪微博",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
