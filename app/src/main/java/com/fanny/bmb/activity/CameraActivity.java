package com.fanny.bmb.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.fanny.bmb.R;
import com.fanny.bmb.adapter.SearchListAdapter;
import com.fanny.bmb.service.BridgeService;
import com.fanny.bmb.util.AudioPlayer;
import com.fanny.bmb.util.ContentCommon;
import com.fanny.bmb.util.CustomAudioRecorder;
import com.fanny.bmb.util.CustomBuffer;
import com.fanny.bmb.util.CustomBufferData;
import com.fanny.bmb.util.CustomBufferHead;
import com.fanny.bmb.util.SystemValue;
import com.fanny.bmb.view.MyRender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vstc2.nativecaller.NativeCaller;


/**
 * Created by Fanny on 17/4/12.
 */

public class CameraActivity extends Activity implements View.OnClickListener
        , BridgeService.AddCameraInterface, BridgeService.IpcamClientInterface,
        BridgeService.CallBackMessageInterface ,BridgeService.PlayInterface,CustomAudioRecorder.AudioRecordResult{

    private WifiManager manager = null;
    private ImageButton btn_connect;
    private ImageButton btn_cameraview;
    private ImageButton btn_search;
    private MyBroadCast receiver;
    private Intent intentBrod = null;
    private GLSurfaceView mySurface = null;
    private MyRender myRender;
    private int option = ContentCommon.INVALID_OPTION;
    private String strUser;
    private String strDID;
    private String strPwd;
    private SearchListAdapter searchListAdapter;

    private static final int AUDIO_BUFFER_START_CODE=0xff00ff;
    //视频数据部分
    private byte[] videodata=null;
    private int videoDataLen=0;
    public int nVideoWidths=0;
    public int nVideoHeights=0;
    //相关参数
    private final int BRIGHT=1;//亮度标志
    private final int CONTRAST=2;//对比度标志
    private final int IR_STATE=14;//IR夜视
    private int nResolution=0;//分辨率值
    private int nBrightness=0;//亮度值
    private int nContrast=0;//对比度



    private int nStreamCodeType;//分辨率格式

    //?
    private String stqvga = "qvga";
    private String stvga = "vga";
    private String stqvga1 = "qvga1";
    private String stvga1 = "vga1";
    private String stp720 = "p720";
    private String sthigh = "high";
    private String stmiddle ="middle";
    private String stmax = "max";

    //分辨率标识符
    private boolean ismax = false;
    private boolean ishigh = false;
    private boolean isp720 = false;
    private boolean ismiddle = false;
    private boolean isqvga1 = false;
    private boolean isvga1 = false;
    private boolean isqvga = false;
    private boolean isvga = false;

    private Animation showAnim;
    private boolean isTakepic = false;
    private boolean isPictSave = false;
    private boolean isTalking = false;//是否在说话
    //    private boolean isMcriophone = false;//是否在
    //视频录像方法
//    private CustomVideoRecord myvideoRecorder;
    public boolean isH264 = false;//是否是H264格式标志
    public boolean isJpeg=false;
    private boolean isTakeVideo = false;
    private long videotime = 0;// 录每张图片的时间

    private Animation dismissAnim;
    private int timeTag = 0;
    private int timeOne = 0;
    private int timeTwo = 0;
    private ImageButton button_back;
    private BitmapDrawable drawable = null;
    //    private boolean bAudioRecordStart = false;
    //送话器
    private CustomAudioRecorder customAudioRecorder;



    //镜像标志
    private boolean m_bUpDownMirror;
    private boolean m_bLeftRightMirror;


    private int i=0;//拍照张数标志
    private ImageButton btn_say;
    private ImageButton btn_hear;
    private ImageButton btn_up_control;
    private ImageButton btn_down_control;
    private ImageButton btn_right_control;
    private ImageButton btn_left_control;


    //默认视频参数
    private void defaultVideoParams(){
        nBrightness=1;
        nContrast=128;
        NativeCaller.PPPPCameraControl(strDID,1,0);
        NativeCaller.PPPPCameraControl(strDID,2,128);

    }

    //设置视频可见
    private void SetViewVisible(){
//        videoViewPortrait.setVisibility(View.VISIBLE);
//        videoViewStandard.setVisibility(View.VISIBLE);
        getCameraParams();
    }


    /**
     * 获取resolution
     */
    public static Map<String,Map<Object,Object>> reslutionlist=new HashMap<String,Map<Object,Object>>();

    /**
     *
     * @param Resolution
     */
    protected void setResolution(int Resolution){
        NativeCaller.PPPPCameraControl(strDID,16,Resolution);
    }


    /**
     * 增加reslution
     * @param mess
     * @param isfast
     */
    private void addReslution(String mess, boolean isfast) {
        if (reslutionlist.size() != 0) {
            if (reslutionlist.containsKey(strDID)) {
                reslutionlist.remove(strDID);
            }
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(mess, isfast);
        reslutionlist.put(strDID, map);
    }

    /**
     * 设置分辨率
     */
    private void getReslution() {
        if (reslutionlist.containsKey(strDID)) {
            Map<Object, Object> map = reslutionlist.get(strDID);
            if (map.containsKey("qvga")) {
                isqvga = true;
            } else if (map.containsKey("vga")) {
                isvga = true;
            } else if (map.containsKey("qvga1")) {
                isqvga1 = true;
            } else if (map.containsKey("vga1")) {
                isvga1 = true;
            } else if (map.containsKey("p720")) {
                isp720 = true;
            } else if (map.containsKey("high")) {
                ishigh = true;
            } else if (map.containsKey("middle")) {
                ismiddle = true;
            } else if (map.containsKey("max")) {
                ismax = true;
            }
        }
    }

    private Bitmap mBmp;
    private ImageView videoViewPortrait;
    private ImageView videoViewStandard;
    /**
     * 处理视频数据的handler
     */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 || msg.what == 2) {
                SetViewVisible();
            }
//            if (!isPTZPrompt)
//            {
//                isPTZPrompt = true;
//                showToast(R.string.ptz_control);
//            }
            int width = getWindowManager().getDefaultDisplay().getWidth();
            int height = getWindowManager().getDefaultDisplay().getHeight();
            switch (msg.what) {
                case 1: // h264
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 0) {
                            ismax = true;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmax, ismax);
                        } else if (nResolution == 1) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = true;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(sthigh, ishigh);
                        } else if (nResolution == 2) {
                            ismax = false;
                            ismiddle = true;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stmiddle, ismiddle);
                        } else if (nResolution == 3) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = true;
                            isqvga1 = false;
                            isvga1 = false;
                            addReslution(stp720, isp720);
                            nResolution = 3;
                        } else if (nResolution == 4) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = false;
                            isvga1 = true;
                            addReslution(stvga1, isvga1);
                        } else if (nResolution == 5) {
                            ismax = false;
                            ismiddle = false;
                            ishigh = false;
                            isp720 = false;
                            isqvga1 = true;
                            isvga1 = false;
                            addReslution(stqvga1, isqvga1);
                        }
                    } else {
                        if (reslutionlist.containsKey(strDID))
                        {
                            getReslution();
                        } else {
                            if (nResolution == 0) {
                                ismax = true;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmax, ismax);
                            } else if (nResolution == 1) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = true;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(sthigh, ishigh);
                            } else if (nResolution == 2) {
                                ismax = false;
                                ismiddle = true;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stmiddle, ismiddle);
                            } else if (nResolution == 3) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = true;
                                isqvga1 = false;
                                isvga1 = false;
                                addReslution(stp720, isp720);
                                nResolution = 3;
                            } else if (nResolution == 4) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = false;
                                isvga1 = true;
                                addReslution(stvga1, isvga1);
                            } else if (nResolution == 5) {
                                ismax = false;
                                ismiddle = false;
                                ishigh = false;
                                isp720 = false;
                                isqvga1 = true;
                                isvga1 = false;
                                addReslution(stqvga1, isqvga1);
                            }
                        }

                    }

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                width, width * 3 / 4);
                        lp.gravity = Gravity.CENTER;
                        mySurface.setLayoutParams(lp);
                    }
                    else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                width, height);
                        lp.gravity = Gravity.CENTER;
                        mySurface.setLayoutParams(lp);
                    }
                    myRender.writeSample(videodata, nVideoWidths, nVideoHeights);
                }
                break;
                case 2: // JPEG
                {
                    if (reslutionlist.size() == 0) {
                        if (nResolution == 1) {
                            isvga = true;
                            isqvga = false;
                            addReslution(stvga, isvga);
                        } else if (nResolution == 0) {
                            isqvga = true;
                            isvga = false;
                            addReslution(stqvga, isqvga);
                        }
                    } else {
                        if (reslutionlist.containsKey(strDID)) {
                            getReslution();
                        } else {
                            if (nResolution == 1) {
                                isvga = true;
                                isqvga = false;
                                addReslution(stvga, isvga);
                            } else if (nResolution == 0) {
                                isqvga = true;
                                isvga = false;
                                addReslution(stqvga, isqvga);
                            }
                        }
                    }
                    mBmp = BitmapFactory.decodeByteArray(videodata, 0,
                            videoDataLen);
                    if (mBmp == null) {
                        bDisplayFinished = true;
                        return;
                    }
//                    if (isTakepic) {
//                        takePicture(mBmp);
//                        isTakepic = false;
//                    }
                    nVideoWidths = mBmp.getWidth();
                    nVideoHeights = mBmp.getHeight();

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        // Bitmap
                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, width,
                                width * 3 / 4, true);
                        //videoViewLandscape.setVisibility(View.GONE);
                        videoViewPortrait.setVisibility(View.VISIBLE);
                        videoViewPortrait.setImageBitmap(bitmap);

                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Bitmap bitmap = Bitmap.createScaledBitmap(mBmp, width,height, true);
                        videoViewPortrait.setVisibility(View.GONE);
                        //videoViewLandscape.setVisibility(View.VISIBLE);
                        //videoViewLandscape.setImageBitmap(bitmap);
                    }

                }
                break;
                default:
                    break;
            }
            if (msg.what == 1 || msg.what == 2)
            {
                bDisplayFinished = true;
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        searchListAdapter=new SearchListAdapter(this);

        //初始化surfaceview
        mySurface = (GLSurfaceView) findViewById(R.id.mysurfaceview);
        myRender = new MyRender(mySurface);
        mySurface.setRenderer(myRender);

        btn_connect = (ImageButton) findViewById(R.id.myconnect);
        btn_cameraview= (ImageButton) findViewById(R.id.mycameraview);
        btn_search= (ImageButton) findViewById(R.id.mysearch);

        btn_say = (ImageButton) findViewById(R.id.mysay);
        btn_hear = (ImageButton) findViewById(R.id.myhear);
        btn_up_control = (ImageButton) findViewById(R.id.upCtl);
        btn_down_control = (ImageButton) findViewById(R.id.downCtl);
        btn_left_control= (ImageButton) findViewById(R.id.leftCtl);
        btn_right_control= (ImageButton) findViewById(R.id.rightCtl);

        AudioBuffer=new CustomBuffer();
        audioPlayer=new AudioPlayer(AudioBuffer);
        customAudioRecorder=new CustomAudioRecorder(this);

        //初始化wifi管理
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        btn_connect.setOnClickListener(this);
        btn_cameraview.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        btn_say.setOnClickListener(this);
        btn_hear.setOnClickListener(this);

        btn_up_control.setOnClickListener(this);
        btn_down_control.setOnClickListener(this);
        btn_left_control.setOnClickListener(this);
        btn_right_control.setOnClickListener(this);

        videoViewPortrait= (ImageView) findViewById(R.id.videoview);
        videoViewStandard= (ImageView) findViewById(R.id.videoview_standard);

        //1.启动service服务
        Intent in=new Intent();
        in.setClass(this,BridgeService.class);
        startService(in);
        //2. 初始化服务器.注意：耗时操作后期要放在子线程中
        NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
//            }
//        }).start();

        BridgeService.setAddCameraInterface(this);
        BridgeService.setCallBackMessage(this);



//        receiver = new MyBroadCast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("finish");
//        registerReceiver(receiver, filter);
//        intentBrod = new Intent("drop");


    }
    private boolean isLeftRight=false;
    private boolean isUpDown=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mysearch:
                beginSearch();
                break;

            case R.id.myconnect:
                //点击连接按钮后，2.初始化服务器
                connect();

                break;
            case R.id.mycameraview:
                BridgeService.setPlayInterface(this);
                //开启视频流画面
                NativeCaller.StartPPPPLivestream(strDID,10,1);
                getCameraParams();
                break;

            case R.id.upCtl:
                isUpDown=true;
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_DOWN_STOP);
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_UP);
                break;
            case R.id.downCtl:
                isUpDown=false;
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_UP_STOP);
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_DOWN);
                break;
            case R.id.leftCtl:
                isLeftRight=true;
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_RIGHT_STOP);
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_LEFT);
                break;
            case R.id.rightCtl:
                isLeftRight=false;
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_LEFT_STOP);
                NativeCaller.PPPPPTZControl(strDID,ContentCommon.CMD_PTZ_RIGHT);
                break;

            case R.id.mysay:
                goMicroPhone();
                break;
            case R.id.myhear:
                goAudio();
                break;
        }

    }

    private CustomBuffer AudioBuffer=null;
    private AudioPlayer audioPlayer=null;
    private boolean bAudioStart=false;
    /**
     * 对讲   相当于在手机端录音，并发送出去  AudioRecord类
     */
    private boolean bAudioRecordStart=false;
    private boolean isTaliking=false;//是否在说话
    private boolean isMcriophone=false;//是否在
    private void goMicroPhone() {
        if(!isTaliking){
            if(bAudioRecordStart){
                //停止讲话
                isMcriophone=false;
                bAudioRecordStart=false;
                btn_say.setSelected(false);
                StopTalk();
            }else {
                //开始讲话
                isMcriophone=true;
                bAudioRecordStart=true;
                btn_say.setSelected(true);
                StartTalk();
            }

        }else {
            isTaliking=false;
            bAudioStart=false;
            btn_hear.setSelected(false);
            StopAudio();
            isMcriophone=true;
            bAudioRecordStart=true;
            btn_say.setSelected(true);
            StartTalk();
        }

    }

    private void StartTalk(){
        if(customAudioRecorder!=null){
            customAudioRecorder.StartRecord();
            NativeCaller.PPPPStartTalk(strDID);
        }

    }
    private void StopTalk(){
        if(customAudioRecorder!=null){
            customAudioRecorder.StopRecord();
            NativeCaller.PPPPStopTalk(strDID);
        }

    }

    /**
     * 监听  相当于在手机端，获取语音播放 AudioTrack类
     */
    private void goAudio() {
        if(!isMcriophone){
            if(bAudioStart){
                isTaliking=false;
                bAudioStart=false;
                btn_hear.setSelected(false);
                StopAudio();
            }else {
                isTaliking=true;
                bAudioStart=true;
                btn_hear.setSelected(true);
                StartAudio();
            }
        }else {
            isMcriophone=false;
            bAudioRecordStart=false;
            btn_say.setSelected(false);
            StopTalk();
            isTaliking=true;
            bAudioStart=true;
            btn_hear.setSelected(true);
            StartAudio();
        }

    }

    private void StartAudio(){
        synchronized (this){
            AudioBuffer.ClearAll();
            audioPlayer.AudioPlayStart();
            NativeCaller.PPPPStartAudio(strDID);
        }

    }

    private void StopAudio(){
        synchronized (this){
            audioPlayer.AudioPlayStop();
            AudioBuffer.ClearAll();
            NativeCaller.PPPPStopAudio(strDID);
        }

    }


    //创建客户端与摄像头连接
    private void connect() {
        //用户信息
        strUser = "admin";
        strDID = "VSTA-368340-HWBMZ";
        strPwd = "888888";

        Intent intent = new Intent();

        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }
        int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
        intent.putExtra(ContentCommon.CAMERA_OPTION, option);
        intent.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
        intent.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
        intent.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
        intent.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);

        SystemValue.deviceName = strUser;
        SystemValue.deviceId = strDID;
        SystemValue.devicePass = strPwd;

        BridgeService.setIpcamClientInterface(this);
        //3.初始化回调
        NativeCaller.Init();



        //开启p2p连接
        new Thread(new StartPPPPThread()).start();

        //以下方法可以返回连接状态i
        int i = NativeCaller.PPPPCameraStatus(strDID, 0x7F);
        Log.e("status", String.valueOf(i));

    }




    private class StartPPPPThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                startCameraPPPP();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startCameraPPPP() {
        if (SystemValue.deviceId.toLowerCase().startsWith("vsta")) {

            NativeCaller.StartPPPPExt(SystemValue.deviceId, SystemValue.deviceName,
                    SystemValue.devicePass, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK");

            Log.e("MainActivity","lianjie1");
        } else {
            NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName, SystemValue.devicePass, 1, "");
            Log.e("MainActivity","lianjie2");
        }
    }

    private void stopCameraPPPP() {
        NativeCaller.StopPPPP(SystemValue.deviceId);
    }

    private class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ip", "MainActivity.this.finish()");
        }
    }



    private void getCameraParams() {
        NativeCaller.PPPPGetSystemParams(strDID, ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);

    }

    private void beginSearch() {
        //先断开连接
        stopCameraPPPP();
        SystemValue.deviceId = null;
        searchCamera();
    }

    private void searchCamera() {
        startSearch();
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("搜寻结果");
        dialog.setPositiveButton("refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSearch();
            }
        });
        dialog.setNegativeButton("cancle",null);
        dialog.setAdapter(searchListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String,Object> mapItem=searchListAdapter.getItemContent(which);
                if(mapItem==null){
                    return;
                }
            }
        });
        dialog.show();

    }

    private void startSearch() {
        new Thread(new SearchThread()).start();
    }

    private class SearchThread implements Runnable {
        @Override
        public void run() {
            NativeCaller.StartSearch();
            Log.e("MainActivity", "startSearch");
        }
    }


    /**
     *
     * @param did
     * @param type
     * @param param
     */

    @Override
    public void BSMsgNotifyData(String did, int type, int param) {
        Log.e("bsmsgNotifydata","type:"+type+" param:"+param);
    }

    @Override
    public void BSSnapshotNotify(String did, byte[] bImage, int len) {

    }

    @Override
    public void callBackUserParams(String did, String user1, String pwd1, String user2, String pwd2, String user3, String pwd3) {

    }

    @Override
    public void CameraStatus(String did, int status) {

    }

    /**
     * BridgeService Callback
     *
     * @param cameraType
     * @param strMac
     * @param strName
     * @param strDeviceID
     * @param strIpAddr
     * @param port
     */
    @Override
    public void callBackSearchResultData(int cameraType, String strMac, String strName, String strDeviceID, String strIpAddr, int port) {
        Log.e("SearchResult", strDeviceID + strName);
        if(searchListAdapter.AddCamera(strMac,strName,strDeviceID)){
            return;
        }
    }

    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {
        if(cmd==ContentCommon.CGI_IEGET_STATUS){
            Log.e("resultPbuf","values:"+resultPbuf);
        }
    }



    /**
     * playinterface的回调方法
     * @param did
     * @param resolution
     * @param brightness
     * @param contrast
     * @param hue
     * @param saturation
     * @param flip
     * @param mode
     */
    //视频参数回调
    private boolean bInitCameraParam=false;
    @Override
    public void callBackCameraParamNotify(String did, int resolution, int brightness, int contrast, int hue, int saturation, int flip, int mode) {
        Log.e("设备返回的参数：",resolution+","+brightness+","+contrast+","+hue+","+saturation+","+flip+","+mode);
        nBrightness=brightness;
        nContrast=contrast;
        nResolution=resolution;
        bInitCameraParam=true;

    }

    private boolean bDisplayFinished=true;
    /**
     * 视频数据流回调   另外在此回到方法内处理拍照和录像
     * @param videobuf 一帧视频数据
     * @param h264Data 0:普请摄像数据  1:高清摄像数据
     * @param len 一帧数据大小
     * @param width 一帧数据宽
     * @param height 一帧数据高
     */
    @Override
    public void callBackVideoData(byte[] videobuf, int h264Data, int len, int width, int height) {

        if(!bDisplayFinished) return;
        bDisplayFinished=false;
        videodata=videobuf;
        videoDataLen=len;
        Message msg=new Message();
        if(h264Data==1){
            nVideoWidths=width;
            nVideoHeights=height;
//            if(isTakepic){
//
//            }
            isH264=true;
            msg.what=1;
        }else {
            isJpeg=true;
            msg.what=2;
        }
        mHandler.sendMessage(msg);

//        if(isTakeVideo){
//
//        }
    }

    @Override
    public void callBackMessageNotify(String did, int msgType, int param) {
        if(msgType==ContentCommon.PPPP_MSG_TYPE_STREAM){
            //设置分辨率格式
            nStreamCodeType=param;
            return;
        }
        if(msgType== ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS){
            return;
        }
        if(!did.equals(strDID)){
            return;
        }
        //其余情况下，通知ui已掉线，写一个handler：



    }

    /**
     * 监听语音播放数据回调数据
     * @param pcm
     * @param len
     */
    @Override
    public void callBackAudioData(byte[] pcm, int len) {
        if(!audioPlayer.isAudioPlaying()){
            return;
        }
        CustomBufferHead head=new CustomBufferHead();
        CustomBufferData data=new CustomBufferData();
        head.length=len;
        head.startcode=AUDIO_BUFFER_START_CODE;
        data.head=head;
        data.data=pcm;
        AudioBuffer.addData(data);
    }

    @Override
    public void callBackH264Data(byte[] h264, int type, int size) {

    }

    /**
     * 对讲数据回调
     * @param data
     * @param len
     */
    @Override
    public void AudioRecordData(byte[] data, int len) {
        if(bAudioRecordStart && len >0){
            NativeCaller.PPPPTalkAudioData(strDID,data,len);
        }
    }

    //定义录像接口
    public void setVideoRecord(VideoRecorder videoRecord){
        this.videoRecorder=videoRecord;
    }
    public VideoRecorder videoRecorder;
    public interface VideoRecorder{
        abstract public void VideoRecordData(int type,byte[]videodata,int width,int height,int time);
    }


    @Override
    protected void onDestroy() {
        NativeCaller.StopPPPPLivestream(strDID);
        StopAudio();
        StopTalk();
        super.onDestroy();
    }


}
