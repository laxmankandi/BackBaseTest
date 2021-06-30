package com.backbasetest.main.ui.adapers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.backbasetest.main.R;
import com.backbasetest.main.data.local.City;
import com.backbasetest.main.databinding.CityItemLayoutBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CityRecycleAdapter extends RecyclerView.Adapter<CityRecycleAdapter.CityViewHolder>{

    private List<City> cityList;
    private RecycleItemClickListener recycleItemClickListener;
    public CityRecycleAdapter(){
        cityList = new ArrayList<>();
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    public void setRecycleItemClickListener(RecycleItemClickListener recycleItemClickListener) {
        this.recycleItemClickListener = recycleItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_layout,parent,false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CityViewHolder holder, int position) {
              holder.bindData(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CityItemLayoutBinding cityItemLayoutBinding;

        public CityViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cityItemLayoutBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this::onClick);
        }
        public void bindData(City city){

            cityItemLayoutBinding.txtCity.setText(city.getName()+", "+city.getCountry());
            cityItemLayoutBinding.txtLatLang.setText("Location: "+city.getCoord().getLat()+" , "+city.getCoord().getLon());

        }

        @Override
        public void onClick(View v) {
            if(recycleItemClickListener != null){
                recycleItemClickListener.onItemClick(v,getAdapterPosition(),cityList.get(getAdapterPosition()));
            }
        }
    }
}
