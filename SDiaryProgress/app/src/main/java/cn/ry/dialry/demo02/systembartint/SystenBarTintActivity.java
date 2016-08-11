package cn.ry.dialry.demo02.systembartint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ry.dialry.R;
import cn.ry.dialry.demo02.actionbar.ActionBarTestActivity;

/**
 * Created by ruibiao on 16-8-10.
 */
public class SystenBarTintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systembartint);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ColorActivity:
                startActivity(new Intent(this, ColorActivity.class));
                break;
            case R.id.DefaultActivity:
                startActivity(new Intent(this, DefaultActivity.class));
                break;

            case R.id.MatchActionBarActivity:
                startActivity(new Intent(this, MatchActionBarActivity.class));
                break;

            case R.id.SamplesListActivity:
                startActivity(new Intent(this, ActionBarTestActivity.class));
                break;

        }
    }
}
