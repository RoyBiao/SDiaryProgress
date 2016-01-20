package cn.ry.dialry.demo01.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import junit.framework.Assert;

/**
 * Created by ruibiao on 16-1-16.
 */
public class ExampleTest extends InstrumentationTestCase {

    public void testAdd(){
        int a=2;
        int b=3;
        Assert.assertEquals(a,b);
    }
}
