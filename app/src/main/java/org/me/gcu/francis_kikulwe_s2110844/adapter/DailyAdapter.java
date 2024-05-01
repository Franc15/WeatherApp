package org.me.gcu.francis_kikulwe_s2110844.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.francis_kikulwe_s2110844.R;
import org.me.gcu.francis_kikulwe_s2110844.model.Daily;

import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {
    ArrayList<Daily> items;
    Context context;

    public DailyAdapter(ArrayList<Daily> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DailyAdapter.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_daily, parent, false);
        context = parent.getContext();
        return new DailyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyAdapter.DailyViewHolder holder, int position) {
        holder.day.setText(items.get(position).getTitle());
        holder.Temp.setText(items.get(position).getDescription());
        holder.pic.setImageResource(R.drawable.rain);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView day, Temp;
        ImageView pic;
        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dayTxt);
            Temp = itemView.findViewById(R.id.tempTxt);
            pic = itemView.findViewById(R.id.picView);
        }
    }
}
