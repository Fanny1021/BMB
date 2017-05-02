package com.fanny.bmb.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanny.bmb.R;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fanny on 17/4/13.
 */

public class HorizontalPagerAdapter extends PagerAdapter {

    private final int[] libs=new int[]{R.drawable.bednew,R.drawable.bednew,R.drawable.bednew};


    private Context mcontext;
    private LayoutInflater mLayoutInflater;


    public HorizontalPagerAdapter(Context context) {
        mcontext=context;
        mLayoutInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return libs.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view;
        view=mLayoutInflater.inflate(R.layout.vp_item,container,false);
        setupItem(view, libs[position]);
        container.addView(view);
        return view;
    }

    private void setupItem(View view, int image) {
//        TextView txt= (TextView) view.findViewById(R.id.txt_item);
//        txt.setText("宣传图片");
        ImageView img= (ImageView) view.findViewById(R.id.img_item);
        img.setImageResource(image);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
