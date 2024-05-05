package com.example.jiaqiguide.ui.Component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.jiaqiguide.Class.EditObject;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;

public class DirectionButton extends androidx.appcompat.widget.AppCompatButton implements EditObject {
    ZoneManager zoneManager = null;
    public boolean clicked = false;
    Drawable d = null;
    public DirectionButton(Context context,Activity activity) {
        super(context);
        this.setVisibility(Button.INVISIBLE);
        FrameLayout.LayoutParams lp =   new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.START;
        lp.bottomMargin = 400;
        lp.leftMargin = 10;
        this.setLayoutParams(lp);
        this.setText("DIRE");
        this.setId(R.id.DirecitonButtonID);
        d = this.getBackground();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked){
                    v.setBackground(d);
                    clicked = false;
                }else{
                    v.setBackgroundColor(Color.BLUE);
                    clicked = true;
                }
            }
        });
    }
    @Override
    public void TurnOnEditMode(Object ojb) {
        zoneManager = (ZoneManager)ojb;
        this.setVisibility(Button.VISIBLE);
    }

    @Override
    public void TurnOffEditMode(Object ojb) {
        zoneManager = null;
        if(this.clicked)
            this.callOnClick();
        this.clicked = false;
        this.setVisibility(Button.INVISIBLE);
    }
}
