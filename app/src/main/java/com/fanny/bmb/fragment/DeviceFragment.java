package com.fanny.bmb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fanny.bmb.R;
import com.fanny.bmb.activity.BedActivity;
import com.fanny.bmb.activity.CameraActivity;
import com.fanny.bmb.activity.CameraCallActivity;
import com.fanny.bmb.activity.ICActivity;
import com.fanny.bmb.adapter.HorizontalPagerAdapter;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Fanny on 17/4/11.
 */

public class DeviceFragment extends Fragment implements View.OnClickListener {
    private ImageButton btn_bed;
    private ImageButton btn_ic;
    private ImageButton btn_camera;
    private ImageButton btn_hair;
    //    @BindView(R.id.bmb)
//    BoomMenuButton bmb;
//    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_device, null);

        /**
         * 产品介绍界面viewpager  在HorizontalPagerAdapter中修改宣传图片
         */
        HorizontalInfiniteCycleViewPager hicvp = (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.device_vp);
        hicvp.setAdapter(new HorizontalPagerAdapter(getActivity()));
        hicvp.setScrollDuration(3000);
        hicvp.startAutoScroll(true);
        /**
         * boombtn效果
         */
//        bmb= (BoomMenuButton) view.findViewById(R.id.bmb);

        /**
         * 控件find
         */
        btn_bed = (ImageButton) view.findViewById(R.id.btn_bed);
        btn_ic = (ImageButton) view.findViewById(R.id.btn_ic);
        btn_camera = (ImageButton) view.findViewById(R.id.btn_camera);
        btn_hair = (ImageButton) view.findViewById(R.id.btn_hair);

        btn_bed.setOnClickListener(this);
        btn_ic.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_hair.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bed:
                Intent intent = new Intent();
                intent.setClass(getActivity(), BedActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_ic:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), ICActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_camera:
                Intent intent2 = new Intent();
//                intent2.setClass(getActivity(), CameraActivity.class);
                intent2.setClass(getActivity(), CameraCallActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_hair:

                break;

        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        /**
//         * boom
//         */
//        int[] images = new int[]{R.mipmap.bed1, R.mipmap.ic1, R.drawable.app, R.mipmap.bath1, R.drawable.pig, R.drawable.sheep};
//
//        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
//        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
//        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);
//
//
//
//        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
//            bmb.addBuilder(new SimpleCircleButton.Builder().normalImageRes(images[i]));
//        }
//
//
//
//        bmb.setOnBoomListener(new OnBoomListener() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                switch (index){
//                    case 0:
//                        new Handler().postDelayed(new Runnable(){
//                            public void run() {
//                                Intent intent=new Intent();
//                                intent.setClass(getActivity(), BedActivity.class);
//                                startActivity(intent);
//                            }
//                        }, 400);
//                        break;
//                    case 1:
//                        new Handler().postDelayed(new Runnable(){
//                            public void run() {
//                                Intent intent=new Intent();
//                                intent.setClass(getActivity(), ICActivity.class);
//                                startActivity(intent);
//                            }
//                        }, 400);
//                        break;
//                    case 2:
//                        new Handler().postDelayed(new Runnable(){
//                            public void run() {
//                                Intent intent=new Intent();
//                                intent.setClass(getActivity(), CameraActivity.class);
//                                startActivity(intent);
//                            }
//                        }, 400);
//                        break;
//                }
//            }
//
//            @Override
//            public void onBackgroundClick() {
//
//            }
//
//            @Override
//            public void onBoomWillHide() {
//
//            }
//
//            @Override
//            public void onBoomDidHide() {
//
//            }
//
//            @Override
//            public void onBoomWillShow() {
//
//            }
//
//            @Override
//            public void onBoomDidShow() {
//
//            }
//        });
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
