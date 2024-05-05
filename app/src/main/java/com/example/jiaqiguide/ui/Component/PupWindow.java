package com.example.jiaqiguide.ui.Component;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiaqiguide.Class.Zone;
import com.example.jiaqiguide.Class.ZoneManager;
import com.example.jiaqiguide.R;
import com.google.android.gms.maps.model.Polygon;

public class PupWindow{
    View popupView;
    PopupWindow popupWindow;
    View rootView;
    public PupWindow(Activity activity, ZoneManager zoneManager, Polygon polygon){
        popupView = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.popupwindowlayout, null);
        popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 必须设置背景来响应点击事件
// 显示在视图正中心
        rootView = activity.getWindow().getDecorView().getRootView(); // 获取根视图
// 其他事件
        TextView tv = popupView.findViewById(R.id.pop_text);
        if(zoneManager.getZone(polygon)!=null){
            tv.setText(zoneManager.getZone(polygon).getName());
        }
        else{
            Toast.makeText(zoneManager.getContext(),"区域不存在,无法更改名称.",Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        }

// 设置按钮点击事件
        popupView.findViewById(R.id.button_delete).setOnClickListener(view -> {
            zoneManager.deletePoly(polygon);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.button_edit).setOnClickListener(view -> {
            Zone z= zoneManager.getZone(polygon);
            String ss = ((EditText)popupView.findViewById(R.id.edit_text_input)).getText().toString();
            if(z!=null&&!ss.isEmpty())
                z.setName(ss);
            // 处理修改操作
            popupWindow.dismiss();
        });

    }
    public void show(boolean editMode){
        if(editMode)
            popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }
}
