package com.example.jiaqiguide.Class;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ZoneManager implements Iterable<Zone>{

    List<Zone> zones = new ArrayList<Zone>();
    Context context;
    GoogleMap mp;
    public ZoneManager(Context context,GoogleMap mp){
        this.context = context;
        this.mp = mp;
    }
    @Override
    public Iterator<Zone> iterator() {
        return zones.iterator();
    }
    @Override
    public void forEach(@NonNull Consumer<? super Zone> action) {
        Iterable.super.forEach(action);
    }

    public Zone addZone(List<LatLng> lats, String name, Color color,String disc)throws Exception{
        Zone z = (new Zone(this.mp,lats,name,color)).addDescription(disc);
        zones.add(z);
        return z;
    }
    public boolean removeZone(Zone z){
        return zones.remove(z);
    }
    public void onMapLongClick(LatLng latLng){
        for(Zone z:zones){
            if(PolyUtil.containsLocation(latLng,z.p.getPoints(),true)){
                z.onPolygonLongClick(latLng);
            }
        }
    }
    public void onPolygonClick(Polygon polygon){
        for(Zone z:zones){
            if(polygon == z.p){
                z.onPolygonLongClick(null);
            }
        }
    }
}
