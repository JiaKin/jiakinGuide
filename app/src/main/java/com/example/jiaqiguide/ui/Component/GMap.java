package com.example.jiaqiguide.ui.Component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.Class.Zone;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class GMap extends LocationCallback implements
        LocationListener, ActivityResultCallback, EditObject,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnPolygonClickListener {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    //private Marker PositionMark;
    private GoogleMap googleMap;
    private Bitmap myBitmap;
    public ZoneManager zoneManager;
    public Activity activity;
    public View view;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    LocationManager locationManager = null;
    List<Marker> tempMarker = new ArrayList<Marker>();
    List<Marker> tempDirMarker = new ArrayList<Marker>();
    boolean editMode = false;


    Location CurrentLocation = null;
    public GMap(Context context) {

    }

    public GMap(Context context, GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
    public void init(Activity activity, ActivityResultLauncher<String> art, View view) {
        this.activity = activity;
        this.view = view;
        zoneManager = new ZoneManager(activity.getBaseContext(),googleMap,activity);
        zoneManager.initZoneManagerFromData();

        googleMap.setOnMapLongClickListener(zoneManager);
        googleMap.setOnPolygonClickListener(this);
        googleMap.setOnMarkerClickListener(this);

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
            String[] str = activity.getBaseContext().getPackageManager().getPackageInfo(activity.getBaseContext().getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public void reLoadZonesShowOnMap(){
        //zoneManager.showZonesOnMap();
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));

    }

    @Override
    public void onActivityResult(Object o) {
        if (ActivityCompat.checkSelfPermission(zoneManager.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(zoneManager.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            this.googleMap.setMyLocationEnabled(true);
        }
        if (locationManager == null) {
            locationManager = (LocationManager) zoneManager.getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if(!zoneManager.getDirectionMode())

            //划分区域
            tempMarker.add(googleMap.addMarker(new MarkerOptions().position(latLng)));
        else{
            //设定指向方向
            if(!zoneManager.isDirectionInit()){
                tempDirMarker.add(googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                zoneManager.setTempDirection(latLng);
            }
        }
    }


    @Override
    public void TurnOnEditMode(Object ojb) {
        googleMap.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this.zoneManager.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.zoneManager.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        editMode = true;
    }

    @Override
    public void TurnOffEditMode(Object ojb) {
        googleMap.setOnMapClickListener(null);
        if (ActivityCompat.checkSelfPermission(this.zoneManager.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.zoneManager.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        List<LatLng> latLngList = tempMarker.stream()
                .map(marker -> marker.getPosition())
                .collect(Collectors.toList());
        if(latLngList.size()>2)
            ((ZoneManager)ojb).setTempLats(latLngList);
        else
            ((ZoneManager)ojb).setTempLats(null);
        //数据保存 ****************************************


        //处理完数据后
        for(Marker m :tempMarker){
            m.remove();
        }
        for(Marker m:tempDirMarker){
            m.remove();
        }
        tempDirMarker = new ArrayList<Marker>();
        tempMarker = new ArrayList<Marker>();
        editMode = false;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.remove();
        if(tempMarker.contains(marker)){
            tempMarker.remove(marker);
        } else if(tempDirMarker.contains(marker)){
            tempDirMarker.remove(marker);
        }
        return true;
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        for(Marker m:tempDirMarker){
            m.remove();
        }
        tempDirMarker.clear();
        PupWindow popWindow =  new PupWindow(activity,zoneManager,polygon);
        popWindow.show(editMode);


        if(((DirectionButton)activity.findViewById(R.id.DirecitonButtonID)).clicked){
            LatLng A = zoneManager.getZone(polygon).getDirectionAB().get(0);
            LatLng B = zoneManager.getZone(polygon).getDirectionAB().get(1);
            tempDirMarker.add(googleMap.addMarker(new MarkerOptions().position(A).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            tempDirMarker.add(googleMap.addMarker(new MarkerOptions().position(B).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
        }else{
            empty_marker();
        }
    }
    public void empty_marker(){
        for(Marker m:tempDirMarker){
            m.remove();
        }
        tempDirMarker.clear();
        for(Marker m:tempMarker){
            m.remove();
        }
        tempMarker.clear();
    }

    public AudioButton ab = null;
    public Zone CurrentZone = null;
    public void setAudioButton(AudioButton a){
        this.ab = a;
    }
    @Override
    public void onLocationResult(@NonNull LocationResult locationResult) {
        super.onLocationResult(locationResult);

        Location location = locationResult.getLastLocation();
        if(CurrentLocation == null)
            CurrentLocation = location;
        else if(CurrentLocation.getLatitude() != location.getLatitude()&&CurrentLocation.getLongitude()!=location.getLongitude()){
            LatLng loca = new LatLng(location.getLatitude(),location.getLongitude());
            LatLng curr = new LatLng(CurrentLocation.getLatitude(),CurrentLocation.getLongitude());
            Zone zone = zoneManager.getZone(loca);
            if(zone != null){
                Zone.Direction dir = new Zone.Direction(curr,loca);
                if(zone.getDirection().isSameDirection(dir)&&CurrentZone!=zone){
                    ab.setAudio(zone.getName());
                    ab.play();
                    CurrentZone = zone;
                }
            }
            CurrentLocation = location;
        }


    }
    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        if (!locationAvailability.isLocationAvailable()) {
            // 位置信息不可用时的处理逻辑
        }
    }

}
