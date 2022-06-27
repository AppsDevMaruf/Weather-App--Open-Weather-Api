package com.marufalam.weatherapiapps.networks;

import com.marufalam.weatherapiapps.models.currents.CurrentResponseModel;
import com.marufalam.weatherapiapps.models.forecast.ForecastResponseModel;
import com.marufalam.weatherapiapps.models.fourDayHourly.FourDayHResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {
    @GET()
    Call<CurrentResponseModel> getCurrentData(@Url String endUrl);
    @GET()
    Call<ForecastResponseModel> getForecastData(@Url String endUrl);
    @GET()
    Call<FourDayHResponseModel> getFourDayHData(@Url String endUrl);
}
