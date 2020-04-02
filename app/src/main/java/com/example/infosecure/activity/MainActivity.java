package com.example.infosecure.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.infosecure.ReceiveSmsReceiver;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.fragment.MainFragment;
import com.example.infosecure.fragment.MessageFragment;
import com.example.infosecure.fragment.PhListFragment;
import com.example.infosecure.R;
import com.example.infosecure.fragment.UserCenterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment fragMain=new MainFragment();
    private Fragment fragMessage=new MessageFragment();
    private Fragment fragPhList=new PhListFragment();
    private Fragment fragUserCenter=new UserCenterFragment();
    private Fragment[] frags=new Fragment[]{fragMain,fragPhList,fragMessage,fragUserCenter};
    private ReceiveSmsReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Infos.getInfos();
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1);
        }
        receiver=new ReceiveSmsReceiver();
        IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(receiver,filter);
        Log.i("broadcast","广播启动1");
        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                navigationItemSelected(item.getItemId());
                return;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragMain).commit();
        SQLiteStudioService.instance().start(this);
    }
    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }
    private void navigationItemSelected(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.navigation_home:
                fragment = frags[0];
                break;
            case R.id.navigation_phlist:
                fragment = frags[1];
                break;
            case R.id.navigation_message:
                fragment = frags[2];
                break;
            case R.id.navigation_usercenter:
                fragment = frags[3];
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }
    }

}
