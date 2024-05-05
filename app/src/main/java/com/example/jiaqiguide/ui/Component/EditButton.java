package com.example.jiaqiguide.ui.Component;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.Class.Zone;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;

import java.util.ArrayList;
import java.util.List;

public class EditButton extends View implements View.OnClickListener {
    Button button;
    FrameLayout fl;
    ZoneManager zm;
    AnimatorSet scaleSet = new AnimatorSet();
    List<EditObject> editObjects = new ArrayList<>();

    public EditButton(Context context) {
        super(context);
        fl = new FrameLayout(context);
        button =  new Button(context);
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.START;
        lp.bottomMargin = 10;
        lp.leftMargin = 10;
        button.setLayoutParams(lp);
        button.setText("Edit");
        button.setTextColor(Color.parseColor("#CDED95"));
        button.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.ripple_b));
        fl.addView(button);
        button.setOnClickListener(this);
    }
    public View getView(){
        return fl;
    }

    public void addEditObject(EditObject e){
        if(e==null)return;
        this.editObjects.add(e);
    }
    ////////////////GET///////////////////
    public ZoneManager getZoneManager(){
        return zm;
    }
    public void setZoneManager(ZoneManager z){
        this.zm = z;
    }
    @Override
    public void onClick(View v) {
        if(button.getText()=="Edit"){
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1.1f);
            scaleX.setRepeatCount(ObjectAnimator.INFINITE);
            scaleX.setRepeatMode(ObjectAnimator.REVERSE);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f);
            scaleY.setRepeatCount(ObjectAnimator.INFINITE);
            scaleY.setRepeatMode(ObjectAnimator.REVERSE);

            button.setText("Done");
            button.setTextColor(Color.parseColor("#682D2D"));
            button.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.ripple));

            scaleSet.play(scaleX).with(scaleY);
            scaleSet.setDuration(500);
            scaleSet.start();
            for(EditObject eo:editObjects){
                eo.TurnOnEditMode(this.zm);
            }
        }
        else{
            button.setText("Edit");
            button.setTextColor(Color.parseColor("#CDED95"));
            button.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.ripple_b));
            scaleSet.end();
            button.setScaleX(1.f);
            button.setScaleY(1.f);
            for(EditObject eo:editObjects){
                if(this.zm!=null)
                    eo.TurnOffEditMode(this.zm);
            }
            this.zm.saveNewZone();
            this.zm.showZonesOnMap();
        }
    }
}
