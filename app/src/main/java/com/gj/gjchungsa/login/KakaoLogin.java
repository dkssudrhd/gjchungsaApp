package com.gj.gjchungsa.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.google.gson.Gson;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class KakaoLogin extends AppCompatActivity {

    private static final String TAG = "KakaoLogin";
    private View loginButton, logoutButton;
    private TextView nickName;
    private ImageView profileImage;
    private static final String login_url = "http://gjchungsa.com/login/login.php";
    private static final String first_login_url ="http://gjchungsa.com/login/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);

        // 카카오톡이 설치되어 있는지 확인하는 메서드 , 카카오에서 제공함. 콜백 객체를 이용합.
        Function2<OAuthToken,Throwable, Unit> callback =new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG,"CallBack Method");
                //oAuthToken != null 이라면 로그인 성공
                if(oAuthToken!=null){
                    // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                    updateKakaoLoginUi();

                }else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail" );
                }

                return null;
            }
        };

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 해당 기기에 카카오톡이 설치되어 있는 확인
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaoLogin.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(KakaoLogin.this, callback);
                }else{
                    // 카카오톡이 설치되어 있지 않다면
                    UserApiClient.getInstance().loginWithKakaoAccount(KakaoLogin.this, callback);
                    Toast.makeText(getApplicationContext(),"카카오톡이 없습니다",Toast.LENGTH_SHORT).show();
                }


                UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                    @Override
                    public Unit invoke(User user, Throwable throwable) {
                        if(user != null){

                        }
                        return null;
                    }
                });

            }
        });

        // 로그아읏 버튼
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });

        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {

        // 로그인 여부에 따른 UI 설정
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    // 유저의 아이디
                    Log.d(TAG, "invoke: id =" + user.getId());
                    // 유저의 이메일
                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
                    // 유저의 닉네임
                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
                    // 유저의 성별
                    Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
                    // 유저의 연령대
                    Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                    
                    // 로그인 구현 여기부터 구현
                    


                    // 유저 닉네임 세팅해주기
                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                    // 유저 프로필 사진 세팅해주기

                    // 로그인이 되어있으면
                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    // 서버로부터의 응답 처리
                                    if(response.equals("null")){    //첫 로그인이라 데이터베이스에 insert
                                            first_login(user.getKakaoAccount().getEmail(),  user.getKakaoAccount().getProfile().getNickname()
                                            );
                                    }
                                    else {      //바로 로그인
                                        user_login(response);
                                    }

                                }
                                private void user_login(String response){
                                    Gson gson = new Gson();
                                    Chungsa_user chungsa_user;
                                    chungsa_user = gson.fromJson(response, Chungsa_user.class);
//                                    Chungsa_user chungsa_user = new Chungsa_user(user.getKakaoAccount().getEmail(),  user.getKakaoAccount().getProfile().getNickname(),
//                                            user.getKakaoAccount().getProfile().getNickname(), "일반사용자", "성도");

                                    Chungsa_user_InfoManager infoManager = new Chungsa_user_InfoManager();

                                    infoManager.setUserInfo(chungsa_user);
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();

                                    Log.i("Login", "Login Volley오류");
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("chungsa_user_email",user.getKakaoAccount().getEmail() );
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                } else {
                    // 로그인 되어있지 않으면
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);
                    Chungsa_user_InfoManager infoManager = new Chungsa_user_InfoManager();

                    infoManager.setUserInfo(null);
                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }
    public void first_login(String email, String nickname){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, first_login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        Chungsa_user chungsa_user = new Chungsa_user(email, nickname, nickname, "일반사용자", "성도");
                        Chungsa_user_InfoManager infoManager = new Chungsa_user_InfoManager();
                        infoManager.setUserInfo(chungsa_user);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "First Login 오류", Toast.LENGTH_SHORT).show();

                        Log.i("Login", "First Login Volley오류");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chungsa_user_email", email);
                params.put("chungsa_user_nickname", nickname);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
}