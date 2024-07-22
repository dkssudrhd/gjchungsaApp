package com.gj.gjchungsa.sermon.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.gj.gjchungsa.sermon.Series_sermon.edit.Series_sermon_edit;

import java.util.ArrayList;

public class Sermon_main extends AppCompatActivity {

    ArrayList<Preach_bbs> preach_bbs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_main);

        Intent get_intent = getIntent();
        preach_bbs = (ArrayList<Preach_bbs>) get_intent.getSerializableExtra("preach_bbs");

    }

    public void series_sermon_go_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Series_sermon_edit.class);
        startActivity(intent);
    }

    public void sermon_delete_go(View view){
        Intent intent = new Intent(getApplicationContext(), Sermon_delete.class);
        intent.putExtra("preach_bbs", preach_bbs);
        startActivity(intent);
    }

    public void sermon_edit_go(View view){
        Intent intent = new Intent(getApplicationContext(), Sermon_edit.class);
        intent.putExtra("preach_bbs", preach_bbs);
        startActivity(intent);
    }

    public void sermon_insert_go(View view){
        Intent intent = new Intent(getApplicationContext(), Sermon_insert.class);
        startActivity(intent);
    }

    public void sermon_s_catalouge_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Preach_bbs_s_catalouge_edit.class);
        startActivity(intent);
    }
}