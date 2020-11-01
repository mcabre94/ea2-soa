package com.example.ea2soa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.example.ea2soa.R;
import com.example.ea2soa.data.RefreshTokenService;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity{
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RefreshTokenService refreshTokenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("bateria","arranca oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabSensoresFragment(), "Sensores");
        adapter.addFragment(new TabQrFragment(), "QR");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        refreshTokenService = new RefreshTokenService(this);
        refreshTokenService.startRefreshTokenAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();

        refreshTokenService.stopRefreshTokenAlarm();
    }
}
