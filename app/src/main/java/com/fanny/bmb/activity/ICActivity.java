package com.fanny.bmb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.fanny.bmb.R;
import com.fanny.bmb.fragment.UserFragment;
import com.fanny.bmb.util.SocketUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Fanny on 17/4/12.
 */

public class ICActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.btn_mode)
    ImageButton btnMode;
    @BindView(R.id.btn_param)
    ImageButton btnParam;
    @BindView(R.id.btn_clear)
    ImageButton btnClear;
    @BindView(R.id.btn_state)
    ImageButton btnState;
    @BindView(R.id.btn_stop)
    ImageButton btnStop;
    @BindView(R.id.btn_record)
    ImageButton btnRecord;
    @BindView(R.id.tv_reply)
    TextView tvReply;
    @BindView(R.id.step_view)
    HorizontalStepView stepView;
    @BindView(R.id.im_waterlevel)
    ImageView imWaterlevel;
    @BindView(R.id.im_press)
    ImageView imPress;
    @BindView(R.id.im_waterwarn)
    ImageView imWaterwarn;
    @BindView(R.id.im_windwarn)
    ImageView imWindwarn;
    @BindView(R.id.im_watertemp)
    ImageView imWatertemp;
    @BindView(R.id.im_windtemp)
    ImageView imWindtemp;
    @BindView(R.id.tv_waterlevel)
    TextView tvWaterlevel;
    @BindView(R.id.tv_press)
    TextView tvPress;
    @BindView(R.id.tv_waterwarn)
    TextView tvWaterwarn;
    @BindView(R.id.tv_windwarn)
    TextView tvWindwarn;
    @BindView(R.id.tv_watertemp)
    TextView tvWatertemp;
    @BindView(R.id.tv_windtemp)
    TextView tvWindtemp;
    @BindView(R.id.im_flag)
    ImageView imFlag;


    private String ipaddr = "10.10.100.254";
    private int portnum = 8899;

    byte[] buff = new byte[16];
    byte[] sentbuff = new byte[16];
    byte[] bytetobit = new byte[8];
    byte[] bytstr = new byte[1];
    String[] opraname = new String[6];
    int[] runstflag = new int[13];
    int snum = 0;

    private ImageButton imReply;
    private EditText waterTemp;
    private EditText windTemp;
    private RadioGroup rdg;
    private View view;
    private View view1;
    private View view3;
    private ImageView imPres;
    private ImageView imWash;
    private ImageView imPush;
    private ImageView imAirpump;
    private ImageView imWaterpump;
    private ImageView imDust;
    private ImageView imHeater;
    private ImageView imWarmheat;
    private HorizontalStepView stepView1;
    private List<StepBean> stepBeanList;
    private StepBean stepBean0;
    private StepBean stepBean1;
    private StepBean stepBean2;
    private StepBean stepBean3;
    private StepBean stepBean4;
    private StepBean stepBean5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ic);
        ButterKnife.bind(this);

        //处理socket连接状态
        Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        Toast.makeText(ICActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(ICActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(ICActivity.this, "网络连接成功", Toast.LENGTH_SHORT).show();
//                        //实时接受服务器端的数据
                        recDataParam();
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

        /**
         * findview
         */
        stepView1 = (HorizontalStepView) findViewById(R.id.step_view);
        stepBeanList = new ArrayList<>();
        stepBean0 = new StepBean("等待开始", 1);
        stepBean1 = new StepBean("设备准备", 1);
        stepBean2 = new StepBean("排便过程", 0);
        stepBean3 = new StepBean("便后清洗", -1);
        stepBean4 = new StepBean("便后烘干", -1);
        stepBean5 = new StepBean("设备清理", -1);
        stepBeanList.add(stepBean0);
        stepBeanList.add(stepBean1);
        stepBeanList.add(stepBean2);
        stepBeanList.add(stepBean3);
        stepBeanList.add(stepBean4);
        stepBeanList.add(stepBean5);

        stepView1.setStepViewTexts(stepBeanList)
                .setTextSize(13)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.colorFreshGreen))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.material_blue_grey_95))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.material_blue_grey_95))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.success))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.process))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.information));//设置StepsViewIndicator AttentionIcon

        btnClear = (ImageButton) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnMode = (ImageButton) findViewById(R.id.btn_mode);
        btnMode.setOnClickListener(this);
        btnParam = (ImageButton) findViewById(R.id.btn_param);
        btnParam.setOnClickListener(this);
        btnState = (ImageButton) findViewById(R.id.btn_state);
        btnState.setOnClickListener(this);
        btnStop = (ImageButton) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(this);
        btnRecord = (ImageButton) findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(this);
