package com.gj.gjchungsa.main_edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.main_edit.user_edit.main_user_edit;

public class main_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit);
    }

    public void sermon_insert_go(View view) {
        Intent intent = new Intent(getApplicationContext(), main_user_edit.class);
        startActivity(intent);
    }
}