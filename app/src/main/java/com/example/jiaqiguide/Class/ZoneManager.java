package com.example.jiaqiguide.Class;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jiaqiguide.R;
import com.example.jiaqiguide.ui.Component.DirectionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ZoneManager implements OnMapLongClickListener{
    Map<Zone,Polygon>  zones = new HashMap<>();
    Map<Zone, GroundOverlay> zogr = new HashMap<>();
    public Zone temp;
    Context context;
    GoogleMap googleMap;
    Activity activity;

    public static class ZoneManagerData{
        public static ZoneManagerData ReadJsonAsZoneManagerData(String Json){
            Gson json = new Gson();
            ZoneManagerData data= null;
            try {
                Type lis =  new TypeToken<List<Zone>>(){}.getType();
                data = json.fromJson(Json,lis);
            }catch (Exception e){
                return null;
            }
            return data;
        }

        public static String SaveAsJson(List<Zone>zones){
            Gson json = new Gson();
            String data = json.toJson(zones);
            return data;
        }

    }







    public void initZoneManagerFromData(){
        try {
            List<Zone> temp = DefaultPath.readJson(context, DefaultPath.FileType.File, "Data", new TypeToken<List<Zone>>(){}.getType());

            Gson json = new Gson();
            Log.d("message", json.toJson(temp));

            Map<Zone,GroundOverlay> tempgr =new HashMap<>();
            Map<Zone,Polygon> temppol = new HashMap<>();

            for(Zone z:temp){
                z.setContext(this.context);
                this.zones.put(z,googleMap.addPolygon(z.getPolygonOption()));
                this.zogr.put(z,googleMap.addGroundOverlay(z.getGOLOption()));
            }
            Toast.makeText(context,"Loading completed.",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(context,"Error File Not Exist Data.",Toast.LENGTH_LONG).show();
        }
    }
    public void saveZoneManagerToJson(){

        Object[] zos = zones.keySet().toArray();
        List<Zone> zoss = new ArrayList<>();
        for(Object z:zos){
            zoss.add((Zone)z);
        }
        Gson json =new Gson();

        DefaultPath.SaveJson(context,ZoneManagerData.SaveAsJson(zoss), DefaultPath.FileType.File,"Data",activity);
    }

    public ZoneManager(Context context, GoogleMap googleMap, Activity activity){
        this.context = context;
        this.googleMap = googleMap;
        this.activity = activity;
        temp = Zone.newZone(context);
    }
    public void saveNewZone(){
        if((temp.getName()!="")&&(!temp.getName().isEmpty())&&(temp.getLats()!=null)&&(temp.getLats().size()>2)){
            zones.put(temp,null);
            Toast.makeText(context, String.format("新区域已添加.",temp.getName(),(temp.getName()!=""),(temp.getName()!=null),(temp.getLats()!=null),(temp.getLats().size()>2)), Toast.LENGTH_LONG).show();
            this.saveZoneManagerToJson();
        }else
            Toast.makeText(context, "新区域名称不可为空，创建点不能少于3个。", Toast.LENGTH_SHORT).show();
        temp =Zone.newZone(context);
    }
/////////////////////////////   SET   ///////////////////////////////////////
    public void setTempName(String name){
        this.temp.setName(name);
    }
    public void setTempLats(List<LatLng> lats){
        this.temp.setLats(lats);
    }
    public void setTempDescription(String Description){
        this.temp.setDescription(Description);
    }
    public void setTempDirection(Zone.Direction dir){
        temp.setDirection(dir);
    }
    public void setTempDirection(LatLng lt){
        temp.getDirection().Add(lt);
    }
////////////////////////////    GET   //////////////////////////////////////
    public String getTempName(){
        return this.temp.getName();
    }
    public List<LatLng> getTempLats(){
        return this.temp.getLats();
    }
    public String getDescription(){
        return this.temp.getDescription();
    }
    public Context getContext(){
        return this.context;
    }
    public  Map<Zone,Polygon> getZones(){
        return  zones;
    }
    public Zone.Direction getTempDirection(){return temp.getDirection();}

    public boolean getDirectionMode(){
        return ((DirectionButton)activity.findViewById(R.id.DirecitonButtonID)).clicked;
    }
    public boolean isDirectionInit(){
        return this.temp.getDirection().isInit();
    }
//////////////////////////// FUNC //////////////////////////
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
    public void showZonesOnMap() {
        for(Zone  z :zones.keySet()){
            if(zones.get(z)!=null)
                zones.get(z).remove();
            zones.put(z,null);
        }
        for(Zone z :zones.keySet()){
            zones.put(z, googleMap.addPolygon(z.getPolygonOption()));
            zogr.put(z,googleMap.addGroundOverlay(z.getGOLOption()));
        }
    }
    public void deletePoly(Polygon poly){
         for(Zone z:zones.keySet()){
             boolean check = true;
             for(int i=0;i<z.getLats().size();i++){
                 if(!poly.getPoints().get(i).equals(zones.get(z).getPoints().get(i))){
                     check = false;
                     break;
                 }
             }
             if(check){
                 poly.remove();
                 zones.get(z).remove();
                 (zogr.get(z)).remove();
                 zogr.remove(z);
                 zones.remove(z);
                 this.saveZoneManagerToJson();
                 return;
             }
         }
        Toast.makeText(this.context,"没有找到",Toast.LENGTH_SHORT).show();
    }
    public Zone getZone(Polygon poly){
        for(Zone zon:zones.keySet()){
            if(zones.get(zon).equals(poly)){
                return zon;
            }
        }
        return null;
 /*       for(Zone z:zones.keySet()){
            boolean check = true;
            for(int i=0;i<z.getLats().size();i++){
                if(!poly.getPoints().get(i).equals(zones.get(z).getPoints().get(i))){
                    check = false;
                    break;
                }
            }
            if(check){
                return z;
            }
        }*/
        //return null;
    }
    public Zone getZone(LatLng location){
        for(int i=0;i<this.getZones().size();i++){
            Zone zone = (Zone) this.zones.keySet().toArray()[i];
            Polygon polygon = (Polygon) this.zones.values().toArray()[i];
            if(PolyUtil.containsLocation(location,polygon.getPoints(),true)){
                return zone;
            }
        }
        return null;
    }

    public void emptyTempMark(){

    }
    public String TextToAudio(String desc){
        return "music.mp3";
    }

}
