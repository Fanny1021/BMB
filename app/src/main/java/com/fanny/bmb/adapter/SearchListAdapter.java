package com.fanny.bmb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.fanny.bmb.R;
import com.fanny.bmb.util.ContentCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fanny on 17/4/19.
 */

public class SearchListAdapter extends BaseAdapter {

    private Context ctx = null;
    private LayoutInflater listContainer = null;

    private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

    public Map<String, Object> getItemContent(int which) {
        if (which > listItems.size()) {
            return null;
        }
        return listItems.get(which);
    }

    public boolean AddCamera(String strMac, String strName, String strDeviceID) {
        if (!CheckCameraInfo(strMac)) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(ContentCommon.STR_CAMERA_MAC, strMac);
        map.put(ContentCommon.STR_CAMERA_NAME, strName);
        map.put(ContentCommon.STR_CAMERA_ID, strDeviceID);
        listItems.add(map);
        return true;
    }

    private boolean CheckCameraInfo(String strMac) {
        int size=listItems.size();
        for(int i=0;i<size;i++){
            String mac= (String) listItems.get(i).get(ContentCommon.STR_CAMERA_MAC);
            if(strMac.equals(mac)){
                return false;
            }
        }
        return true;
    }

    public final class SearchListItem {
        public TextView devName;
        public TextView devID;
    }

    public SearchListAdapter(Context context) {
        ctx = context;
        listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchListItem searchListItem = null;
        if (convertView == null) {
            searchListItem = new SearchListItem();
            convertView = listContainer.inflate(R.layout.search_list_item, null);
            searchListItem.devName = (TextView) convertView.findViewById(R.id.searchDevName);
            searchListItem.devID = (TextView) convertView.findViewById(R.id.searchDevID);
            convertView.setTag(searchListItem);
        } else {
            searchListItem = (SearchListItem) convertView.getTag();
        }

        if(listItems!=null){
            searchListItem.devName.setText((String)listItems.get(position).get(ContentCommon.STR_CAMERA_NAME));
            searchListItem.devID.setText((String)listItems.get(position).get(ContentCommon.STR_CAMERA_ID));
        }
        return convertView;
    }

}
