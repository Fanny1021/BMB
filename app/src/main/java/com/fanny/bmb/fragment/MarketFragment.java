package com.fanny.bmb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fanny.bmb.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by Fanny on 17/4/11.
 */

public class MarketFragment extends Fragment{

    private BoomMenuButton bmb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.fragment_cart, null);
        bmb = (BoomMenuButton) view.findViewById(R.id.bmb1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        int[] images = new int[]{R.mipmap.bed, R.mipmap.ic, R.mipmap.hair, R.mipmap.bath};
        int[] strs=new int[]{R.string.bed,R.string.ic,R.string.hair,R.string.bath};

        for(int i=0;i<bmb.getPiecePlaceEnum().pieceNumber();i++){
            HamButton.Builder builder=new HamButton.Builder()
                    .normalImageRes(images[i])
                    .normalTextRes(strs[i])
                    .subNormalTextRes(R.string.open);
            bmb.addBuilder(builder);


        }
        super.onActivityCreated(savedInstanceState);
    }
}
