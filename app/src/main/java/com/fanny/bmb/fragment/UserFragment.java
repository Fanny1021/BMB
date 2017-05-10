package com.fanny.bmb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanny.bmb.R;
import com.fanny.bmb.util.SocketUtil;
import com.fanny.bmb.util.SpUtil;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by Fanny on 17/4/11.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView userName;
    private TextView userGentle;
    private ImageView ivStaus;
    private Button login_local;
    private Button login_remote;
    private RelativeLayout rl_unlogin;
    private RelativeLayout rl_login;


    private Socket socket;
    private SocketAddress socketAddress;
    private String ipAddress;
    private String ipPort;
    private int staus;

    private View view;
    private EditText ip_address;
    private EditText ip_port;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        userGentle = (TextView) view.findViewById(R.id.user_gentle);
        ivStaus = (ImageView) view.findViewById(R.id.iv_staus);

        ip_address = (EditText) view.findViewById(R.id.ip_address0);
        ip_port = (EditText) view.findViewById(R.id.ip_port0);

        rl_unlogin = (RelativeLayout) view.findViewById(R.id.rl_unlogin);
        rl_login = (RelativeLayout) view.findViewById(R.id.rl_login);

        login_local = (Button) view.findViewById(R.id.bt_login_local);
        login_remote = (Button) view.findViewById(R.id.bt_login_remote);
        login_local.setOnClickListener(this);
        login_remote.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_local:
                /**
                 * 建立socket长连接（如果是远程连接，需要先断开）
                 */
                ipAddress = ip_address.getText().toString();
                ipPort = ip_port.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket();
                            socketAddress = new InetSocketAddress(ipAddress, Integer.parseInt(ipPort));
                            socket.setSoTimeout(3000);
                            /**
                             * 判断socket是否连接
                             */
                            if (socket.isConnected()) {
                                /**
                                 * 保存socket的ip和端口号
                                 */
                                SpUtil.putString(getApplicationContext(), SpUtil.IPADDRESS, ipAddress);
                                SpUtil.putString(getApplicationContext(), SpUtil.IPPORT, ipPort);
                                /**
                                 * 设置全局的socket
                                 */
                                SocketUtil.setSocket(socket);
                                staus=1;
                                SocketUtil.setConnectStaus(staus);

                            } else {
                                staus=-1;
                                SocketUtil.setConnectStaus(staus);
                                Log.e("Socket连接", "未连接");
                            }

                        } catch (SocketException e) {
                            staus=0;
                            SocketUtil.setConnectStaus(staus);
                            Log.e("Socket连接", "连接异常");
                            e.printStackTrace();
                        }
                    }
                }).start();

                if(staus==1){
                    /**
                     * 登录成功后，显示用户信息界面
                     */
                    rl_unlogin.setVisibility(View.GONE);
                    rl_login.setVisibility(View.VISIBLE);
                    ivStaus.setImageResource(R.mipmap.offline1);
                }else {
                    rl_unlogin.setVisibility(View.VISIBLE);
                    rl_login.setVisibility(View.GONE);
                    ivStaus.setImageResource(R.mipmap.offline);
                }
                break;
            case R.id.bt_login_remote:
                /**
                 * 发送建立网络连接请求（如果是本地连接，需要先断开）
                 */


                /**
                 * 登录成功后，显示用户信息界面
                 */
                rl_unlogin.setVisibility(View.GONE);
                rl_login.setVisibility(View.VISIBLE);
                ivStaus.setImageResource(R.mipmap.offline1);
                break;
        }
    }
}