//        tvReply = (TextView) findViewById(R.id.tv_reply);
//        tvReply.setOnClickListener(this);
        imReply= (ImageButton) findViewById(R.id.im_reply);
        imReply.setOnClickListener(this);

        tvWaterlevel = (TextView) findViewById(R.id.tv_waterlevel);
        imWaterlevel = (ImageView) findViewById(R.id.im_waterlevel);

        tvPress = (TextView) findViewById(R.id.tv_press);
        imPress = (ImageView) findViewById(R.id.im_press);

        tvWaterwarn = (TextView) findViewById(R.id.tv_waterwarn);
        imWaterwarn = (ImageView) findViewById(R.id.im_waterwarn);

        tvWindwarn = (TextView) findViewById(R.id.tv_windwarn);
        imWindwarn = (ImageView) findViewById(R.id.im_windwarn);

        tvWatertemp = (TextView) findViewById(R.id.tv_watertemp);
        imWatertemp = (ImageView) findViewById(R.id.im_watertemp);

        tvWindtemp = (TextView) findViewById(R.id.tv_watertemp);
        imWindtemp = (ImageView) findViewById(R.id.im_windtemp);

        imFlag= (ImageView) findViewById(R.id.im_flag);

        /**
         * 初始化设备数据
         */
        initData();

    }


    boolean bstat1 = true;
    boolean bstat2 = true;
    boolean bstat3 = true;
    boolean bstat4 = true;

    private boolean revctrl = true;
    private boolean showflag = false;
    private boolean checkflag = false;

    public boolean isRunning = true;
    public boolean tsflag = true;
    char checkXOR;
    int testint = 0;
    int runflag = 0;
    int lcflag = 1;
    int autoflag = 1;
    int amflag = 0;

    /**
     * 界面数据显示模块
     */
    private void recDataParam() {
        /**
         * 显示报警信息
         */

        bytetobit = getbit(buff[5]);
        //水位报警
        if (bytetobit[2] == 1) {
            tvWaterlevel.setTextColor(Color.rgb(0, 0, 0));
            imWaterlevel.setImageDrawable(getResources().getDrawable(R.mipmap.waterok));
        } else {
            tvWaterlevel.setTextColor(Color.rgb(255, 0, 0));
            //图片闪烁
            if (bstat1) {
                imWaterlevel.setImageDrawable(getResources().getDrawable(R.mipmap.watererr));
            } else {
                imWaterlevel.setImageDrawable(getResources().getDrawable(R.mipmap.waterok));
            }
            bstat1 = !bstat1;
        }
        //压力报警
        if (bytetobit[1] == 1) {
            tvPress.setTextColor(Color.rgb(0, 0, 0));
            imPress.setImageDrawable(getResources().getDrawable(R.mipmap.pressok));
        } else {
            tvPress.setTextColor(Color.rgb(255, 0, 0));
            //图片闪烁
            if (bstat1) {
                imPress.setImageDrawable(getResources().getDrawable(R.mipmap.presserr));
            } else {
                imPress.setImageDrawable(getResources().getDrawable(R.mipmap.pressok));
            }
            bstat2 = !bstat2;
        }

        bytetobit = getbit(buff[9]);
        //水温报警
        if (bytetobit[0] == 1) {
            tvWaterwarn.setTextColor(Color.rgb(255, 0, 0));
            if (bstat3) {
                imWaterwarn.setImageDrawable(getResources().getDrawable(R.mipmap.wtemperr));
            } else {
                imWaterwarn.setImageDrawable(getResources().getDrawable(R.mipmap.wtempok));
            }
            bstat3 = !bstat3;
        } else {
            tvWaterwarn.setTextColor(Color.rgb(0, 0, 0));
            imWaterwarn.setImageDrawable(getResources().getDrawable(R.mipmap.wtempok));
        }
        //气温报警
        if (bytetobit[2] == 1) {
            tvWindwarn.setTextColor(Color.rgb(255, 0, 0));
            if (bstat4) {
                imWindwarn.setImageDrawable(getResources().getDrawable(R.mipmap.winderr));
            } else {
                imWindwarn.setImageDrawable(getResources().getDrawable(R.mipmap.windok));
            }
            bstat4 = !bstat4;
        } else {
            tvWindwarn.setTextColor(Color.rgb(0, 0, 0));
            imWindwarn.setImageDrawable(getResources().getDrawable(R.mipmap.windok));
        }


        /**
         * 显示设备运行状态
         */

        bytetobit = getbit(buff[6]);  //负压

        if (bytetobit[1] == 1) {

            runstflag[5] = 1;

            //	imview5.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[5] = 0;
        //	imview5.setImageDrawable(getResources().getDrawable(R.drawable.off));


        if (bytetobit[0] == 1) {     //气泵

            runstflag[8] = 1;

            //	imview8.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[8] = 0;
        //	imview8.setImageDrawable(getResources().getDrawable(R.drawable.off));

        if (bytetobit[2] == 1) {     //水泵

            runstflag[9] = 1;
            //	imview9.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[9] = 0;
        //	imview9.setImageDrawable(getResources().getDrawable(R.drawable.off));


        if (bytetobit[4] == 1) {     //污阀

            runstflag[10] = 1;
            //	imview10.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[10] = 0;
        //imview10.setImageDrawable(getResources().getDrawable(R.drawable.off));

        if (bytetobit[3] == 1) {     //热风

            runstflag[12] = 1;
            //imview12.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[12] = 0;
        //	imview12.setImageDrawable(getResources().getDrawable(R.drawable.off));


        bytetobit = getbit(buff[9]);  //冲洗
        if (bytetobit[6] == 1) {

            runstflag[6] = 1;
            //imview6.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[6] = 0;
        //	imview6.setImageDrawable(getResources().getDrawable(R.drawable.off));

        bytetobit = getbit(buff[10]);  //冲桶
        if (bytetobit[0] == 1) {

            runstflag[7] = 1;
            //	imview7.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[7] = 0;
        //	imview7.setImageDrawable(getResources().getDrawable(R.drawable.off));

        bytetobit = getbit(buff[7]);  //水加热
        if (bytetobit[3] == 1) {

            runstflag[11] = 1;
            //imview11.setImageDrawable(getResources().getDrawable(R.drawable.on));
        } else runstflag[11] = 0;
        //	imview11.setImageDrawable(getResources().getDrawable(R.drawable.off));


        tvWatertemp.setText(String.valueOf(buff[13]));
        tvWindtemp.setText(String.valueOf(buff[14]));


        /**
         * 远程或者手动操作的过程显示：
         */
        bytetobit = getbit(buff[10]);
        if (bytetobit[1] == 1) {        //远程

            lcflag = 0;
        } else    //本地
        {
            lcflag = 1;
        }

        bytetobit = getbit(buff[8]);
        if (bytetobit[0] == 1) {        //手动

            autoflag = 0;
            if (lcflag == 0) {   //远程.手动

                imFlag.setImageDrawable(getResources().getDrawable(R.mipmap.manual));
                amflag = 1;

            } else    //本地.手动
            {
                imFlag.setImageDrawable(getResources().getDrawable(R.mipmap.offline));

                amflag = 0;

            }
        } else {                      //自动
            autoflag = 1;
            if (lcflag == 0) {   //远程.自动

                imFlag.setImageDrawable(getResources().getDrawable(R.mipmap.auto));
                amflag = 2;


            } else    //本地.自动
            {
                imFlag.setImageDrawable(getResources().getDrawable(R.mipmap.offline));
                amflag = 0;

            }
        }


        runflag = 0;
        bytetobit = getbit(buff[9]);  //设备准备
        if (bytetobit[4] == 1) {

            //		tv2.setText(opraname[1]);
            //		tv3.setText(opraname[2]);
            runflag = 1;
            tvReply.setText("准备中");

            imReply.setEnabled(false);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);

            if (showflag) {
                stepBeanList.get(1).setState(1);
            }
            else {
                stepBeanList.get(1).setState(0);
            }
        }
        if (bytetobit[5] == 1) {    //排便过程

            //	tv2.setText(opraname[2]);
            //	tv3.setText(opraname[3]);
            runflag = 1;
            tvReply.setText("大便结束");

            imReply.setEnabled(true);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);
            snum = 1;

            if (showflag) {

                stepBeanList.get(2).setState(1);
            }
            else {
                stepBeanList.get(2).setState(0);
            }
        }
        if (bytetobit[6] == 1) {    //便后清洗

            //		tv2.setText(opraname[3]);
            //		tv3.setText(opraname[4]);
            runflag = 1;
            tvReply.setText("清洗中");

            imReply.setEnabled(false);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);
            if (showflag) {
                stepBeanList.get(3).setState(1);
            }
            else {
                stepBeanList.get(3).setState(0);
            }
        }
        if (bytetobit[7] == 1) {    //烘干

            //		tv2.setText(opraname[4]);
            //		tv3.setText(opraname[5]);
            runflag = 1;
            tvReply.setText("烘干结束");

            imReply.setEnabled(true);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);
            snum = 2;
            if (showflag) {
                stepBeanList.get(4).setState(1);
            }
            else {
                stepBeanList.get(4).setState(0);
            }
        }

        bytetobit = getbit(buff[10]);

        if (bytetobit[0] == 1) {    //设备清理

            //		tv2.setText(opraname[5]);
            //		tv3.setText(opraname[0]);

            tvReply.setText("清理中");
            imReply.setEnabled(false);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);

            runflag = 1;
            if (showflag) {
                stepBeanList.get(5).setState(1);
            }
            else {
                stepBeanList.get(5).setState(0);
            }
        }
        if (runflag == 0) {

            tvReply.setText("设备准备");
            //		tv2.setText(opraname[0]);
            //		tv3.setText(opraname[1]);

            if (autoflag == 1) {   //自动模式  设备准备无法人工操作

                imReply.setEnabled(false);
            }
            else {
                imReply.setEnabled(true);
            }
            btnMode.setEnabled(true);
            btnParam.setEnabled(true);
            snum = 0;
            if (showflag) {
                stepBeanList.get(0).setState(1);
            }
            else {
                stepBeanList.get(0).setState(0);
            }
        }


        if (lcflag == 1) {   //模式为本地时远程无法操作

            imReply.setEnabled(false);
            btnMode.setEnabled(false);
            btnParam.setEnabled(false);
        }

        //	tv4.setText(str);
        //	tv5.setText(88);



        //	tv_recaz.setText(msg.obj.toString());

        if ((buff[9]& 0x80) == 0x80) {

        }

    }


    private byte[] getbit(byte b) {
        byte[] array = new byte[6];
        for (int i = 0; i < 8; i++) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 初始化设备所需字节数据
     */
    private void initData() {
        for (int i = 0; i < 15; i++) {

            sentbuff[i] = 0x00;
        }
        for (int i = 0; i < 13; i++) {

            runstflag[i] = 0;

        }
        sentbuff[0] = 0x24;
        sentbuff[1] = 0x10;
        sentbuff[2] = 0x00;
        sentbuff[3] = 0x01;
        sentbuff[8] = 0x01;   //停止
        sentbuff[15] = 0x0D;
    }

    /**
     * 处理子线程消息
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private boolean isSuccess = true;

    /**
     * 参数设置模块
     * @param v 设置面板
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_mode:
                MaterialDialog.Builder modeDialog = new MaterialDialog.Builder(this);
                view = View.inflate(this, R.layout.mode_dialog_item, null);
                rdg = (RadioGroup) view.findViewById(R.id.radiogroup);
                modeDialog.customView(view, true);
                modeDialog.title("模式设置")
                        .iconRes(R.mipmap.outshow)
//                        .limitIconToDefaultSize()
                        .positiveText("确定")
                        .negativeText("取消")
                        .titleColor(Color.rgb(118, 215, 55))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                sentbuff[10] = 0x00;
                                if(SocketUtil.connectStaus==1){
                                    //自动
                                    if (R.id.radio1 == rdg.getCheckedRadioButtonId()) {
                                        sentbuff[8] = 0x02;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                SocketUtil.SendDataByte(sentbuff);
                                            }
                                        }).start();
                                    }
                                    //手动
                                    else if (R.id.radio2 == rdg.getCheckedRadioButtonId()) {
                                        sentbuff[8] = 0x01;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                SocketUtil.SendDataByte(sentbuff);
                                            }
                                        }).start();
                                    }
                                }

//                                else if(R.id.radio1!=rdg.getCheckedRadioButtonId() && R.id.radio2!=rdg.getCheckedRadioButtonId()){
//                                    Toast.makeText(ICActivity.this,"未选择模式",Toast.LENGTH_SHORT).show();
//                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
                break;

            case R.id.btn_param:
                // View view=View.inflate(this,R.layout.mode_dialog_item,null);
                AlertDialog.Builder paramDialog = new AlertDialog.Builder(this);
                view1 = LayoutInflater.from(this).inflate(R.layout.param_dialog_item, null);
                waterTemp = (EditText) view1.findViewById(R.id.et_water);
                windTemp = (EditText) view1.findViewById(R.id.et_wind);
                //控制编辑框的光标位置
                waterTemp.setSelection(waterTemp.length());
                windTemp.setSelection(windTemp.length());
                paramDialog.setView(view1);

                paramDialog.setTitle("参数界面设置")
                        .setIcon(R.mipmap.outshow)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String waterValue = waterTemp.getText().toString();
                                String windValue = windTemp.getText().toString();
                                if (waterValue.equals("") || windValue.equals("")) {
                                    Toast.makeText(ICActivity.this, "请输入参数正确的参数", Toast.LENGTH_SHORT).show();
                                    waterTemp.setText("25.0");
                                    windTemp.setText("35.0");
                                } else {
                                    Toast.makeText(ICActivity.this, "waterValue:" + waterValue + "  windValue:" + windValue, Toast.LENGTH_SHORT).show();
//                                    socketUtil.SendDataByte();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;

            case R.id.btn_clear:
                SweetAlertDialog clearDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                clearDialog.setTitleText("确定清理设备？")
                        .setContentText("数据将不能恢复！")
                        .setConfirmText("是的，清理")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                //异步加载之发送数据
                                if(SocketUtil.connectStaus==1){
                                    sentbuff[10] = 0x01;
                                    new AsyncTask() {

                                        @Override
                                        protected void onPreExecute() {
                                            sweetAlertDialog.setTitleText("正在清理！")
                                                    .setContentText("请稍等!")
                                                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                            super.onPreExecute();
                                        }


                                        @Override
                                        protected void onPostExecute(Object o) {
                                            if (!isSuccess) {
                                                sweetAlertDialog.setTitleText("未清理！")
                                                        .setContentText("您的设备出现异常!")
                                                        .setConfirmText("确定")
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                            } else {
                                                sweetAlertDialog.setTitleText("清理！")
                                                        .setContentText("您的设备已清理!")
                                                        .setConfirmText("确定")
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            }
                                            super.onPostExecute(o);
                                        }

                                        @Override
                                        protected Object doInBackground(Object[] params) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SocketUtil.SendDataByte(sentbuff);
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//                                            }
//                                        }).start();

                                            return null;
                                        }
                                    }.execute();
                                }


                            }
                        })
                        .setCancelClickListener(null)
                        .show();
                break;

            case R.id.btn_state:
                NiftyDialogBuilder stateDialog = NiftyDialogBuilder.getInstance(ICActivity.this);
                view3 = LayoutInflater.from(this).inflate(R.layout.state_dialog_item, null);
                //根据数据显示view3的界面
                imPres = (ImageView) view3.findViewById(R.id.img_press);
                imWash = (ImageView) view3.findViewById(R.id.img_wash);
                imPush = (ImageView) view3.findViewById(R.id.img_push);
                imAirpump = (ImageView) view3.findViewById(R.id.img_airpump);
                imWaterpump = (ImageView) view3.findViewById(R.id.img_waterpump);
                imDust = (ImageView) view3.findViewById(R.id.img_dust);
                imHeater = (ImageView) view3.findViewById(R.id.img_heater);
                imWarmheat = (ImageView) view3.findViewById(R.id.img_warmheat);

                if (runstflag[5] == 1) {

                    imPres.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imPres.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }

                if (runstflag[6] == 1) {

                    imWash.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imWash.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[7] == 1) {

                    imPush.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imPush.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[8] == 1) {

                    imAirpump.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imAirpump.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[9] == 1) {

                    imWaterpump.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imWaterpump.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[10] == 1) {

                    imDust.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imDust.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[11] == 1) {

                    imHeater.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imHeater.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }
                if (runstflag[12] == 1) {

                    imWarmheat.setImageDrawable(getResources().getDrawable(R.drawable.on));
                } else {
                    imWarmheat.setImageDrawable(getResources().getDrawable(R.drawable.off));
                }

                stateDialog.setCustomView(view3, v.getContext());
                stateDialog.withTitle("设备运行状态")
                        .withTitleColor("#000000")
                        .withDividerColor("#11000000")
                        .withDialogColor(Color.WHITE)
                        .withIcon(getResources().getDrawable(R.mipmap.outshow))
                        .withDuration(700)
                        .withEffect(Effectstype.RotateBottom)
//                        .withButton1Text("确定")
//                        .withButton2Text("退出")
                        .isCancelableOnTouchOutside(true)
//                        .setButton1Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                finish();
//                            }
//                        })
//                        .setButton2Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        })
                        .show();
                break;

            case R.id.btn_stop:
                sentbuff[10] = 0x00;
                sentbuff[8] = 0x04;
                if(SocketUtil.connectStaus==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketUtil.SendDataByte(sentbuff);
                        }
                    }).start();
                }
                break;

            case R.id.btn_record:
                //数据库存取操作

                break;

            case R.id.im_reply:
                sentbuff[10] = 0x00;
                if (snum == 0) {
                    sentbuff[8] = 0x08;
                    if(SocketUtil.connectStaus==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SocketUtil.SendDataByte(sentbuff);
                            }
                        }).start();
                    }

                }

                if (snum == 1) {
                    sentbuff[8] = 0x10;
                    if(SocketUtil.connectStaus==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SocketUtil.SendDataByte(sentbuff);
                            }
                        }).start();
                    }

                }

                if (snum == 2) {
                    sentbuff[8] = 0x40;
                    if(SocketUtil.connectStaus==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SocketUtil.SendDataByte(sentbuff);
                            }
                        }).start();
                    }

                }

        }
    }
}
