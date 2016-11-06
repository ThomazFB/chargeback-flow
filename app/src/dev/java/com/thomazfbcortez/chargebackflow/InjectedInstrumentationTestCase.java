package com.thomazfbcortez.chargebackflow;

import android.test.InstrumentationTestCase;

import com.thomazfbcortez.chargebackflow.android.ChargebackApplication;
import com.thomazfbcortez.chargebackflow.api.API;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.setDefaultAPIMockBehaviour;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.setDefaultEventBusMockBehaviour;

public class InjectedInstrumentationTestCase extends InstrumentationTestCase
{
    @Inject EventBus eventBus;
    @Inject API api;
    private ChargebackApplication app;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        app = (ChargebackApplication) getInstrumentation().getTargetContext().getApplicationContext();
        app.setMockInjectionModeTo(true);
        app.getAppGraph().inject(this);
        setDefaultAPIMockBehaviour(api);
        setDefaultEventBusMockBehaviour(eventBus);
    }

    @Override
    protected void tearDown() throws Exception
    {
        app.setMockInjectionModeTo(false);
        super.tearDown();
    }
}
