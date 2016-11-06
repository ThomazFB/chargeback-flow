package com.thomazfbcortez.chargebackflow;

import android.support.test.filters.SmallTest;

import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.android.fragment.FinishFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/*
    Tests in this class make calls to the UI,
    make sure your device is active and with the screen awake
    so everything can run in the intended environment

    Also, in order to the Espresso tests work as expected,
    the framework documentation asks for the testing device to be
    with all the animations options disabled at the device Developer Options
*/
public class FinishFragmentTests extends InjectedActivityTestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        openFinishFragment();
    }

    @Override
    protected void tearDown() throws Exception
    {
        FinishFragment finishFragment = getFinishFragment();
        if(finishFragment != null) finishFragment.dismiss();
        super.tearDown();
    }

    @SmallTest
    public void testFinishFragment_CompleteChargebackFlow()
    {
        onView(withId(R.id.finish_close_button)).perform(click());
        onView(withId(R.id.finish_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testFinishFragment_TitleShouldBePresented()
    {
        onView(withId(R.id.finish_title)).check(matches(withText(R.string.finish_title_text)));
    }

    @SmallTest
    public void testFinishFragment_MessageShouldBePresented()
    {
        onView(withId(R.id.finish_message)).check(matches(withText(R.string.finish_message_text)));
    }

    @SmallTest
    public void testFinishFragment_CloseButtonShouldBePresented()
    {
        onView(withId(R.id.finish_close_button)).check(matches(withText(R.string.finish_close_button_text)));
    }

    @SmallTest
    public void testFinishFragment_BackButtonPressed_FragmentShouldBeDismissed()
    {
        pressBack();
        onView(withId(R.id.finish_fragment_layout)).check(doesNotExist());
    }

    private void openFinishFragment()
    {
        Runnable noticeFragmentRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                FinishFragment.newInstance().show(getActivity().getFragmentManager(), ChargeActivity.FINISH_FRAGMENT_TAG);
                getActivity().getFragmentManager().executePendingTransactions();
            }
        };
        getInstrumentation().runOnMainSync(noticeFragmentRunnable);
        getInstrumentation().waitForIdleSync();
    }
}
