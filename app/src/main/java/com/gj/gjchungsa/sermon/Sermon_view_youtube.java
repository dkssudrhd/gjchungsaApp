package com.gj.gjchungsa.sermon;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class Sermon_view_youtube extends AppCompatActivity {

    private Preach_bbs preach_bbs;
    TextView youtube_title, youtube_parse, youtube_content;

    private static String url = "http://gjchungsa.com/select_preach_bbs.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_view_youtube);


        //정보 받아오기
        Intent intent = getIntent();
        preach_bbs = (Preach_bbs) intent.getSerializableExtra("preach_bbs");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Preach_bbs[] preaches = gson.fromJson(response, Preach_bbs[].class);
                if (preaches != null) {
                    preach_bbs = preaches[0];
                }
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
                params.put("no", Integer.toString(preach_bbs.getBbs_no()));

                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    private void setting() {
        //TextView
        youtube_title = findViewById(R.id.youtube_title);
        youtube_parse = findViewById(R.id.youtube_parse);
        youtube_content = findViewById(R.id.youtube_content);


        youtube_title.setText(preach_bbs.getBbs_title());
        youtube_parse.setText(preach_bbs.getBbs_parse());
        youtube_content.setText(preach_bbs.getBbs_content());

        WebView youtube_webView = findViewById(R.id.youtube_webView);

        youtube_webView.setWebViewClient(new WebViewClient());
        youtube_webView.setWebChromeClient(new FullscreenableChromeClient(this));   //동영상 풀 화면 적용

        WebSettings webSettings = youtube_webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String youtubeEmbedCode = "<iframe width=\"330\" height=\"230\" src=\"https://www.youtube.com/embed/" + preach_bbs.getBbs_line() + "\" frameborder=\"0\" allowfullscreen></iframe>";
        youtube_webView.loadData(youtubeEmbedCode, "text/html", "utf-8");
    }
}