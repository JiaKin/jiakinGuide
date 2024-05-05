package com.example.jiaqiguide.Class;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import com.example.jiaqiguide.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;


public class Zone{
    private List<LatLng>lats = new ArrayList<>();
    private String Name = null;
    private String Description = "music.mp3";
    private Direction direction = null;
    private transient  Context context = null;
    public Zone(){
        this.direction = new Direction();
    }
    public Zone(Context context){
        this.direction = new Direction();
        this.context = context;
    }
    public Zone(String name){
        this.Name = name;
        this.direction = new Direction();
    }
    public Zone(String name ,List<LatLng>lats){
        this(name);
        this.lats = lats;
        Random random = new Random();
        if(lats==null||lats.size()<3)return;

        this.direction = new Direction();
    }
    public Zone(String name ,List<LatLng>lats,Context context){
        this(name,lats);
        this.context = context;
    }

    public Zone(String name,List<LatLng>lats,String description){
        this(name,lats);
        this.Description = description;
        this.direction = new Direction();
    }
    public Zone(String name,List<LatLng>lats,String description,Direction dir){
        this(name,lats,description);
        this.direction = dir;
    }
    public GroundOverlayOptions getGOLOption(){
        Gson json = new Gson();

        Bitmap bit =this.getArrowFixedImage(this.direction.getAngle());
        Log.d("message3", json.toJson(bit));
        LatLngBounds.Builder llb =new LatLngBounds.Builder();
        for(LatLng a:this.getLats()){
            llb.include(a);
        }

        GroundOverlayOptions gop =new GroundOverlayOptions().positionFromBounds(llb.build()).image(BitmapDescriptorFactory.fromBitmap(bit));
        Log.d("message3", json.toJson(gop));
        return  gop;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public Bitmap getArrowFixedImage(float angle){
        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(),R.raw.sarrow);
        //Bitmap  outPutImage = Bitmap.createBitmap(bitmap.getWidth()*4,bitmap.getHeight()*4,bitmap.getConfig());
        //Canvas canvas = new Canvas(outPutImage);
        Matrix matrix = new Matrix();
        matrix.postRotate(360-angle,bitmap.getWidth()/2f,bitmap.getHeight()/2f);
        Bitmap rotatedBitMap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Log.d(TAG, bitmap.getWidth()+".............................");
        /*for(int x = 0;x<outPutImage.getWidth();x+=rotatedBitMap.getWidth()){
            for(int y = 0;y<outPutImage.getHeight();y+=rotatedBitMap.getHeight()){
                canvas.drawBitmap(rotatedBitMap,x,y,null);
            }
        }*/
        return rotatedBitMap;
    }

    public int[] getArrowFixedSize(){
        double x_min =this.getLats().get(0).latitude,y_min = this.getLats().get(0).longitude;
        double x_max=this.getLats().get(0).latitude,y_max = this.getLats().get(0).longitude;
        for(LatLng ll:this.getLats()){
            x_min = (ll.latitude < x_min)?(ll.latitude):(x_min);
            y_min = (ll.longitude< y_min)?(ll.longitude):(y_min);
            x_max = (ll.latitude > x_max)?(ll.latitude):(x_max);
            y_max = (ll.longitude> y_max)?(ll.longitude):(y_max);
        }
        return new int[]{(int)(x_max-x_min+1),(int)(y_max-y_min+1)};

    }
    public PolygonOptions getPolygonOption(){
        Random random = new Random();
        return new PolygonOptions()
                .addAll((this.lats))
                .strokeWidth(2)
                .strokeColor(R.color.white)
                .fillColor(Color.parseColor(String.format("#%02X%02X%02X%02X",100,random.nextInt(256), random.nextInt(256), random.nextInt(256))))
                .clickable(true);
    }
    //////////////////////   GET    ///////////////////////////////
    public List<LatLng> getLats(){
        if(lats ==null) return new ArrayList<LatLng>();
        return lats;
    }
    public String getName(){
        return Name;
    }
    public String getDescription(){
        return Description;
    }
    public Direction getDirection(){
        return this.direction;
    }
    /////////////////////    SET    ///////////////////////////////
    public void setLats(List<LatLng> lats){
        if(lats==null) return;
        if(lats.size()<3)return;
        this.lats = lats;
    }
    public void setName(String name){
        this.Name = name;
    }
    public void setDescription(String description){
        this.Description = description;
    }
    public void setDirection(Direction dir){
        this.direction = dir;
    }

