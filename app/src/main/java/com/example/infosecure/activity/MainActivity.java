package com.example.infosecure.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.infosecure.MessageService;
import com.example.infosecure.ReceiveSmsReceiver;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.fragment.MainFragment;
import com.example.infosecure.fragment.MessageFragment;
import com.example.infosecure.fragment.PhListFragment;
import com.example.infosecure.R;
import com.example.infosecure.fragment.UserCenterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.infosecure.activity.FileEncryptActivity.verifyStoragePermissions;

public class MainActivity extends AppCompatActivity {
    private Fragment fragMain=new MainFragment();
    private Fragment fragMessage=new MessageFragment();
    private Fragment fragPhList=new PhListFragment();
    private Fragment fragUserCenter=new UserCenterFragment();
    private Fragment[] frags=new Fragment[]{fragMain,fragPhList,fragMessage,fragUserCenter};
    //private ReceiveSmsReceiver receiver;
    private int lastFragment=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        Infos.getInfos();
        Intent start=new Intent(this, MessageService.class);
        startService(start);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragMain).commit();
        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationItemSelected(item.getItemId());
                return true;
            }
        });

        //SQLiteStudioService.instance().start(this);
    }

    private  void requestPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1);
        }
        verifyStoragePermissions(this);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},201);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        //this.unregisterReceiver(receiver);
        super.onDestroy();
    }
    private void navigationItemSelected(int itemId) {
        Log.d("上一个活动",""+lastFragment);
        switch (itemId) {
            case R.id.navigation_home:
                if(lastFragment!=0){
                    switchFragment(lastFragment,0);
                    lastFragment=0;
                }
                break;
            case R.id.navigation_phlist:
                if(lastFragment!=1){
                    switchFragment(lastFragment,1);
                    lastFragment=1;
                }
                break;
            case R.id.navigation_message:
                if(lastFragment!=2){
                    switchFragment(lastFragment,2);
                    lastFragment=2;
                }
                break;
            case R.id.navigation_usercenter:
                if(lastFragment!=3){
                    switchFragment(lastFragment,3);
                    lastFragment=3;
                }
                break;
        }
    }
    /**
     *切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(frags[lastfragment]);
        if (frags[index].isAdded() == false) {
            transaction.add(R.id.fragmentContainer, frags[index]);
        }
        transaction.show(frags[index]).commitAllowingStateLoss();
    }

}
