package com.ordermanger.online.ordermanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.StringBytes;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        String result = "CF16C403D5D4E48B542D7F4C287FD69346898740A646B04D34E2F42D94F11329DD1033B49E97C5671F80808E00228AFA4DCFE7734A988B5E24736969764F0E105B6A66037EC662D92D968C495F645D5CBE38CA08767BDBE6A726D6E34937B5B17987D55B76BAAC3D";
        LogUtil.print(Des.decrypt(StringBytes.hexStr2Bytes(result.toString())));
        assertEquals("com.ordermanger.online.ordermanager", appContext.getPackageName());
    }
}
