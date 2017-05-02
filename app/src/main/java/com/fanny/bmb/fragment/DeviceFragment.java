package com.fanny.bmb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanny.bmb.R;
import com.fanny.bmb.activity.BedActivity;
import com.fanny.bmb.activity.CameraActivity;
import com.fanny.bmb.activity.ICActivity;
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

public class DeviceFragment extends Fragment {
    @BindView(R.id.bmb)
    BoomMenuButton bmb;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_device, null);
        unbinder = ButterKnife.bind(this, view);
        bmb= (BoomMenuButton) view.findViewById(R.id.bmb);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        /**
         * boom
         */
        int[] images = new int[]{R.mipmap.bed1, R.mipmap.ic1, R.drawable.app, R.mipmap.bath1, R.drawable.pig, R.drawable.sheep};

        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);



        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder().normalImageRes(images[i]));
        }



        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index){
                    case 0:
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), BedActivity.class);
                                startActivity(intent);
                            }
                        }, 400);
                        break;
                    case 1:
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), ICActivity.class);
                                startActivity(intent);
                            }
                        }, 400);
                        break;
                    case 2:
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), CameraActivity.class);
                                startActivity(intent);
                            }
                        }, 400);
                        break;
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
