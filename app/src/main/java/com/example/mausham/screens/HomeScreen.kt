package com.example.mausham.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.mausham.NetworkResponse
import com.example.mausham.R
import com.example.mausham.WeatherViewModel
import com.example.mausham.api.WeatherResponse

@Composable

fun HomeScreen(viewModel:WeatherViewModel,modifier: Modifier) {
    val keyboardController=LocalSoftwareKeyboardController.current
    var city by remember { mutableStateOf("") }
    val weatherResult=viewModel.weatherResult.observeAsState()
    Column(modifier= Modifier
        .systemBarsPadding()
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row(horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            OutlinedTextField(
                value = city,
                modifier = Modifier.weight(1f),
                onValueChange = {city=it},
                label = { Text("Search For any City") },
            )
            IconButton(onClick = {
                keyboardController?.hide()
                viewModel.getData(city)
            })
            {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
            }
        }
        when(val result=weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {

            }
        }
    }

}

@Composable
fun WeatherDetails(data:WeatherResponse) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
            , verticalAlignment = Alignment.Bottom
        ) {
            Icon(imageVector = Icons.Default.LocationOn,
                contentDescription = "location icon",
                modifier = Modifier.size(40.dp))
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${data.current.temp_c}°C", fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(model="https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "weather icon",
            modifier = Modifier.size(160.dp))
        Text(text = data.current.condition.text, fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card() {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    WeatherKeyValue("Humidity","${data.current.humidity}%")
                    WeatherKeyValue("Wind Speed","${data.current.wind_kph}km/h")

                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    WeatherKeyValue("UV","${data.current.uv}%")
                    WeatherKeyValue("Precipitation","${data.current.precip_mm} mm")

                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    WeatherKeyValue("Local Time",data.location.localtime.split(" ")[1])
                    WeatherKeyValue("Local Date",data.location.localtime.split(" ")[0])
                }
            }
        }

    }


}

@Composable
fun WeatherKeyValue(key:String,value:String) {
    Column(modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }

}
