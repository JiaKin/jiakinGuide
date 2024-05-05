package com.example.jiaqiguide.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.ui.Component.AudioButton;
import com.example.jiaqiguide.ui.Component.DirectionButton;
import com.example.jiaqiguide.ui.Component.EditButton;
import com.example.jiaqiguide.ui.Component.EditTextButton;
import com.example.jiaqiguide.ui.Component.GMap;
import com.example.jiaqiguide.ui.Component.TextBox;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    FrameLayout mainLayout;
    LinearLayout fl;//布局
    SupportMapFragment supportMapFragment = null;//布局
    FragmentTransaction transaction = null;//布局
    GMap gmap = null; //地图
    LocationManager locationManager;//地点
    int fid = -1;//布局ID
    ActivityResultLauncher<String> requestMapPermissionLauncher; //权限所求

    AudioButton audioButton = null;

    EditButton editButton = null;

    TextBox textBox = null;
    EditTextButton etb = null;
    DirectionButton ditb = null;

    LocationManager location_manager = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //声明组件

        editButton = sysprocess(new EditButton(this.getContext()));
        gmap = sysprocess(new GMap(this.getContext()));

        audioButton = sysprocess(new AudioButton(this.getContext()));
        gmap.setAudioButton(audioButton);
        mainLayout = sysprocess(new FrameLayout(this.getContext()));
        textBox = sysprocess(new TextBox(this.getContext()));
        etb = sysprocess(new EditTextButton(this.getContext(), this.getActivity()));
        ditb = sysprocess(new DirectionButton(this.getContext(), this.getActivity()));



        //组件加载

        editButton.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        if (fid == -1 || supportMapFragment == null || transaction == null) {
            fid = View.generateViewId();
            fl = new LinearLayout(this.getContext());
            fl.setOrientation(LinearLayout.VERTICAL);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //fl.setBackgroundColor(Color.BLACK);
            fl.setLayoutParams(params);
            fl.setId(fid);
            //audioButton = new AudioButton(this.getContext());
            fl.addView(audioButton.getView());
            supportMapFragment = SupportMapFragment.newInstance();
            transaction = getChildFragmentManager().beginTransaction()
                    .add(fid, supportMapFragment);
            transaction.commit();
            supportMapFragment.getMapAsync(this);
        }
        requestMapPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), gmap);
        mainLayout.addView(fl);
        mainLayout.addView(editButton.getView());
        mainLayout.addView(textBox.getView());
        mainLayout.addView(etb);
        mainLayout.addView(ditb);
        return mainLayout;
    }

    public <T> T sysprocess(T object) {
        if (object instanceof EditObject && editButton != null)
            editButton.addEditObject((EditObject) object);
        return object;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap.setMap(googleMap);
        gmap.init(this.getActivity(), requestMapPermissionLauncher, supportMapFragment.getView());
        editButton.setZoneManager(gmap.zoneManager);
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        LocationRequest locationRequest =new LocationRequest.Builder(1000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        LocationCallback lc = new LocationCallback() {

        };



        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (fusedLocationClient != null) {
            fusedLocationClient.requestLocationUpdates(locationRequest,gmap, Looper.getMainLooper());
        } else {
            Log.e("LocationManager", "Failed to retrieve LocationManager");
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}