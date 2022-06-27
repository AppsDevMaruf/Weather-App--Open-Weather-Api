package com.marufalam.weatherapiapps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.marufalam.weatherapiapps.adapter.ForeDayHAdapter;
import com.marufalam.weatherapiapps.adapter.ForecastAdapter;
import com.marufalam.weatherapiapps.databinding.FragmentWeatherBinding;
import com.marufalam.weatherapiapps.utils.Constants;
import com.marufalam.weatherapiapps.utils.LocationPermissionService;
import com.marufalam.weatherapiapps.utils.WeatherHelperFunctions;
import com.marufalam.weatherapiapps.viewmodels.WeatherViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private final String TAG = WeatherFragment.class.getSimpleName();
    private WeatherViewModel viewModel;
    private ForeDayHAdapter foreDayHAdapter;
    private FusedLocationProviderClient providerClient;
    private final ActivityResultLauncher<String> launcher =
            registerForActivityResult(new ActivityResultContracts
                            .RequestPermission(),
                    result -> {
                        if (result) {
                            detectUserLocation();
                            //detect location
                        } else {
                            //show dialog and explain why you need this permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(false);
                            builder.setTitle(getResources().getString(R.string.enable_permission));
                            builder.setMessage(getResources().getString(R.string.enable_access));
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(
                                            new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    });


    public WeatherFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        providerClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //initial viewModel before location permission
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        cheekLocationPermission();
        viewModel.getCurrentLiveData().observe(getViewLifecycleOwner(), currentResponseModel -> {
            if (currentResponseModel != null) {
                Log.e(TAG, "current: " + currentResponseModel.getMain().getTemp());
                binding.mainTempTV.setText(String.format("%.0f\u00B0", currentResponseModel.getMain().getTemp()));
               /* binding.description.setText(currentResponseModel.getWeather().get(0).getDescription());
                final String iconUrl = Constants.ICON_PREFIX+currentResponseModel.getWeather().get(0).getIcon()+Constants.ICON_SUFFIX;
                Picasso.get().load(iconUrl).into(binding.weatherIcon);*/
                binding.cityTV.setText(currentResponseModel.getName() + "," + currentResponseModel.getSys().getCountry());
                binding.todayTV.setText(WeatherHelperFunctions.getFormattedDateTime(currentResponseModel.getDt(), "dd MMMM yyyy"));
                binding.humidity.setText(currentResponseModel.getMain().getHumidity() + "%");
                binding.wind.setText(currentResponseModel.getWind().getSpeed() + "m/s");
                binding.airPressure.setText(currentResponseModel.getMain().getPressure() + "mm");
                binding.seaLevel.setText(currentResponseModel.getMain().getSeaLevel() + "m");

                final String iconUrl2 = Constants.ICON_PREFIX + currentResponseModel.getWeather().get(0).getIcon() + Constants.ICON_SUFFIX;
                Picasso.get().load(iconUrl2).into(binding.mainTempIV);



            } else {
                Toast.makeText(requireActivity(), "Error here:current", Toast.LENGTH_SHORT).show();
            }

        });
        viewModel.getForecastLiveData().observe(getViewLifecycleOwner(), forecastResponseModel -> {
            if (forecastResponseModel != null) {

                Log.e(TAG, "forecast: " + forecastResponseModel.getList().size());
                binding.bottonReciclerView.setHasFixedSize(true);
                binding.bottonReciclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.bottonReciclerView.setAdapter(new ForecastAdapter(forecastResponseModel));


            } else {
                Toast.makeText(requireActivity(), "Error here:forecast", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getFourDayLiveData().observe(getViewLifecycleOwner(), fourDayHResponseModel -> {
            Toast.makeText(getActivity(), "No data Found"+fourDayHResponseModel.getList().size(), Toast.LENGTH_LONG).show();
            if (fourDayHResponseModel != null) {
                Toast.makeText(getActivity(), "FourDay"+fourDayHResponseModel.getList().size(), Toast.LENGTH_LONG).show();
                //fourDayForecast
                Log.e(TAG, "onCreateView: "+fourDayHResponseModel.getList().size());
                /*RecyclerView recyclerView = requireView().findViewById(R.id.fourDayRV);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(llm);
                foreDayHAdapter = new ForeDayHAdapter(fourDayHResponseModel);
                recyclerView.setAdapter(foreDayHAdapter);*/


            } else {
                Toast.makeText(requireActivity(), "Error here:forecast", Toast.LENGTH_SHORT).show();
            }
        });
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void cheekLocationPermission() {
        if (LocationPermissionService.isLocationPermissionGranted(getActivity())) {
            //detect location
            detectUserLocation();
        } else {
            LocationPermissionService.requestLocationPermission(launcher);
        }

    }

    private void detectUserLocation() {
        providerClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) return;
            //here we get lat 7 lon
            /*double latitude = location.getLatitude();
            double longitude = location.getLongitude();*/
            viewModel.setLocation(location);
            viewModel.loadData();
            //Log.i("WatherApps", "detectUserLocation: "+latitude+" "+longitude);

        });
    }

}