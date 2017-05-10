package com.fanny.bmb.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanny.bmb.R;
import com.fanny.bmb.util.SpUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;


public class CameraCallActivity extends Activity implements View.OnClickListener {


    EditText etLoginUsername;
    EditText etLoginPwd;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_call);

        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                // 登陆到环信服务器
                final String username = etLoginUsername.getText().toString();
                final String password = etLoginPwd.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

                    EMClient.getInstance().login(username, password, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.d("login", "登录聊天服务器成功！");
//                            Toast.makeText(CameraCallActivity.this,"成功登录到远程监控系统！",Toast.LENGTH_SHORT).show();
                            //保存用户信息
                            SpUtil.putString(getApplicationContext(),SpUtil.USERNAME,username);
                            SpUtil.putString(getApplicationContext(),SpUtil.PASSWORD,password);
                            startActivity(new Intent(CameraCallActivity.this,PhoneCallActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(int code, String error) {

                            Log.d("login", "登录聊天服务器失败！");
                            Log.d("login", error);
//                            Toast.makeText(CameraCallActivity.this,"未成功登录到远程监控系统！请确保用户是否注册和密码是否正确",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });

                }
                break;
        }
    }
}
