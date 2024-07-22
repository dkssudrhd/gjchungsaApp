package com.gj.gjchungsa.introduction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.ImageSliderAdapter;
import com.gj.gjchungsa.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Introduction_main extends AppCompatActivity {

    ArrayList<Introduction_image> introduction_image_list = new ArrayList<>();
    TextView introduction_text1, introduction_text2, introduction_text3, introduction_text4, introduction_text5, introduction_text6;

    String introduction_image_select_url ="http://gjchungsa.com/introduction/introduction_select.php";
    String introduction_image_url ="http://gjchungsa.com/introduction/introduction_images/";

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_main);

        introduction_text1 = findViewById(R.id.introduction_textView1);
        introduction_text2 = findViewById(R.id.introduction_textView2);
        introduction_text3 = findViewById(R.id.introduction_textView3);
        introduction_text4 = findViewById(R.id.introduction_textView4);
        introduction_text5 = findViewById(R.id.introduction_textView5);
        introduction_text6 = findViewById(R.id.introduction_textView6);

        String text1 =  "전남 영암 상호에서 백정석목사의 장남으로 태어났다.\n" +
                        "농촌교회 목사의 아들로 살아와서인지 그는 늘 가난하고, 소외된 자를 향한 눈물을 간직한채 살아간다.\n" +
                        "한 때 국회의원의 꿈을 안고, 학생회장을 역임하고, 정치현장에 관심을 둔 적도 있었다.\n";

        String text2 =  "하나님의 특별한 은혜로 광주대학교(B.A.), 세종대학교(M.SW.), 광신대학교(M.Div.), 충신대학교(M.A&M.Div.equ), 아세아연합신학대학교(Th.M.&Ph.D.)에서 공부했고,\n" +
                        "서울에서 교육목사, GBS교육개발원총무, 솔로몬영재사관학교 교목으로 훈련받았다.\n" +
                        "교육목사로 섬기는 동안 기독교육의 원리를 전국에 소개하는 일을 했고, 통합신앙캠프, 부모학교, 교사학교 등을 열정적으로 섬겼다.\n" +
                        "그는 사람 세우는 일에 남다른 열정을 가진 목사다.\n";

        String text3 =  "\"건물을 팔아 사람을 세우자\"고 외치며 실제로 그가 섬기는 광주청사교회와 신앙특성화학교샬롬스쿨, 뿌리깊은나무국제기독교육연구소를 통하여 탁월한 역사들을 만들어가고 있다.\n";

        String text4 =  "그 외에도 하나님의 특별한 간섭하심으로 같은 신앙 전수의 통로를 열기 위한 부흥회 강사, 광신대학교 실천신학교수, 마을기업 들래미 이사장, 기독문화선교회 강사 등으로 섬기며 하나님의 나라 확장을 위해 열심히 달리는 중이다. \n";

        String text5 =  "그는 항상 \"날마다 감사, 모든 것이 감사, 감사뿐이다. 목사로서 행복하고, 광산구민이라 행복하다. 사랑 받아서 행복하고, 사랑할 수 있어 행복하다. 무엇보다 허물 뿐인 종을 지지하고, 격려해 주는 많은 동역자들로 인해 행복하다.\"고 고백한다.\n";

        String text6 =  "저서로는 『세대통합이 살길입니다』, 『세대통합목회가 대안입니다』, 『샬롬스쿨이 대안입니다』, 『교회가 길을 찾다』 등이 있다.";

        Spannable spannable1 = new SpannableString(text3);
        int startIndex1 = 0;
        int endIndex1 = 16;

        Typeface typeface1 = ResourcesCompat.getFont(this, R.font.hakgyoansimgaeulsopungb);
        spannable1.setSpan(
                new CustomTypefaceSpan(typeface1),
                startIndex1,
                endIndex1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannable1.setSpan(
                new RelativeSizeSpan(1.5f),
                startIndex1,
                endIndex1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        Spannable spannable2 = new SpannableString(text5);
        int startIndex2 = 6;
        int endIndex2 = 129;

        Typeface typeface2 = ResourcesCompat.getFont(this, R.font.hakgyoansimgaeulsopungb);
        spannable2.setSpan(
                new CustomTypefaceSpan(typeface2),
                startIndex2,
                endIndex2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannable2.setSpan(
                new RelativeSizeSpan(1.5f),
                startIndex2,
                endIndex2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );




        introduction_text1.setText(text1);
        introduction_text2.setText(text2);
        introduction_text3.setText(spannable1);
        introduction_text4.setText(text4);
        introduction_text5.setText(spannable2);
        introduction_text6.setText(text6);

        image_slide_setting();
    }

    private void image_slide_setting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, introduction_image_select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Gson gson = new Gson();
                        introduction_image_list =new ArrayList<>();
                        Introduction_image[] introduction_images = gson.fromJson(response, Introduction_image[].class);

                        if (introduction_images != null) {
                            introduction_image_list.addAll(Arrays.asList(introduction_images));
                            ConstraintLayout constraintLayout = findViewById(R.id.introduction_main_constraintLayout);
                            constraintLayout.setVisibility(View.VISIBLE);

                        }
                        image_slide();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void image_slide() {


        sliderViewPager = findViewById(R.id.introduction_main_ViewPager);
        layoutIndicator = findViewById(R.id.introduction_main_layoutIndicators);
        sliderViewPager.setOffscreenPageLimit(1);

        String image_url[] = new String[introduction_image_list.size()];

        for(int i=0; i< introduction_image_list.size();i++){
            image_url[i] = introduction_image_url + introduction_image_list.get(i).getIntroduction_image_url();
        }

        sliderViewPager.setAdapter(new ImageSliderAdapter(this, image_url));

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });


        setupIndicators(image_url.length);

    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

}