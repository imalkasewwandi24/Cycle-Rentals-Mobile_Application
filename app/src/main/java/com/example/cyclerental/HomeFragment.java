package com.example.cyclerental;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public HomeFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        List<Plan> topPicks = new ArrayList<>();
        topPicks.add(new Plan(R.drawable.price1,"Pay-As-You-Go", "2$", "Per hour"));
        topPicks.add(new Plan(R.drawable.price2,"Daily Ride", "10% off", "For 24 hours"));
        topPicks.add(new Plan(R.drawable.price3,"Weekly Ride", "15% off", "For 7 days"));
        topPicks.add(new Plan(R.drawable.price4,"Monthly Ride", "30% off", "For 1 month"));
        topPicks.add(new Plan(R.drawable.price5,"Uni-Go", "50% off", "For 1 month"));


        List<Plan> promoList = new ArrayList<>();
        promoList.add(new Plan(R.drawable.promo1,"Ride On Us!","Free", "For 30 minutes"));
        promoList.add(new Plan(R.drawable.promo2,"Rifer & Ride!","Earn 5$", "Anytime"));
        promoList.add(new Plan(R.drawable.promo3,"Festive Deals!","Upto 20% Off", "Only For April"));

        setupRecycleView(view,R.id.rvTopPicks,topPicks);
        setupRecycleView(view,R.id.rvPromotions,promoList);

        return view;

    }

    private void setupRecycleView(View view ,int rcId,List<Plan> planList){
        RecyclerView recyclerView = view.findViewById(rcId);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        PlanAdapter adapter = new PlanAdapter(planList);
        recyclerView.setAdapter(adapter);

    }


}