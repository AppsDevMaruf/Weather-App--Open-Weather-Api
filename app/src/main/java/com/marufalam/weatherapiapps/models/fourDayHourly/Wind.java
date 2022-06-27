package com.marufalam.weatherapiapps.models.fourDayHourly;

import com.google.gson.annotations.SerializedName;

public class Wind{

	@SerializedName("deg")
	private int deg;

	@SerializedName("speed")
	private double speed;

	@SerializedName("gust")
	private double gust;

	public void setDeg(int deg){
		this.deg = deg;
	}

	public int getDeg(){
		return deg;
	}

	public void setSpeed(double speed){
		this.speed = speed;
	}

	public double getSpeed(){
		return speed;
	}

	public void setGust(double gust){
		this.gust = gust;
	}

	public double getGust(){
		return gust;
	}

	@Override
 	public String toString(){
		return 
			"Wind{" + 
			"deg = '" + deg + '\'' + 
			",speed = '" + speed + '\'' + 
			",gust = '" + gust + '\'' + 
			"}";
		}
}