package com.thomazfbcortez.chargebackflow.android;

import android.app.Application;

import com.thomazfbcortez.chargebackflow.AppGraph;

public class ChargebackApplication extends Application
{
    private static ChargebackApplication instance;
    private AppGraph appGraph;

    public static ChargebackApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        appGraph = AppGraph.Initializer.init(false);
    }

    public AppGraph getAppGraph()
    {
        return appGraph;
    }

    public void setMockInjectionModeTo(boolean mockInjectionMode)
    {
        appGraph = AppGraph.Initializer.init(mockInjectionMode);
    }
}
