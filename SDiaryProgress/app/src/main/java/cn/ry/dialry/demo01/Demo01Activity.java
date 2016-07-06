package cn.ry.dialry.demo01;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ry.dialry.R;
import cn.ry.dialry.demo01.bgarefresh.analyzerecyclerviewwithbgarefreshlayout.MainActivity;
import cn.ry.dialry.demo01.canvas.CanvasActivity;
import cn.ry.dialry.demo01.city.CityDialog;
import cn.ry.dialry.demo01.ndk.NDKActivity;
import cn.ry.dialry.demo01.okhttp.OKHttpActivity;
import cn.ry.dialry.demo01.percent.PercentFrameLayoutActivity;
import cn.ry.dialry.demo01.percent.PercentLinearLayoutActivity;
import cn.ry.dialry.demo01.percent.PercentRelayouLayoutActivity;
import cn.ry.dialry.demo01.recycleview.HomeActivity;
import cn.ry.dialry.demo01.surface.SurfaceViewActivity;

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
            case R.id.okhttp://
                startActivity(new Intent(Demo01Activity.this, OKHttpActivity.class));
                break;
            case R.id.recycleview://
                startActivity(new Intent(Demo01Activity.this, HomeActivity.class));
                break;
            case R.id.analyzerecyclerviewwithbgarefreshlayout://
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.ndkbegin://
                startActivity(new Intent(this, NDKActivity.class));
                break;
            case R.id.percentFrameLayout://
                startActivity(new Intent(this, PercentFrameLayoutActivity.class));
                break;
            case R.id.percentRelativeLayout://
                startActivity(new Intent(this, PercentRelayouLayoutActivity.class));
                break;
            case R.id.percentLinearLayout://
                startActivity(new Intent(this, PercentLinearLayoutActivity.class));
                break;
            case R.id.cityDialogFragment://
                /**
                 * 为了不重复显示dialog，在显示对话框之前移除正在显示的对话框。
                 */
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(CityDialog.class.getName());
                if (null != fragment) {
                    ft.remove(fragment);
                }

                /**
                 * 0:默认样式
                 * 1：无标题样式
                 * 2：无边框样式
                 * 3：不可输入，不可获得焦点样式
                 * 可根据参数不同执行测试这几种样式的对话框。
                 */
                CityDialog dialogFragment = CityDialog.newInstance(0);
                dialogFragment.show(ft, CityDialog.class.getName());
                break;

            case R.id.canvas:
                startActivity(new Intent(this, CanvasActivity.class));
                break;

            case R.id.surfaceView:
                startActivity(new Intent(this, SurfaceViewActivity.class));
                break;
        }
    }
}
