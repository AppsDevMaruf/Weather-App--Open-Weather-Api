package com.marufalam.weatherapiapps.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marufalam.weatherapiapps.R;
import com.marufalam.weatherapiapps.models.forecast.ForecastResponseModel;
import com.marufalam.weatherapiapps.models.forecast.ListItem;
import com.marufalam.weatherapiapps.utils.Constants;
import com.marufalam.weatherapiapps.utils.WeatherHelperFunctions;
import com.squareup.picasso.Picasso;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private ForecastResponseModel forecastModel;

    public ForecastAdapter(ForecastResponseModel forecastModel) {
        this.forecastModel = forecastModel;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_temp, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        final String iconUrl = Constants.ICON_PREFIX + forecastModel.getList().get(position).getWeather().get(0).getIcon() + Constants.ICON_SUFFIX;
        Picasso.get().load(iconUrl).into(holder.icon);
        holder.day.setText(WeatherHelperFunctions.getFormattedDateTime(forecastModel.getList().get(position).getDt(), "EEE"));
        holder.max.setText(String.format("%.0f\u00B0", forecastModel.getList().get(position).getMain().getTempMax()));
        holder.min.setText(String.format("%.0f\u00B0", forecastModel.getList().get(position).getMain().getTempMin()));

    }

    @Override
    public int getItemCount() {
        return forecastModel.getList().size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView day, max, min;
        ImageView icon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            max = itemView.findViewById(R.id.max);
            min = itemView.findViewById(R.id.min);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
