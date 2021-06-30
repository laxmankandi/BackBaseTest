package com.backbasetest.main.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.backbasetest.main.data.local.City;

import java.util.ArrayList;
import java.util.List;


public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> searchQuery;
    private MutableLiveData<List<City>> cityListLiveData;
    public MainViewModel(){
         cityListLiveData = new MutableLiveData<>();
         searchQuery = new MutableLiveData<>();
    }

    public MutableLiveData<String> getSearchQuery() {

        return searchQuery;
    }

    public MutableLiveData<List<City>> getCityListLiveData() {

        return cityListLiveData;
    }
    public LiveData<List<City>> getFilteredList(){
        return Transformations.switchMap(getSearchQuery(),query->filterCity(query));
    }
    private MutableLiveData<List<City>> filterCity(String query){
        if(query.isEmpty()){
            return cityListLiveData;
        }
        List<City> filteredCities = new ArrayList<>();
        for(City city : cityListLiveData.getValue()){
            if(city.getName().startsWith(query) || city.getCountry().startsWith(query) ){
                 filteredCities.add(city);
            }
        }

        return new MutableLiveData<>(filteredCities);

    }



}