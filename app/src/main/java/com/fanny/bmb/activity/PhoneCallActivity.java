package com.fanny.bmb.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanny.bmb.R;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.media.EMLocalSurfaceView;
import com.hyphenate.media.EMOppositeSurfaceView;

import java.io.File;

import butterknife.BindView;

public class PhoneCallActivity extends Activity implements View.OnClickListener {
    private FrameLayout flContent;
    private Button callReceive;
    private Button callEnd;
    private Button callCall;
    private ImageView switchcamera;
    private LinearLayout lllarge;
    private LinearLayout llsmall;
    private EMOppositeSurfaceView remoteSurfaceView;  // 对方视频
    private EMLocalSurfaceView localSurfaceView;
    private View itemview;
    private EditText callnum;
    private Ringtone rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        lllarge = (LinearLayout) findViewById(R.id.ll1);
        llsmall = (LinearLayout) findViewById(R.id.ll2);

        remoteSurfaceView = (EMOppositeSurfaceView) findViewById(R.id.oppositesurface);
        localSurfaceView = (EMLocalSurfaceView) findViewById(R.id.localsurfaceview);
        localSurfaceView.setZOrderOnTop(true);
        switchcamera = (ImageView) findViewById(R.id.switchcamera);

        callCall = (Button) findViewById(R.id.call_call);
        callReceive = (Button) findViewById(R.id.call_receive);
        callEnd = (Button) findViewById(R.id.call_end);
        callCall.setOnClickListener(this);
        callReceive.setOnClickListener(this);
        callEnd.setOnClickListener(this);
        switchcamera.setOnClickListener(this);


        initSurface();
        initReceiveVideo();
        initcalllistener();
    }

    private void initcalllistener() {
        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                switch (callState) {
                    case CONNECTING: // 正在连接对方
                        Toast.makeText(PhoneCallActivity.this, "正在连接", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "正在连接对方");
                        break;
                    case CONNECTED: // 双方已经建立连接
                        Toast.makeText(PhoneCallActivity.this, "已建立连接", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "双方已经建立连接");
                        break;

                    case ACCEPTED: // 电话接通成功
                        //开始纪录通话时间
//                        rt.stop();
                        Toast.makeText(PhoneCallActivity.this, "开始通话", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "电话接通成功");
                        break;
                    case DISCONNECTED: // 电话断了
                        //结束纪录通话时间
                        Toast.makeText(PhoneCallActivity.this, "结束通话", Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "电话断了");
//                        finish();
                        break;
                    case NETWORK_UNSTABLE: //网络不稳定
                        if (error == CallError.ERROR_NO_DATA) {
                            //无通话数据
                            Toast.makeText(PhoneCallActivity.this, "网络不稳定", Toast.LENGTH_LONG).show();
                        } else {
                        }
                        break;
                    case NETWORK_NORMAL: //网络恢复正常

                        break;
                    default:
                        break;
                }

            }
        });
    }

    private boolean islocal = true;

    private void initSurface() {

        EMClient.getInstance().callManager().setSurfaceView(localSurfaceView, remoteSurfaceView);
        EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(30);
        EMCallManager.EMVideoCallHelper callHelper = EMClient.getInstance().callManager().getVideoCallHelper();
        /**
         * 方案一：给两个surfaceview各包裹一个linearlayout，然后动态删除和添加两个surfaceview内容
         */


        /**
         * 方案二：直接给两个surfaceview设置点击事件，动态给两个view添加layoutparams约束
         */

//        switchsf();


    }

    private void switchsf() {
        if (islocal) {
            localSurfaceView.setZOrderOnTop(true);
            localSurfaceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    localSurfaceView.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 =
                            new LinearLayout.LayoutParams(100, 100);
                    remoteSurfaceView.setLayoutParams(params1);
                    localSurfaceView.setZOrderOnTop(false);
                    remoteSurfaceView.setZOrderOnTop(true);
                    islocal = false;

                }
            });
        }

        if (!islocal) {
            remoteSurfaceView.setZOrderOnTop(true);
            remoteSurfaceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    remoteSurfaceView.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 =
                            new LinearLayout.LayoutParams(100, 100);
                    localSurfaceView.setLayoutParams(params1);
                    remoteSurfaceView.setZOrderOnTop(false);
                    localSurfaceView.setZOrderOnTop(true);
                    islocal = true;
                }
            });

        }

    }

    private void switchsf1(EMOppositeSurfaceView remoteSurfaceView, EMLocalSurfaceView localSurfaceView) {
        //        llsmall.setFocusable(true);
//        llsmall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                if(islocal) {
//                    llsmall.removeView(localSurfaceView);
//                    lllarge.removeView(remoteSurfaceView);
//                    llsmall.addView(remoteSurfaceView);
//                    lllarge.addView(localSurfaceView);
//
//                    islocal=false;
//                }else {
//                    llsmall.removeView(remoteSurfaceView);
//                    lllarge.removeView(localSurfaceView);
//                    llsmall.addView(localSurfaceView);
//                    lllarge.addView(remoteSurfaceView);
//
//                    islocal=true;
//                }
//            }
//        });

//        LinearLayout ll3 = (LinearLayout) localSurfaceView.getParent();
//        LinearLayout ll4 = (LinearLayout) remoteSurfaceView.getParent();
//        ll3.addView(remoteSurfaceView);
//        ll3.removeView(localSurfaceView);
//        ll4.addView(localSurfaceView);
//        ll4.removeView(remoteSurfaceView);

    }

    //注册监听来电广播
    private void initReceiveVideo() {
        IntentFilter callFilter = new
                IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(new CallReceiver(), callFilter);
    }

    String remoteUser;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_call:
                itemview = View.inflate(getApplicationContext(), R.layout.item, null);
