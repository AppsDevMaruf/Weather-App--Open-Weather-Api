package com.marufalam.weatherapiapps.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.marufalam.weatherapiapps.R;
import com.marufalam.weatherapiapps.models.fourDayHourly.FourDayHResponseModel;
import com.marufalam.weatherapiapps.utils.Constants;
import com.marufalam.weatherapiapps.utils.WeatherHelperFunctions;
import com.squareup.picasso.Picasso;

public class ForeDayHAdapter extends RecyclerView.Adapter<ForeDayHAdapter.ForeDayHViewHolder> {
    private FourDayHResponseModel fourDayHResponseModel;

    public ForeDayHAdapter(FourDayHResponseModel fourDayHResponseModel) {
        this.fourDayHResponseModel = fourDayHResponseModel;
    }

    @NonNull
    @Override
    public ForeDayHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fourday_simple_row,parent,false);
        return new ForeDayHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForeDayHViewHolder holder, int position) {

        final String iconUrl = Constants.ICON_PREFIX + fourDayHResponseModel.getList().get(position).getWeather().get(0).getIcon() + Constants.ICON_SUFFIX;
        Picasso.get().load(iconUrl).into(holder.weather_image_view);
        holder.day_name_text_view.setText(WeatherHelperFunctions.getFormattedDateTime(fourDayHResponseModel.getList().get(position).getDt(), "EEE, dd/MM/yyyy"));
        holder.temp_text_view.setText(String.format("%.0f\u00B0", fourDayHResponseModel.getList().get(position).getMain().getTemp()));
        holder.max_temp_text_view.setText(String.format("%.0f\u00B0", fourDayHResponseModel.getList().get(position).getMain().getTempMax()));
        holder.min_temp_text_view.setText(String.format("%.0f\u00B0", fourDayHResponseModel.getList().get(position).getMain().getTempMin()));
    }

    @Override
    public int getItemCount() {
        return fourDayHResponseModel.getList().size();
    }

    public class ForeDayHViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView day_name_text_view,temp_text_view,min_temp_text_view,max_temp_text_view;
        AppCompatImageView weather_image_view;

        public ForeDayHViewHolder(@NonNull View itemView) {
            super(itemView);
            day_name_text_view = itemView.findViewById(R.id.day_name_text_view);
            temp_text_view = itemView.findViewById(R.id.temp_text_view);
            min_temp_text_view = itemView.findViewById(R.id.min_temp_text_view);
            max_temp_text_view = itemView.findViewById(R.id.max_temp_text_view);
            weather_image_view = itemView.findViewById(R.id.weather_image_view);


        }
    }
}
