package cn.ry.dialry.demo02.actionbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-8-9.
 */
public class ActionBarViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar02, menu);
        return true;
    }
}
