package com.pop.pareeya.ahelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by masterUNG on 10/29/2016 AD.
 */

public class PhoneAdapter extends BaseAdapter{

    //Explicit
    private Context context;
    private String[] nameStrings, phoneStrings;

    public PhoneAdapter(Context context,
                        String[] nameStrings,
                        String[] phoneStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.phoneStrings = phoneStrings;
    }

    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.phone_listview, viewGroup, false);

        TextView textView = (TextView) view1.findViewById(R.id.textView12);
        TextView textView1 = (TextView) view1.findViewById(R.id.textView13);

        textView.setText(nameStrings[i]);
//        textView1.setText(phoneStrings[i]);
        textView1.setText("");

        return view1;
    }
}   // Main Class
