package com.gj.gjchungsa.weekly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;

public class Weekly_view extends AppCompatActivity {

    public Weekly weekly;
    public static final String weekly_url ="http://gjchungsa.com/weekly/weekly_images/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_view);

        Intent intent = getIntent();
        weekly = (Weekly) intent.getSerializableExtra("weekly");

//        ImageView weekly1 = findViewById(R.id.weekly1) ;
//        ImageView weekly2 = findViewById(R.id.weekly2) ;
//        ImageView weekly3 = findViewById(R.id.weekly3) ;


        AppCompatImageView weekly1 = findViewById(R.id.weekly1);
        AppCompatImageView weekly2 = findViewById(R.id.weekly2);
        AppCompatImageView weekly3 = findViewById(R.id.weekly3);



        String url1 = weekly_url + weekly.getWeekly_image1();
        String url2 = weekly_url + weekly.getWeekly_image2();
        String url3 = weekly_url + weekly.getWeekly_image3();

        Glide.with(this)
                .load(url1)
                .error(R.drawable.warning)
                .placeholder(R.drawable.loading)
                .into(weekly1);
        Glide.with(this)
                .load(url2)
                .error(R.drawable.warning)
                .placeholder(R.drawable.loading)
                .into(weekly2);
        Glide.with(this)
                .load(url3)
                .error(R.drawable.warning)
                .placeholder(R.drawable.loading)
                .into(weekly3);

        weekly1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 발생 시 확대/축소 로직 추가
                Intent intent = new Intent(getApplicationContext(), Weekly_image.class);
                intent.putExtra("image_res_id", url1); // 확대하려는 이미지 리소스 ID로 변경
                startActivity(intent);
            }
        });
        weekly2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 발생 시 확대/축소 로직 추가
                Intent intent = new Intent(getApplicationContext(), Weekly_image.class);
                intent.putExtra("image_res_id", url2); // 확대하려는 이미지 리소스 ID로 변경
                startActivity(intent);
            }
        });
        weekly3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 발생 시 확대/축소 로직 추가
                Intent intent = new Intent(getApplicationContext(), Weekly_image.class);
                intent.putExtra("image_res_id", url3); // 확대하려는 이미지 리소스 ID로 변경
                startActivity(intent);
            }
        });



    }
}