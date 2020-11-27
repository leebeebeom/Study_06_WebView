package com.beebeom.a06_webview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private Button mButton;
    private EditText mEditText;
    private SwipeRefreshLayout mRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRefreshLayout = findViewById(R.id.swipeLayout);
        //스와이프 리프레쉬 색 설정
        mRefreshLayout.setColorSchemeColors(Color.BLUE);
        mWebView = findViewById(R.id.webView);
        mEditText = findViewById(R.id.et_address);

        //웹뷰를 설정할 수 있는 객체를 만들어줌.
        WebSettings settings = mWebView.getSettings();
        //웹이 어플 안에서 동작하도록.
        //이 코드를 안 써주면 크롬이든 뭐든 다른 브라우져 어플리케이션이 동작함.
        mWebView.setWebViewClient(new WebViewClient());
        //자바 허용해주기.
        settings.setJavaScriptEnabled(true);

        //키보드 오른쪽 아래 클릭시 호출되는 메소드
        //이제 버튼 필요 없어서 visibility 로 gone 해줌
        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            //키보드 오른쪽 아래가 서치버튼일시 버튼의 onClick(웹뷰 로드 url)호출
            //사실 if 문 필요없음ㅋ
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mButton.callOnClick();
                //onClick 호출 후 키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
            return true;
        });

        mButton = findViewById(R.id.btn_connect);
        mButton.setOnClickListener(v -> {
            //주소 객체화
            String url = mEditText.getText().toString();
            //웹뷰의 주소는 https로 시작해야하기 때문에
            //시작이 https가 아니라면 내가 입력해줌.
            if (url.startsWith("https://")) {
                mWebView.loadUrl(url);
            } else {
                mWebView.loadUrl("https://" + url);
            }
        });

        //스와이프 리프레쉬 설정
        mRefreshLayout.setOnRefreshListener(() -> {
            mWebView.reload();
            //이거 안해주면 안멈춤
            mRefreshLayout.setRefreshing(false);
        });
    }

    //뒤로가기 버튼 재정의
    @Override
    public void onBackPressed() {
        //웹뷰가 뒤로갈 수 있으면 뒤로가기
        //아니면 원래 뒤로가기 버튼 동작
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }//else 에 넣어주지 않으면 이벤트가 흘러서 무조건 어플이 꺼지기 때문에 else 필수
        else {
            super.onBackPressed();
        }
    }

    //메뉴 달아주기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }
    //메뉴 버튼 기능 달아주기
    //switch 대신 if 문 쓰라길래 바꿔줌
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.move_back && mWebView.canGoBack()) {
            mWebView.goBack();
        }else if(item.getItemId() == R.id.move_forward && mWebView.canGoForward()){
            mWebView.goForward();
        }else {
            mWebView.reload();
        }
        return super.onOptionsItemSelected(item);
    }
}