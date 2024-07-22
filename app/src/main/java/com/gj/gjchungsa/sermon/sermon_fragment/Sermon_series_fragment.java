package com.gj.gjchungsa.sermon.sermon_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Series_sermon.Series_sermon;
import com.gj.gjchungsa.sermon.Series_sermon.Series_sermon_Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Sermon_series_fragment extends Fragment {

    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    RecyclerView recyclerView;
    Series_sermon_Adapter adapter;
    int page_num =0;
    ArrayList<Series_sermon> series_sermon_list = new ArrayList<>();
    String select_url = "http://gjchungsa.com/series_sermon/series_sermon_select.php";


    public Sermon_series_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sermon_series_fragment, container, false);

        findview_setting(rootView);

        list_loading();

        return rootView;
    }

    private void list_setting(){        //list셋팅
        adapter = new Series_sermon_Adapter();
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<series_sermon_list.size() ;i++){
            adapter.addItem(series_sermon_list.get(i));
        }
        recyclerView.setAdapter(adapter);
    }


    private void btn_page_number_setting(){     //버튼의 숫자와 위치를 셋팅
        if(page_num>=3){
            text_1.setVisibility(View.VISIBLE);
            text_1.setText(Integer.toString(page_num-2));
        } else {
            text_1.setVisibility(View.GONE);
        }
        if(page_num>=2){
            text_2.setVisibility(View.VISIBLE);
            text_2.setText(Integer.toString(page_num-1));
        } else {
            text_2.setVisibility(View.GONE);
        }
        text_3.setText(Integer.toString(page_num));
        if(page_num*10<series_sermon_list.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<series_sermon_list.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }

    private void findview_setting(ViewGroup rootView) {
        start_text = rootView.findViewById(R.id.series_list_start_text);
        text_1 = rootView.findViewById(R.id.series_list_1_text);
        text_2 = rootView.findViewById(R.id.series_list_2_text);
        text_3 = rootView.findViewById(R.id.series_list_3_text);
        text_4 = rootView.findViewById(R.id.series_list_4_text);
        text_5 = rootView.findViewById(R.id.series_list_5_text);
        last_text = rootView.findViewById(R.id.series_list_last_text);

        recyclerView = rootView.findViewById(R.id.series_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void list_view_setting(){       //recyclerView에 보여주는 버튼 셋팅
        start_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = 1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-2;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+2;
                btn_page_number_setting();
                list_setting();
            }
        });
        last_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = ((series_sermon_list.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });
    }

    private void list_loading(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        camp_schedule_listup(response);
                    }

                    private void camp_schedule_listup(String response) {
                        Gson gson = new Gson();
                        series_sermon_list =new ArrayList<>();
                        Series_sermon[] series_sermons = gson.fromJson(response, Series_sermon[].class);
                        if (series_sermons != null) {
                            series_sermon_list.addAll(Arrays.asList(series_sermons));
                        }
                        page_num =1;

                        list_view_setting();
                        list_setting();
                        btn_page_number_setting();
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

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}