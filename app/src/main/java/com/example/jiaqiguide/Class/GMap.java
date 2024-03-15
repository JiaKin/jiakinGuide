package com.example.jiaqiguide.Class;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GMap implements
        LocationListener, ActivityResultCallback {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private Marker PositionMark;
    private GoogleMap googleMap;
    private Bitmap myBitmap;
    ZoneManager zones;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    LocationManager locationManager = null;

    public GMap() {
    }

    public GMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void init(Context context, ActivityResultLauncher<String> art) {
        zones = new ZoneManager(context, googleMap);
        googleMap.setOnMapLongClickListener(zones);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        requestPermissionLauncher = art;
        try {
            String[] str = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 10));
    }

    @Override
    public void onActivityResult(Object o) {
        if (ActivityCompat.checkSelfPermission(zones.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(zones.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            this.googleMap.setMyLocationEnabled(true);
        }
        if(locationManager == null){
            locationManager = (LocationManager) zones.context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }

}
