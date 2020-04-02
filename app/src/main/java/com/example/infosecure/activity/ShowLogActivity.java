package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.infosecure.R;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.entity.Photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowLogActivity extends AppCompatActivity {
    private TextView textTitle,textContent;
    private int position;
    private List<Map<String,Object>>dataList;
    private GridView gridView;
    private SimpleAdapter simpAdapter;
    public static int  screenWidth;//屏幕宽度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);
        Intent intent=getIntent();
        position=intent.getIntExtra("position",0);
        initView();
    }
    public void initView(){
        textTitle=(TextView) findViewById(R.id.text_show_title);
        textContent=(TextView) findViewById(R.id.text_show_content);
        textTitle.setText(Infos.secretLogList.get(position).getTitle());
        textContent.setText(Infos.secretLogList.get(position).getContent());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        gridView = (GridView) findViewById(R.id.gridView_show_image); // step1
        dataList = new ArrayList<Map<String, Object>>(); // step2
        simpAdapter = new SimpleAdapter(this, getData(), R.layout.item_grid_show_iamge,
                new String[]{"img"}, new int[]{R.id.img_show_item});
        simpAdapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation){
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpAdapter);
    }


    private List<Map<String, Object>> getData(){
        for (int i=0; i<Infos.secretLogList.get(position).images_base64.size(); i++) {
            Bitmap bitmap= Photos.base64ToImage(Infos.secretLogList.get(position).images_base64.get(i));
            Log.d("转换图片base64",Infos.secretLogList.get(position).images_base64.get(i));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img",bitmap);
            dataList.add(map);
        }
        return dataList;
    }
}
