package uz.context.musicplayerapp_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    Context context;
    String[] list;

    ItemClickListener listener;

    public CustomAdapter(Context context, String[] list, ItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
       holder.textName.setText(list[position]);
        holder.relativeLayout.setOnClickListener(view -> {
            listener.itemClick(list[position], position);
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textName;
        RelativeLayout relativeLayout;

        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            textName = view.findViewById(R.id.txt_song_name);
            relativeLayout = view.findViewById(R.id.relative_layout);
        }
    }
}
