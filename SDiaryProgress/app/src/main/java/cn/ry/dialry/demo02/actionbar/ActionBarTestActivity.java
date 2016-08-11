package cn.ry.dialry.demo02.actionbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import cn.ry.dialry.R;

/**
 * Created by ruibiao on 16-8-9.
 */
public class ActionBarTestActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar_test);
        actionBar = getSupportActionBar();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show:
                actionBar.show();
                break;

            case R.id.hide:
                actionBar.hide();
                break;

            case R.id.home:
                startActivity(new Intent(ActionBarTestActivity.this, ActionBarHomeActivity.class));
                break;

            case R.id.func:
                startActivity(new Intent(ActionBarTestActivity.this, ActionBarViewActivity.class));
                break;

            case R.id.tab:
                startActivity(new Intent(ActionBarTestActivity.this, ActionBarTabActivity.class));
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar03, menu);
        menu.getItem(0).setIcon(R.mipmap.ic_menu_delete);
        return true;
    }
}
