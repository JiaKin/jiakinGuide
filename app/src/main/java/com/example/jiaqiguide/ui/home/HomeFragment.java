package com.example.jiaqiguide.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.jiaqiguide.Class.GMap;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;
import com.example.jiaqiguide.databinding.FragmentHomeBinding;
import com.example.jiaqiguide.ui.Component.AudioButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {


    LinearLayout fl;//布局
    SupportMapFragment supportMapFragment = null;//布局
    FragmentTransaction transaction = null;//布局
    GMap gmap; //地图
    LocationManager locationManager;//地点
    int fid = -1;//布局ID
    ActivityResultLauncher<String> requestMapPermissionLauncher; //权限所求

    AudioButton audioButton;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gmap = new GMap();
        audioButton = new AudioButton(this.getContext());

        if (fid == -1 || supportMapFragment == null || transaction == null) {
            fid = View.generateViewId();
            fl = new LinearLayout(this.getContext());
            fl.setOrientation(LinearLayout.VERTICAL);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //fl.setBackgroundColor(Color.BLACK);
            fl.setLayoutParams(params);
            fl.setId(fid);
            audioButton = new AudioButton(this.getContext());
            fl.addView(audioButton.getView());
            supportMapFragment = SupportMapFragment.newInstance();
            transaction = getChildFragmentManager().beginTransaction()
                    .add(fid, supportMapFragment);
            transaction.commit();
            supportMapFragment.getMapAsync(this);
        }

        requestMapPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), gmap);

        return fl;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap.setMap(googleMap);
        gmap.init(this.getContext(),requestMapPermissionLauncher);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}