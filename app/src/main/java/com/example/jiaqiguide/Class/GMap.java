package com.example.jiaqiguide.Class;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GMap extends  MapView implements OnMapReadyCallback,EditObject, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener ,GoogleMap.OnMapLongClickListener, GoogleMap.OnPolygonClickListener{

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private Marker PositionMark;
    private GMap mp;
    private Bitmap myBitmap;
    ZoneManager zones;
    GoogleMap googleMap;
    public GMap(@NonNull Context context, Bundle savedInstanceState, GoogleMapOptions options) {
        super(context,options);
        onCreate(savedInstanceState);
        getMapAsync(this);
        mp= this;
        //myBitmap =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.raw.car), 100,100, true);
        zones = new ZoneManager(this.getContext(),googleMap);
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        this.setClickable(true);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMapt) {
        this.googleMap = googleMapt;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void empty() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void EndOperation(String inputName) {

    }
    public boolean checkResource(int ID){
        try{
            AssetFileDescriptor as=getResources().openRawResourceFd(ID);
            if(as != null) {
                as.close();
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
    public int getResourceId(String name){
        return getResources().getIdentifier(name,"raw",this.getContext().getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void TurnOnEditMode(Object ojb) {

    }

    @Override
    public void TurnOffEditMode(Object ojb) {

    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        zones.onMapLongClick(latLng);
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        zones.onPolygonClick(polygon);
    }
}
