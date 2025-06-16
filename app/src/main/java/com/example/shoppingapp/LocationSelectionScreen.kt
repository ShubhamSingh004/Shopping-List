package com.example.shoppingapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelectionScreen(
    paddingValues: PaddingValues,
    location: LocationData,
    onLocationSelected: (LocationData) -> Unit
){
    val userLocation = remember {
        mutableStateOf(LatLng(location.latitude, location.longitude))
    }

    var cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }
}