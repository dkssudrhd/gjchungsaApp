package com.gj.gjchungsa.camp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.gj.gjchungsa.R;

public class Camp_recommend_list extends AppCompatActivity {

    Button city_all;
    ImageView seoul, incheon, gangwon, gyeonggi,
            chungnam, sejong, daejeon, jeollabuk, gwangju, jeollanam,
            jeju, gyeongbuk, daegu, ulsan, gyeongnam, busan, chungbuk;


    Intent intent, get_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_recommend_list);

        get_intent = getIntent();   //intent정보 받아오기
        String Camp_recommend_type = "먹거리";


        seoul = findViewById(R.id.seoul);
        incheon = findViewById(R.id.incheon);
        city_all = findViewById(R.id.city_all);
        gangwon = findViewById(R.id.gangwon);
        gyeonggi = findViewById(R.id.gyeonggi);
        chungnam = findViewById(R.id.chungnam);
        sejong = findViewById(R.id.sejong);
        daejeon = findViewById(R.id.daejeon);
        jeollabuk = findViewById(R.id.jeollabuk);
        gwangju = findViewById(R.id.gwangju);
        jeollanam = findViewById(R.id.jeollanam);
        jeju  = findViewById(R.id.jeju);
        gyeongbuk = findViewById(R.id.gyeongbuk);
        daegu = findViewById(R.id.daegu);
        ulsan = findViewById(R.id.ulsan);
        gyeongnam = findViewById(R.id.gyeongnam);
        busan = findViewById(R.id.busan);
        chungbuk = findViewById(R.id.chungbuk);

        seoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("서울", view, 37.5642135
                , 127.0016985);
            }
        });
        incheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("인천", view,
                        37.4562557, 126.7052062);
            }
        });
        city_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { next_intent("전체", view,
                    35.153788,126.813754); }
        });
        gangwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("강원", view,
                        37.6004030, 128.4305235);
            }
        });
        gyeonggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("경기", view,
                        37.3594648, 127.4668975);
            }
        });
        chungnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("충남", view,
                        36.5343899, 126.8015332);
            }
        });
        sejong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("세종", view,
                        36.4800541, 127.2891841);
            }
        });
        daejeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("대전", view,
                        36.3504578,  127.3848233);
            }
        });
        jeollabuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("전북", view,
                        35.7122521, 127.1300141);
            }
        });
        gwangju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("광주", view,
                        35.1537885, 126.8137545);
            }
        });
        jeollanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("전남", view,
                        34.9266460, 126.9984787);
            }
        });
        jeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("제주", view,
                        33.3760727, 126.5423465);
            }
        });
        gyeongbuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("경북", view,
                        36.4509838, 128.7038986);
            }
        });
        daegu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("대구", view,
                        35.8713585, 128.6018128);
            }
        });
        busan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("부산", view,
                        35.1798079, 129.0751629);
            }
        });
        ulsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("울산", view,
                        35.5394729, 129.3116054);
            }
        });
        gyeongnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("경남", view,
                        35.4238849, 128.2303449);
            }
        });
        chungbuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_intent("충북", view,
                        36.8650697, 127.7564082);
            }
        });


    }
    public void next_intent(String where, View view, Double y, Double x){//클릭시 메뉴 버튼 두개 나옴 그리고 누르면 이동
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.camp_recommend_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.camp_bbs){

                    intent = new Intent(getApplicationContext(), Camp_recommend_bbs.class);
                    intent.putExtra("Camp_mark_where", where);
                    startActivity(intent);

                }else if(menuItem.getItemId() == R.id.camp_map){
                    intent = new Intent(getApplicationContext(), Camp_recommend.class);
                    intent.putExtra("Camp_mark_where", where);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }
}