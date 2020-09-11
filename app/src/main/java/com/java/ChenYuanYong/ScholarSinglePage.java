package com.java.ChenYuanYong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

public class ScholarSinglePage extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Scholar scholar = (Scholar) intent.getSerializableExtra("SCHOLAR");

        setContentView(R.layout.scholar_single_page);

        Toolbar toolbar = findViewById(R.id.scholar_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标

        nestedScrollView = findViewById(R.id.scholar_scroll_view);

        MyImageView scholar_avatar = findViewById(R.id.scholar_page_avatar);
        TextView scholar_name = findViewById(R.id.scholar_page_name);
        TextView scholar_position = findViewById(R.id.scholar_page_position);
        TextView scholar_affiliation = findViewById(R.id.scholar_page_affiliation);
        TextView scholar_edu = findViewById(R.id.scholar_page_edu);
        TextView scholar_work = findViewById(R.id.scholar_page_work);
        TextView scholar_bio = findViewById(R.id.scholar_page_bio);
        TextView scholar_page_title = findViewById(R.id.scholar_page_title);


        scholar_avatar.setImageURL(scholar.avatar);

        String name;
        if (!scholar.name_zh.equals(""))
            name = scholar.name_zh;
        else
            name  = scholar.name;
        scholar_name.setText(name);

        if (!scholar.position.equals(""))
            scholar_position.setText(scholar.position);

        scholar_affiliation.setText(scholar.affiliation);

        if(!scholar.edu.equals(""))
            scholar_edu.setText(scholar.edu);

        if(!scholar.work.equals(""))
            scholar_work.setText(scholar.edu);

        if (!scholar.bio.equals(""))
            scholar_bio.setText(scholar.bio);

        if (scholar.isPassedAway)
            name = "追忆学者 - "+name;
        scholar_page_title.setText(name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
