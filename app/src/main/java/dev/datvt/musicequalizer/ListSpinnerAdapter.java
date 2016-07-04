package dev.datvt.musicequalizer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by datvt on 7/3/2016.
 */
public class ListSpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> data = null;
    private Activity context;
    private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();


    public ListSpinnerAdapter(Activity context, int resource, ArrayList<String> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        hashMap.put("Normal", R.drawable.musics);
        hashMap.put("Classical", R.drawable.classical);
        hashMap.put("Pop", R.drawable.pop);
        hashMap.put("Hip Hop", R.drawable.hiphop);
        hashMap.put("Jazz", R.drawable.jazz);
        hashMap.put("Rock", R.drawable.rock);
        hashMap.put("Rap", R.drawable.rap);
        hashMap.put("Reggae", R.drawable.reggae);
        hashMap.put("R&B", R.drawable.rb);
        hashMap.put("Country", R.drawable.country);
        hashMap.put("Electronic", R.drawable.electronic);
        hashMap.put("Blues", R.drawable.blues);
        hashMap.put("Soundtrack", R.drawable.soundtrack);
        hashMap.put("Various", R.drawable.various);
        hashMap.put("Punk", R.drawable.punk);
        hashMap.put("Heavy Metal", R.drawable.metal);
        hashMap.put("Folk", R.drawable.folk);
        hashMap.put("Gospel", R.drawable.gospel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = data.get(position);
        if (hashMap.get(name) != null) {
            holder.icon.setImageResource(hashMap.get(name));
        } else {
            holder.icon.setImageResource(R.drawable.ic_album);
        }
        holder.name.setText(name);

        return convertView;
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) v.findViewById(R.id.tvNameTypeSong);
        holder.icon = (ImageView) v.findViewById(R.id.ivIcon);
        return holder;
    }

    private static class ViewHolder {
        public TextView name;
        public ImageView icon;
    }
}
