package de.reikodd.systeminfo.services;

import de.reikodd.systeminfo.models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;


public interface WeatherService {
    @GET(".")
    Call<Weather> getWeather();
}
