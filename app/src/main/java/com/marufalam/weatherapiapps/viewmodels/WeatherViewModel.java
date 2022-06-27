package com.marufalam.weatherapiapps.viewmodels;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.marufalam.weatherapiapps.models.currents.CurrentResponseModel;
import com.marufalam.weatherapiapps.models.forecast.ForecastResponseModel;
import com.marufalam.weatherapiapps.models.fourDayHourly.FourDayHResponseModel;
import com.marufalam.weatherapiapps.networks.WeatherService;
import com.marufalam.weatherapiapps.utils.Constants;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final String TAG  = WeatherViewModel.class.getSimpleName();
    private Location location;
    private final MutableLiveData<CurrentResponseModel> currentLiveData = new MutableLiveData<>();
    private final MutableLiveData<ForecastResponseModel> forecastLiveData = new MutableLiveData<>();
    private final MutableLiveData<FourDayHResponseModel> fourDayLiveData = new MutableLiveData<>();

    public LiveData<FourDayHResponseModel> getFourDayLiveData() {
        return fourDayLiveData;
    }
    public LiveData<CurrentResponseModel> getCurrentLiveData() {
        return currentLiveData;
    }

    public LiveData<ForecastResponseModel> getForecastLiveData() {
        return forecastLiveData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void loadData(){
        loadCurrentData();
        loadForecastData();
        loadFourDayHData();
    }

    private void loadForecastData() {
        final String endUrl = String.format("forecast?lat=%f&lon=%f&units=metric&appid=%s",location.getLatitude(),location.getLongitude(),
                Constants.WEATHER_API_KEY);
        WeatherService.getService().getForecastData(endUrl)
                .enqueue(new Callback<ForecastResponseModel>() {
                    @Override
                    public void onResponse(Call<ForecastResponseModel> call, Response<ForecastResponseModel> response) {
                        if (response.code()==200){
                            forecastLiveData.postValue(response.body());


                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastResponseModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getLocalizedMessage());
                    }

                });
    }
    private void loadFourDayHData() {
        //forecast/hourly?lat=23.59699&lon=90.54&appid=d98aea393d44d7912075d86400b9c419
        final String endUrl = String.format("forecast/hourly?lat=%f&lon=%f&units=metric&appid=%s",location.getLatitude(),location.getLongitude(),
                Constants.WEATHER_API_KEY);
        WeatherService.getService().getFourDayHData(endUrl)
                .enqueue(new Callback<FourDayHResponseModel>() {
                    @Override
                    public void onResponse(Call<FourDayHResponseModel> call, Response<FourDayHResponseModel> response) {
                        if (response.code() ==200){
                            fourDayLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FourDayHResponseModel> call, Throwable t) {

                    }
                });

    }

    private void loadCurrentData() {
        final String endUrl = String.format("weather?lat=%f&lon=%f&units=metric&appid=%s",location.getLatitude(),location.getLongitude(),
                Constants.WEATHER_API_KEY);
        WeatherService.getService().getCurrentData(endUrl)
                .enqueue(new Callback<CurrentResponseModel>() {
                    @Override
                    public void onResponse(Call<CurrentResponseModel> call, Response<CurrentResponseModel> response) {
                        if (response.code() ==200){
                            currentLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentResponseModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getLocalizedMessage());
                    }
                });
    }
}