    ///////////////////////  STATIC  //////////////////////////////
    public static List<LatLng>  fixLatlngs(List<LatLng> lats){
        LatLng p = getCenter(lats);
        TreeMap<Double,LatLng> lp = new TreeMap<Double, LatLng>();
        LatLng Start = lats.get(0);
        for(LatLng l : lats){
            lp.put(getAngle(p,l),l);
        }
        Log.e("shit",lp.keySet().toString()+"||"+lp.values().toString());

        return new ArrayList<LatLng>(lp.values());
    }
    public static Double getAngle(LatLng Center,LatLng point){
        LatLng p = new LatLng(point.latitude-Center.latitude,point.longitude-Center.longitude);
        double angle = Math.toDegrees(Math.atan(p.latitude/p.longitude));
        if((p.latitude>=.0f)&&(angle<0))
            angle+=180;
        if(p.latitude<0&&angle>=0)
            angle = angle-180;
        return angle;
    }
    public static LatLng getCenter(List<LatLng> points){
        //if(points == null)points = po.getPoints();
        double a = 0,b = 0;
        for(LatLng l :points){
            a+=l.latitude;
            b+=l.longitude;
        }
        return new LatLng(a/points.size(),b/points.size());
    }
    public List<LatLng> getDirectionAB(){
        List<LatLng> ll = new ArrayList<LatLng>();
        Gson json = new Gson();
        Log.d("message2222", json.toJson(direction));
        ll.add(direction.one);
        ll.add(direction.two);
        return ll;
    }
    public static Zone newZone(Context context){
        return new Zone(context);
    }
    public static class Direction {
        private LatLng direction;
        public LatLng one;
        public LatLng two;

        public Direction() {
            one = null;
            two = null;
            direction = null;
        }

        public Direction(LatLng one, LatLng two) {
            direction = new LatLng(two.latitude - one.latitude, two.longitude - one.longitude);
        }

        public void Add(LatLng point) {
            if (one == null)
                one = point;
            else if (two == null)
                two = point;
            if (one != null && two != null) {
                direction = new LatLng(two.latitude - one.latitude, two.longitude - one.longitude);
                Log.e("Erro", "sssssssssssssssssssssss");
            }

        }

        public void setDirection(LatLng one, LatLng two) {
            direction = new LatLng(two.latitude - one.latitude, two.longitude - one.longitude);
        }

        public LatLng getDIR() {
            return this.direction;
        }

        public boolean isInit() {
            return one != null && two != null;
        }

        public boolean isSameDirection(Direction dir) {
            Log.d("dir", "dir:"+dir.getDIR());
            Log.d("this.dir", "this.dir:"+this.getDIR());
            double dot_product = (this.getDIR().latitude*dir.getDIR().latitude)+(this.getDIR().longitude*dir.getDIR().longitude);
            Log.d("dot_product", "dot_product:"+dot_product);
            double magnitude_a = Math.sqrt(Math.pow(this.getDIR().latitude,2) + Math.pow( this.getDIR().longitude,2));
            Log.d("magnitude_a", "magnitude_a:"+magnitude_a);
            double magnitude_b = Math.sqrt(Math.pow(dir.getDIR().latitude,2) + Math.pow( dir.getDIR().longitude,2));
            Log.d("magnitude_b", "magnitude_b:"+magnitude_b);
            double cos_theta = dot_product/(magnitude_a*magnitude_b);
            Log.d("cos_theta", dot_product+"/("+magnitude_a+"*"+magnitude_b+")="+cos_theta);
            double angle_radians = Math.acos(cos_theta);
            Log.d("angle_radians", "angle_radians:"+angle_radians);
            double angle_degrees = Math.toDegrees(angle_radians);
            Log.d("Angle", "angle:"+angle_degrees);
            return (angle_degrees<80)?true:false;
        }

        public float getAngle() {
            LatLng point = new LatLng(two.latitude - one.latitude, two.longitude - one.longitude);
            Log.d(TAG, "Point---" + point.latitude + ":" + point.longitude);
            double d = 1;
            if (point.latitude != 0) {
                d = Math.atan2(point.latitude, point.longitude);
                Log.d(TAG, "atan2-》alpha " + d);
            } else {
                return (float) (point.longitude > 0 ? 90.00 : -90.00);
            }
            return (float) (d * (180 / Math.PI));
        }
    }
}


