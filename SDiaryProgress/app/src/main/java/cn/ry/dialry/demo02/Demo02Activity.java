package cn.ry.dialry.demo02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ry.dialry.R;
import cn.ry.dialry.demo02.actionbar.ActionBarTestActivity;
import cn.ry.dialry.demo02.systembartint.SystenBarTintActivity;

/**
 * Created by ruibiao on 16-8-9.
 */
public class Demo02Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo02);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionBar:
                startActivity(new Intent(this, ActionBarTestActivity.class));
                break;

            case R.id.SystenBarTint:
                startActivity(new Intent(this, SystenBarTintActivity.class));
                break;
        }
    }
}
