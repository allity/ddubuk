package com.example.ddubuk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {
    MainActivity ma = new MainActivity();
    private static String email;
    private String userList;

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "3BzU7Utmsotxk8qdLyB3";
    private static String OAUTH_CLIENT_SECRET = "RkpB2JWm4P";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    /**
     * UI 요소들
     */
    private OAuthLoginButton mOAuthLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();

        mContext = this;

        initData();
        initView();

        mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
//        mOAuthLoginButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
//            }
//        });

        this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());
    }


    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    private void initView() {
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String password = mOAuthLoginInstance.getAccessToken(mContext);

                ProfileTask task = new ProfileTask();
                //유저 정보를 가져오는 업무 담당
                task.execute(password);

                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);

                Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    class ProfileTask extends AsyncTask<String, Void, String> {
        private String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            //result 값은 JSONObject 형태로 넘어옵니다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //넘어온 result 값을 JSONObject 로 변환해주고, 값을 가져오면 되는데요.
                // result 를 Log에 찍어보면 어떻게 가져와야할 지 감이 오실거에요.
                JSONObject object = new JSONObject(result);
                if (object.getString("resultcode").equals("00")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    email = jsonObject.getString("email");
                    Log.d("result", result);
                    Log.d("jsonObject", jsonObject.toString());

                    SharedPreferences sharedPreferences = getSharedPreferences("inform", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", jsonObject.getString("email"));
                    editor.apply();

                    Log.e("email----->", email);
                    setEmail(email);

                    setUserList();

                /*SharedPreferences.Editor editor = activity.userData.edit();
                editor.putString("email", jsonObject.getString("email"));
                editor.putString("name", jsonObject.getString("name"));
                editor.putString("nickname", jsonObject.getString("nickname"));
                editor.putString("profile", jsonObject.getString("profile_image"));
                editor.apply();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setEmail(String email) {
        int idx = email.indexOf("@");
        String id = email.substring(0, idx);
        this.email = id;
    }

    public static String getEmail() {
        return email;
    }

    private void setUserList() {
        ma.mdatabase.child("사용자").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = String.valueOf(dataSnapshot.getValue());
                checkUser(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUser(String userList) {
        String id = getEmail();
        Log.e("<checkUser>", id);
        if (!userList.contains(id)) {
            Intent intent = new Intent(LoginActivity.this, ResearchActivity.class);
            FragmentAccount.idView.setVisibility(View.GONE);
            FragmentAccount.layout_idView.setVisibility(View.VISIBLE);
            FragmentAccount.view_userId.setText(id + "님");
            startActivity(intent);
        } else {
            FragmentAccount.idView.setVisibility(View.GONE);
            FragmentAccount.layout_idView.setVisibility(View.VISIBLE);
            FragmentAccount.view_userId.setText(id + "님");
        }
    }
}