package com.minyazev.apploginpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LoginActivity extends Activity implements View.OnClickListener {

    public static final String FULLUSERNAME = "user";
    private EditText etLogin, etPass;
    private Button btn;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final String TAG = "AppLoginPass LoginActivity: ";

    private Toast toast;

    private static String urlBase = "http://192.168.1.134/loginScript.php";

    private Handler mHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage( Message msg) {

            if(msg.what == SUCCESS){
                String responseData = (String)msg.obj;
                if(responseData.equals("EMPTY\n")) {
                    int duration = Toast.LENGTH_SHORT;

                    if(toast!=null){
                        toast.cancel();
                    }
                    CharSequence text = "Check login and pass";
                    toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();

                }
                else{
                    Intent answerIntent = new Intent();
                    answerIntent.putExtra(FULLUSERNAME, responseData);
                    setResult(RESULT_OK, answerIntent);
                    finish();
                }
            } else{
                if(msg.what == ERROR){
                    setResult(RESULT_CANCELED);
                    Log.e(TAG, "handleMessage: Ошибка при получении данных" );
                    finish();

                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        etLogin = findViewById(R.id.loginTextView);
        etPass = findViewById(R.id.passTextView);
        btn = findViewById(R.id.loginButton);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String urlString = urlBase+"?login="+etLogin.getText().toString()+"&pass="+etPass.getText().toString();
        Log.d(TAG, "onClick: "+urlString);
        DataFetcherThread thread = new DataFetcherThread(mHandler, urlString);
        thread.start();

    }
}
