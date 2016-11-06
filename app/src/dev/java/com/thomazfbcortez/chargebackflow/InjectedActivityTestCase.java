package com.thomazfbcortez.chargebackflow;

import android.test.ActivityInstrumentationTestCase2;

import com.thomazfbcortez.chargebackflow.android.ChargebackApplication;
import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.android.fragment.ChargebackFragment;
import com.thomazfbcortez.chargebackflow.android.fragment.FinishFragment;
import com.thomazfbcortez.chargebackflow.android.fragment.NoticeFragment;
import com.thomazfbcortez.chargebackflow.api.API;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.setDefaultAPIMockBehaviour;

public class InjectedActivityTestCase extends ActivityInstrumentationTestCase2<ChargeActivity>
{
    @Inject API api;
    @Inject EventBus eventBus;

    public InjectedActivityTestCase()
    {
        super(ChargeActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getActivity();
        ChargebackApplication app = (ChargebackApplication) getInstrumentation().getTargetContext().getApplicationContext();
        app.getAppGraph().inject(this);
        setDefaultAPIMockBehaviour(api);
    }

    protected NoticeFragment getNoticeFragment()
    {
        return (NoticeFragment) getActivity().getFragmentManager().findFragmentByTag(ChargeActivity.NOTICE_FRAGMENT_TAG);
    }

    protected ChargebackFragment getChargebackFragment()
    {
        return (ChargebackFragment) getActivity().getFragmentManager().findFragmentByTag(ChargeActivity.CHARGEBACK_FRAGMENT_TAG);
    }

    protected FinishFragment getFinishFragment()
    {
        return (FinishFragment) getActivity().getFragmentManager().findFragmentByTag(ChargeActivity.FINISH_FRAGMENT_TAG);
    }
}
