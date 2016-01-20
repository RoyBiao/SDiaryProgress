package cn.ry.dialry.demo01.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ruibiao on 16-1-19.
 */
public class NDKActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView =new TextView(this);
        textView.setText(getStringFromC());
        setContentView(textView);
    }

    static {
        System.loadLibrary("MyJni");
    }

    public  native String getStringFromC();
}
