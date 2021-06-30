package com.backbasetest.main.ui.main;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.FileUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.backbasetest.main.MainActivity;
import com.backbasetest.main.R;
import com.backbasetest.main.data.local.City;
import com.backbasetest.main.databinding.MainFragmentBinding;
import com.backbasetest.main.ui.adapers.CityRecycleAdapter;
import com.backbasetest.main.ui.adapers.RecycleItemClickListener;
import com.backbasetest.main.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment implements SearchView.OnQueryTextListener, RecycleItemClickListener, OnMapReadyCallback {

    public static MainFragment newInstance() {
        return new MainFragment();
    }
    MainFragmentBinding mainFragmentBinding;
    private MainViewModel mViewModel;
    private CityRecycleAdapter cityRecycleAdapter;
    private SearchView searchView;
    private City city;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mainFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.main_fragment, container, false);
        return mainFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.cities));
          cityRecycleAdapter = new CityRecycleAdapter();
          cityRecycleAdapter.setRecycleItemClickListener(this::onItemClick);
        try {
            InputStream inputStream = getActivity().getAssets().open("cities.json");
            String cityJson  = Utils.convertInputStreamToString(inputStream);
            Type listType = new TypeToken<ArrayList<City>>(){}.getType();
            List<City> cityList = new Gson().fromJson(cityJson, listType);

            Collections.sort(cityList, new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    int comp = o1.getName().compareTo(o2.getName());
                    if(comp != 0){
                        return comp;
                    }
                    return o1.getCountry().compareTo(o2.getCountry());
                }
            });


                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                mainFragmentBinding.recycleCities.setLayoutManager(linearLayoutManager);
                mainFragmentBinding.recycleCities.setAdapter(cityRecycleAdapter);
                mViewModel.getCityListLiveData().setValue(cityList);



        } catch (IOException e) {
            e.printStackTrace();
        }

        mViewModel.getFilteredList().observe(getViewLifecycleOwner(), new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                if(cities != null){
                    cityRecycleAdapter.setCityList(cities);
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_search,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_serach);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        searchView.setOnQueryTextListener(this);
        if(mViewModel.getSearchQuery().getValue() == null) {
            searchView.setQuery("",false);
        }else {
            searchView.setQuery(mViewModel.getSearchQuery().getValue(),true);

        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mViewModel.getSearchQuery().setValue(newText);
        return true;
    }

    @Override
    public void onItemClick(View view, int position, Object object) {
        city = (City)object;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(city.getCoord().getLat(), city.getCoord().getLon())).zoom(5).build();
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.zOrderOnTop(true);
        googleMapOptions.zoomControlsEnabled(true);
        googleMapOptions.zoomGesturesEnabled(true);
        googleMapOptions.ambientEnabled(true);
        googleMapOptions.camera(cameraPosition);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        supportMapFragment.getMapAsync(this::onMapReady);
        getParentFragmentManager().beginTransaction().replace(R.id.container, supportMapFragment).addToBackStack(null).commit();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(new LatLng(city.getCoord().getLat(), city.getCoord().getLon())).title(city.getName()+", "+city.getCountry()));
    }
}