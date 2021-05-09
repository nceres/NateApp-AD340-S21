package com.natesoft.nateapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    private Context context;
    private List<Camera> list;

    public CameraAdapter(Context context, List<Camera> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.camLocation);
            image = itemView.findViewById(R.id.camImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.camera_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Camera camera = list.get(position);
        holder.description.setText(camera.getDescription());
        Picasso.get().load(camera.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() { return list.size(); }

}
