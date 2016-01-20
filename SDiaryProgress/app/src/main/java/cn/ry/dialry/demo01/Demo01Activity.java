package cn.ry.dialry.demo01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ry.dialry.R;
import cn.ry.dialry.demo01.bgarefresh.analyzerecyclerviewwithbgarefreshlayout.MainActivity;
import cn.ry.dialry.demo01.ndk.NDKActivity;
import cn.ry.dialry.demo01.okhttp.OKHttpActivity;
import cn.ry.dialry.demo01.recycleview.HomeActivity;

/**
 * Created by ruibiao on 15-12-23.
 */
public class Demo01Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo01);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okhttp:
                startActivity(new Intent(Demo01Activity.this, OKHttpActivity.class));
                break;
            case R.id.recycleview:
                startActivity(new Intent(Demo01Activity.this, HomeActivity.class));
                break;
            case R.id.analyzerecyclerviewwithbgarefreshlayout:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.ndkbegin:
                startActivity(new Intent(this, NDKActivity.class));
                break;
        }
    }
}
