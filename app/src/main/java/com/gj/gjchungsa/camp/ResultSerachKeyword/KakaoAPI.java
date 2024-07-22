package com.gj.gjchungsa.camp.ResultSerachKeyword;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoAPI {
    @GET("v2/local/search/keyword.json")
    Call<Camp_mark_ResultSearchKeyword> getSearchKeyword(
            @Header("Authorization") String key,
            @Query("query") String query
            // 다른 매개변수 추가 가능
            // @Query("category_group_code") String category
    );
}
