package com.gj.gjchungsa.sermon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Sermon_view_vimeo extends AppCompatActivity {

    ImageButton sermon_sunam_logo_button;
    Preach_bbs preach_bbs;
    private WebView webView;
    String vimeoVideoUrl = "https://player.vimeo.com/video/";
    TextView vimeo_title, vimeo_parse, vimeo_content;


    private static String url = "http://gjchungsa.com/select_preach_bbs.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_view_vimeo);

        sermon_sunam_logo_button = findViewById(R.id.sermon_view_vimeo_logo_button);
        sermon_sunam_logo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        preach_bbs = (Preach_bbs) intent.getSerializableExtra("preach_bbs");
        int preach_bbs_no = preach_bbs.getBbs_no();

//        Toast.makeText(getApplicationContext(), preach_bbs.getBbs_title() + " : " + Integer.toString(preach_bbs_no), Toast.LENGTH_SHORT ).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Preach_bbs[] preaches = gson.fromJson(response, Preach_bbs[].class);
                if (preaches != null) {
                    preach_bbs = preaches[0];
                }
//                Log.d("stringRequest checking", response);
                setting();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley에러", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no", Integer.toString(preach_bbs_no));

                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);




    }

    private void setting() {
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new FullscreenableChromeClient(this));   //동영상 풀 화면 적용

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//         Vimeo 동영상을 로드
        webView.loadUrl(vimeoVideoUrl+preach_bbs.getBbs_line());

        vimeo_title = findViewById(R.id.vimeo_title);
        vimeo_parse = findViewById(R.id.vimeo_parse);
        vimeo_content = findViewById(R.id.vimeo_content);


        vimeo_title.setText(preach_bbs.getBbs_title());
        vimeo_parse.setText(preach_bbs.getBbs_parse());
        vimeo_content.setText(preach_bbs.getBbs_content());

    }


}