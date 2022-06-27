package com.marufalam.weatherapiapps.models.currents;

import com.google.gson.annotations.SerializedName;

public class Clouds{

	@SerializedName("all")
	private float all;

	public void setAll(float all){
		this.all = all;
	}

	public float getAll(){
		return all;
	}

	@Override
 	public String toString(){
		return 
			"Clouds{" + 
			"all = '" + all + '\'' + 
			"}";
		}
}