package dev.datvt.musicequalizer.list_song;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.datvt.musicequalizer.R;

/**
 * Created by datvt on 6/30/2016.
 */
public class ListMusicAdapter extends BaseAdapter {

    private Activity context;
    private List<Music> musicList;

    public ListMusicAdapter(Activity context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        if (musicList != null) {
            return musicList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (musicList != null) {
            return musicList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_music, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Music music = (Music) getItem(position);
        holder.name.setText(music.getName());
        holder.author.setText(music.getAuthor());
        holder.time.setText(music.getTime());
        holder.image.setImageBitmap(music.getAlbumArt());

        return convertView;
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) v.findViewById(R.id.tvName);
        holder.author = (TextView) v.findViewById(R.id.tvAuthor);
        holder.time = (TextView) v.findViewById(R.id.tvTime);
        holder.image = (ImageView) v.findViewById(R.id.iv_abum);
        return holder;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView author;
        public TextView time;
        public ImageView image;
    }
}
