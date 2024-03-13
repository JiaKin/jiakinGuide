package com.example.jiaqiguide.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jiaqiguide.Class.GMap;
import com.example.jiaqiguide.R;
import com.example.jiaqiguide.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private GMap googlemap;
    private LinearLayout linearLayout = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        googlemap = new GMap(getContext(),savedInstanceState,new GoogleMapOptions());
        this.getActivity().setContentView(googlemap);
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();

        FrameLayout fl = new FrameLayout(this.getContext());
        fl.setId(View.generateViewId());


        //getChildFragmentManager().beginTransaction().add(R.id.map_container, supportMapFragment).commit();

        supportMapFragment.getMapAsync(this);

        return root;
    }

    //============================================================================ Jiahao add
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout hoLayout = new LinearLayout(this.getContext());
        hoLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200));
        hoLayout.setOrientation(LinearLayout.HORIZONTAL);
        hoLayout.addView(gp);
        hoLayout.addView(editButton);
        //添加整体布局
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(hoLayout);
        layout.addView(gp);
        //添加非常态布局s
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.addView(layout);
        relativeLayout.addView(textBox);
        relativeLayout.addView(sundRecord);
        setContentView(relativeLayout);

    }
    //============================================================================ Jiahao add
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}