package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private TextView titleTextView;

    private ArrayList<ListViewItem> listViewItemsList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){
    }

    @Override
    public int getCount() {
        return listViewItemsList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        titleTextView = (TextView) view.findViewById(R.id.title);

        ListViewItem listViewItem = listViewItemsList.get(position);

        titleTextView.setText(listViewItem.getTitle());

        return view;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String title){
        ListViewItem item = new ListViewItem();
        item.setTitle(title);
        listViewItemsList.add(item);
    }
}
