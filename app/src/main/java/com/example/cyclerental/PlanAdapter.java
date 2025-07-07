package com.example.cyclerental;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;

    public PlanAdapter(List<Plan> planList){
        this.planList = planList;
    }



    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent , parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        holder.ivRent.setImageResource(plan.getImageId());
        holder.tvTitle.setText(plan.getName());
        holder.tvPrice.setText(plan.getPrice());
        holder.tvTime.setText(plan.getTime());



    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRent;
        TextView tvTitle,tvPrice, tvTime;


        public PlanViewHolder(View itemView){
            super(itemView);

            ivRent = itemView.findViewById(R.id.ivRent);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);

        }

    }
}