//                itemview = getLayoutInflater().inflate(R.layout.item, null);
                callnum = (EditText) itemview.findViewById(R.id.et_number);
                AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneCallActivity.this);
                dialog.setTitle("拨号")
                        .setView(itemview)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定,拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remoteUser = callnum.getText().toString();
                                //拨打视频电话
                                if (remoteUser != null) {
                                    SendVideo(remoteUser);
                                }
                            }
                        }).create().show();

                break;
            case R.id.call_receive:
                //接听视频通话
                ReceiveVideo();//设备端需要自动连接。
                break;
            case R.id.call_end:
                //挂断结束视频通话
                EndVedio();
                break;
            case R.id.switchcamera:
                //切换前置和后置摄像头
                EMClient.getInstance().callManager().switchCamera();
                break;
        }

    }

    private void EndVedio() {
        /**
         * 挂断通话
         */
        try {
            EMClient.getInstance().callManager().endCall();
            Toast.makeText(PhoneCallActivity.this, "通话结束", Toast.LENGTH_LONG).show();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
    }

    private void ReceiveVideo() {
        /**
         * 接听通话
         * @throws EMNoActiveCallException
         * @throws EMNetworkUnconnectedException
         */
        try {
            EMClient.getInstance().callManager().answerCall();
        } catch (EMNoActiveCallException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra("from");
            String type = intent.getStringExtra("type");
            //跳转到通话页面
            //自定义toast显示来电状态
            Toast.makeText(PhoneCallActivity.this, "用户" + from + "来电", Toast.LENGTH_LONG).show();
            //来电铃声设置
//            setMyRingtone("/RingTone/a.mp3");

        }
    }

    private void setMyRingtone(String path) {
        File sdfile = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
        Uri newUri = this.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
        rt = RingtoneManager.getRingtone(this, newUri);
        rt.play();


    }

    //拨打视频通话
    private void SendVideo(String username) {
        try {
            EMClient.getInstance().callManager().makeVideoCall(username);
        } catch (EMServiceNotReadyException e) {
            e.printStackTrace();
        }
    }
}
