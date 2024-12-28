package com.example.mausham

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mausham.api.Constant
import com.example.mausham.api.RetrofitInstance
import com.example.mausham.api.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    val weatherApi=RetrofitInstance.weatherApi

    private val _weatherResult= MutableLiveData<NetworkResponse<WeatherResponse>>()
     val weatherResult: LiveData<NetworkResponse<WeatherResponse>> = _weatherResult

    fun getData(city:String){
        viewModelScope.launch {
            _weatherResult.value=NetworkResponse.Loading
            try {
                val response=weatherApi.getWeather(apikey = Constant.apikey,city = city)
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value=NetworkResponse.Success(it)
                    }
                    Log.d("Response : ",response.body().toString())
                }
                else{
                    _weatherResult.value=NetworkResponse.Error("Failed to Load data from API !")
                    Log.d("Error : ",response.message())
                }
            }
            catch (e:Exception){
                _weatherResult.value=NetworkResponse.Error("Failed to Load data from API !")
            }

        }

        Log.d("log", "getData called with city: $city")
    }
}