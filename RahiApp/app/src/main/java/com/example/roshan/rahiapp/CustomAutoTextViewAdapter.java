package com.example.roshan.rahiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darthvader on 17/7/18.
 */

public class CustomAutoTextViewAdapter extends ArrayAdapter {
    private List<String> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;



    public CustomAutoTextViewAdapter(Context context, int resource, List<String> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position));
        return dataList.get(position);
    }



    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                dataListAllItems = new ArrayList<String>(dataList);

            }

            if (prefix == null || prefix.length() == 0) {
                results.values = dataListAllItems;
                results.count = dataListAllItems.size();

            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().contains(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dataList = (ArrayList<String>)results.values;
            notifyDataSetChanged();

        }

    }
}
