package com.example.jiaqiguide.Class;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.google.maps.android.PolyUtil;
import androidx.core.graphics.ColorUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Zone{
    public PolygonOptions po;
    public String Name;
    public String Description;
    public boolean isClicked =false;
    public Zone(GoogleMap map,List<LatLng> lats,String Name,Color c) throws Exception {
        if(lats.size()<3)throw new Exception("Lats less then 3...");
        if(c==null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Random r = new Random();
            c= Color.valueOf(r.nextInt(255),r.nextInt(255),r.nextInt(255),100);
        }
        po = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            po = new PolygonOptions()
                    .addAll(fixLatlngs(lats))
                    .strokeWidth(2)
                    .strokeColor(android.R.color.white)
                    .fillColor(c.toArgb());
        }
    }
    public Zone addDescription(String des){
        this.Description =des;
        return this;
    }
    public Zone ChangeFillColor(Color c){
        if(c!=null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                po.fillColor(c.toArgb());
            }
        return this;
    }

    public List<LatLng>  fixLatlngs(List<LatLng> lats){
        LatLng p = getCenter(lats);
        TreeMap<Double,LatLng> lp = new TreeMap<Double, LatLng>();
        LatLng Start = lats.get(0);
        lp.put(0.00,Start);
        for(LatLng l : lats){
            if(l != Start){
                lp.put(getAngle(p,Start,l),l);
            }
        }
        return (List<LatLng>) lp.values();
    }
    public Double getAngle(LatLng Center,LatLng Start,LatLng End){
        LatLng ab =new LatLng(Start.latitude-Center.latitude,Start.longitude-Center.longitude);
        LatLng cb =new LatLng(End.latitude-Center.latitude,End.longitude-Center.longitude);
        double dotP = ab.latitude*cb.latitude + ab.longitude*cb.longitude;
        double mag_ab = Math.sqrt(Math.pow(Start.latitude - Center.latitude, 2) + Math.pow(Start.longitude - Center.longitude, 2));
        double mag_bc = Math.sqrt(Math.pow(Center.latitude - End.latitude, 2) + Math.pow(Center.longitude - End.longitude, 2));
        double angle = Math.acos(dotP / (mag_ab * mag_bc));
        return Math.toDegrees(angle);
    }
    public LatLng getCenter(List<LatLng> points){
        if(points == null)points = po.getPoints();
        double a = 0,b = 0;
        for(LatLng l :points){
            a+=l.latitude;
            b+=l.longitude;
        }
        return new LatLng(a/points.size(),b/points.size());
    }

    public void onPolygonLongClick(LatLng lat) {
        isClicked = !isClicked;
        if (isClicked) {
            po.fillColor(ColorUtils.setAlphaComponent(po.getFillColor(),230));
            //Do something
        } else if (!(isClicked)) {
            po.fillColor(ColorUtils.setAlphaComponent(po.getFillColor(), 100));
            //Do somthing
        }
    }
}
