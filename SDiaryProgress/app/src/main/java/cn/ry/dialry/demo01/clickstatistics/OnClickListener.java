package cn.ry.dialry.demo01.clickstatistics;

import android.view.View;

/**
 * Created by biao on 2016/3/10.
 */
public abstract class OnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        onClickView(v);
    }

    public abstract void  onClickView(View v);

}
