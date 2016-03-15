package cn.ry.dialry.demo01.clickstatistics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ry.dialry.R;

/**
 * Created by biao on 2016/3/10.
 */
public class ClickStatisticsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_statistics);

        Button button = (Button) this.findViewById(R.id.button);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClickView(View v) {

            }
        });

    }
}
