package com.gj.gjchungsa.weekly;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;
import com.github.chrisbanes.photoview.PhotoView;

public class Weekly_image extends AppCompatActivity {

    //public static final String weekly_url ="http://gjchungsa.com/weekly_images/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_image);

        PhotoView photoView = findViewById(R.id.photoView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("image_res_id");

        Glide.with(this)
                .load(url)
                .into(photoView);

    }
}