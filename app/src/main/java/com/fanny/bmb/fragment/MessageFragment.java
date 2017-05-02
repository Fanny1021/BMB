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
import com.fanny.bmb.activity.SocialMsgActivity;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Fanny on 17/4/11.
 */

public class MessageFragment extends Fragment {

    @BindView(R.id.favorite)
    CircleMenuButton favorite;
    @BindView(R.id.search)
    CircleMenuButton search;
    @BindView(R.id.alert)
    CircleMenuButton alert;
    @BindView(R.id.place)
    CircleMenuButton place;
    @BindView(R.id.edit)
    CircleMenuButton edit;
    @BindView(R.id.circlemenu)
    CircleMenu circlemenu;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_message, null);
        unbinder = ButterKnife.bind(this, view);
        circlemenu= (CircleMenu) view.findViewById(R.id.circlemenu);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        /**
         * circlemenu
         */

        circlemenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
            @Override
            public void onItemClick(CircleMenuButton menuButton) {
                switch (menuButton.getId()){
                    case R.id.favorite:
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                Intent intent=new Intent();
                                intent.setClass(getActivity(), SocialMsgActivity.class);
                                startActivity(intent);
                            }
                        }, 900);
                        break;

                }



            }
        });
        circlemenu.setStateUpdateListener(new CircleMenu.OnStateUpdateListener() {
            @Override
            public void onMenuExpanded() {

            }

            @Override
            public void onMenuCollapsed() {

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
