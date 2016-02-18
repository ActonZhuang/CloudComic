package com.wallf.cloudcomic;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

/**
 * @author acton
 */
public class ReboundViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reboundview);
        if (savedInstanceState == null) {
            ReboundViewFragment fgt = ReboundViewFragment.newInstance();
            FragmentManager fgtMgr = getSupportFragmentManager();
            fgtMgr.beginTransaction().add(R.id.content_root, fgt).commit();
        }
        initUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
