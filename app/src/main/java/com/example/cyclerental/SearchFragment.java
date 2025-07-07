package com.example.cyclerental;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.List;
import java.util.stream.Collectors;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private CycleAdapter adapter;
    private List<Cycle> cycleList;
    private DBHelper dbHelper;
    private SearchView searchView;
    private Spinner spinnerType, spinnerPriceFilter;



    public SearchFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        dbHelper = new DBHelper(getContext());
        cycleList = dbHelper.getAllCycles();

        recyclerView = view.findViewById(R.id.rvCycles);
        searchView = view.findViewById(R.id.searchView);
        spinnerType = view.findViewById(R.id.spType);
        spinnerPriceFilter = view.findViewById(R.id.spPrice);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CycleAdapter(getContext(),cycleList);
        recyclerView.setAdapter(adapter);

        setupSearch();
        setupFilters();

        return view;
    }

    private void setupSearch() {
        searchView.setQueryHint("Search your location");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCycles();
                return true;
            }
        });
    }

    private void setupFilters() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCycles();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerType.setOnItemSelectedListener(listener);
        spinnerPriceFilter.setOnItemSelectedListener(listener);
    }

    private void filterCycles() {
        String searchText = searchView.getQuery().toString().toLowerCase();
        String selectedType = spinnerType.getSelectedItem().toString();
        String selectedSort = spinnerPriceFilter.getSelectedItem().toString();

        List<Cycle> filteredList = cycleList.stream()
                .filter(b -> TextUtils.isEmpty(searchText) || b.getLocation().toLowerCase().contains(searchText))
                .filter(b -> selectedType.equals("All") || b.getType().equals(selectedType))
                .collect(Collectors.toList());

        if (selectedSort.equals("Low - High")) {
            filteredList.sort((b1, b2) -> Double.compare(b1.getCprice(), b2.getCprice()));
        } else if (selectedSort.equals("High - Low")) {
            filteredList.sort((b1, b2) -> Double.compare(b2.getCprice(), b1.getCprice()));
        }

        adapter.updateList(filteredList);
    }

}