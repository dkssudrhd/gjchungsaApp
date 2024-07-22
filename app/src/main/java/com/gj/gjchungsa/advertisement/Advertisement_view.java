package com.gj.gjchungsa.advertisement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;

public class Advertisement_view extends AppCompatActivity {

    Advertisement advertisement;

    ImageView imageView, cancel;
    String image_url = "http://gjchungsa.com/advertisement/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_view);

        Intent get_intent = getIntent();
        advertisement = (Advertisement) get_intent.getSerializableExtra("advertisement");

        imageView = findViewById(R.id.advertisement_imageview);
        cancel = findViewById(R.id.advertisement_cancel_btn);

        Glide.with(this)
                .load(image_url + advertisement.getAdvertisement_image_url())
                .placeholder(R.drawable.loading)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(advertisement.getAdvertisement_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                if(advertisement.getAdvertisement_url().contains("instagram")){        //인스타일시
                    intent.setPackage("com.instagram.android"); // 인스타그램 앱을 사용하여 열기
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // 인스타그램 앱이 설치되어 있지 않은 경우, 웹 브라우저에서 열기
                        intent.setPackage(null); // 패키지 설정 제거
                        startActivity(intent);
                    }
                }
                else {  //아닐시
                    startActivity(intent);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("advertisement");
                sendBroadcast(intent);
                finish();

            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}