package com.fanny.bmb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanny.bmb.R;
import com.fanny.bmb.adapter.HorizontalPagerAdapter;
import com.fanny.bmb.util.SocketUtil;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fanny on 17/4/12.
 */

public class BedActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.btn_turnleft)
    ImageButton btnTurnleft;
    @BindView(R.id.btn_turnright)
    ImageButton btnTurnright;
    @BindView(R.id.btn_liftfoot)
    ImageButton btnLiftfoot;
    @BindView(R.id.btn_stayfoot)
    ImageButton btnStayfoot;
    @BindView(R.id.btn_autioA)
    ImageButton btnAutioA;
    @BindView(R.id.btn_autioB)
    ImageButton btnAutioB;
    @BindView(R.id.btn_situp)
    ImageButton btnSitup;
    @BindView(R.id.btn_liedown)
    ImageButton btnLiedown;
    @BindView(R.id.btn_open_bedpan)
    ImageButton btnOpenBedpan;
    @BindView(R.id.btn_close_bedpan)
    ImageButton btnCloseBedpan;
    @BindView(R.id.btn_reset)
    ImageButton btnReset;

    private Vibrator vibrator;
    char SendData1[] = {'$', 'A', 'A', '0', '0', '0', 'x'};   //
    char SendData2[] = {'$', 'A', 'B', '0', '0', '0', 'x'};   //
    char SendData3[] = {'$', 'A', 'C', '0', '0', '0', 'x'};   //
    char SendData4[] = {'$', 'A', 'D', '0', '0', '0', 'x'};   //
    char SendData5[] = {'$', 'A', 'E', '0', '0', '0', 'x'};   //
    char SendData6[] = {'$', 'A', 'F', '0', '0', '0', 'x'};   //
    char SendData7[] = {'$', 'A', 'G', '0', '0', '0', 'x'};   //
    char SendData8[] = {'$', 'A', 'H', '0', '0', '0', 'x'};   //
    char SendData9[] = {'$', 'A', 'I', '0', '0', '0', 'x'};   //
    char SendData10[] = {'$', 'A', 'J', '0', '0', '0', 'x'};   //
    char SendData11[] = {'$', 'A', 'K', '0', '0', '0', 'x'};   //
    char SendData12[] = {'$', 'A', 'L', '0', '0', '0', 'x'};   //

//    private SocketUtil socketUtil;
    private String ipaddr = "10.10.100.254";
    private int portnum=8899;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示：或者xml文件节点下android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_bed);
        ButterKnife.bind(this);


        btnTurnleft= (ImageButton) findViewById(R.id.btn_turnleft);
        btnTurnright= (ImageButton) findViewById(R.id.btn_turnright);
        btnLiftfoot= (ImageButton) findViewById(R.id.btn_liftfoot);
        btnStayfoot= (ImageButton) findViewById(R.id.btn_stayfoot);
        btnAutioA= (ImageButton) findViewById(R.id.btn_autioA);
        btnAutioB= (ImageButton) findViewById(R.id.btn_autioB);
        btnSitup= (ImageButton) findViewById(R.id.btn_situp);
        btnLiedown= (ImageButton) findViewById(R.id.btn_liedown);
        btnOpenBedpan= (ImageButton) findViewById(R.id.btn_open_bedpan);
        btnCloseBedpan= (ImageButton) findViewById(R.id.btn_close_bedpan);
        btnReset= (ImageButton) findViewById(R.id.btn_reset);



        btnTurnleft.setOnClickListener(this);
        btnTurnright.setOnClickListener(this);
        btnLiftfoot.setOnClickListener(this);
        btnStayfoot.setOnClickListener(this);
        btnAutioA.setOnClickListener(this);
        btnAutioB.setOnClickListener(this);
        btnSitup.setOnClickListener(this);
        btnLiedown.setOnClickListener(this);
        btnOpenBedpan.setOnClickListener(this);
        btnCloseBedpan.setOnClickListener(this);
        btnReset.setOnClickListener(this);


        //点击控件震动效果
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //处理socket连接状态
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case -1:
                        Toast.makeText(BedActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(BedActivity.this,"网络连接异常",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(BedActivity.this,"网络连接成功",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        /**
         * 进入界面，提示连接状态
         */
        Message msg = new Message();
        int connect = SocketUtil.connectStaus;
        switch (connect) {
            case 0:
                msg.what = 0;
                myHandler.sendMessage(msg);
                break;
            case -1:
                msg.what = -1;
                myHandler.sendMessage(msg);
                break;
            case 1:
                msg.what = 1;
                myHandler.sendMessage(msg);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_turnleft:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData1);
                break;
            case R.id.btn_turnright:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData2);
                break;
            case R.id.btn_situp:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData3);
                break;
            case R.id.btn_liedown:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData4);
                break;
            case R.id.btn_liftfoot:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData5);
                break;
            case R.id.btn_stayfoot:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData6);
                break;
            case R.id.btn_open_bedpan:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData7);
                break;
            case R.id.btn_close_bedpan:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData8);
                break;
            case R.id.btn_autioA:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData9);
                break;
            case R.id.btn_autioB:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData10);
                break;
            case R.id.btn_reset:
                vibrator.vibrate(100);
                if(SocketUtil.connectStaus!=1){
                    Toast.makeText(BedActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketUtil.SendData(SendData11);
                break;

        }
    }
}
