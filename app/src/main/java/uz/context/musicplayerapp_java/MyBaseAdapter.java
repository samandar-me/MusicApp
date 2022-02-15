package uz.context.musicplayerapp_java;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyBaseAdapter extends BaseAdapter {

    Activity activity;
    String[] items;
    Context context;
    public MyBaseAdapter(Activity activity, Context context, String[] items){
        this.activity = activity;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
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
        View myView = activity.getLayoutInflater().inflate(R.layout.list_item, null);
        TextView textSong = myView.findViewById(R.id.txt_song_name);
        textSong.setSelected(true);
        textSong.setText(items[i]);

        return myView;
    }
}
