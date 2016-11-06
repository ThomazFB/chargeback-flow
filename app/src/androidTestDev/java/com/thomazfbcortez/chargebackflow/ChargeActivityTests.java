package com.thomazfbcortez.chargebackflow;

import android.support.test.filters.SmallTest;

import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETEntryEndpointMessageMock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETNoticeMessageMock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

/*
    Tests in this class make calls to the UI,
    make sure your device is active and with the screen awake
    so everything can run in the intended environment

    Also, in order to the Espresso tests work as expected,
    the framework documentation asks for the testing device to be
    with all the animations options disabled at the device Developer Options
*/
public class ChargeActivityTests extends InjectedActivityTestCase
{
    @SmallTest
    public void testChargeActivity_StartChargebackButtonTextShouldBePresented()
    {
        onView(withId(R.id.start_chargeback_button)).check(matches(withText(R.string.charge_activity_start_chargeback_button_text)));
    }

    @SmallTest
    public void testChargeActivity_GETNoticeMessageConnectionError_ShouldDisplayConnectionErrorDialog() throws Exception
    {
        doThrow(Exception.class).when(api).fetchJSONTo(isA(GETNoticeMessage.class));

        onView(withId(R.id.start_chargeback_button)).perform(click());
        onView(withText(R.string.no_connection_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargeActivity_GETNoticeMessageParsingError_ShouldDisplayAPIErrorDialog() throws Exception
    {
        doReturn(BROKEN_RESPONSE).when(api).fetchJSONTo(isA(GETNoticeMessage.class));

        onView(withId(R.id.start_chargeback_button)).perform(click());
        onView(withText(R.string.api_data_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargeActivity_GETEntryEndpointMessageError_ShouldDisplayConnectionErrorDialog() throws Exception
    {
        doThrow(Exception.class).when(api).fetchJSONTo(isA(GETEntryEndpointMessage.class));

        onView(withId(R.id.start_chargeback_button)).perform(click());
        onView(withText(R.string.no_connection_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargeActivity_GETEntryEndpointMessageParsingError_ShouldDisplayAPIErrorDialog() throws Exception
    {
        doReturn(BROKEN_RESPONSE).when(api).fetchJSONTo(isA(GETEntryEndpointMessage.class));

        onView(withId(R.id.start_chargeback_button)).perform(click());
        onView(withText(R.string.api_data_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargeActivityCreateProgressDialog_ShouldBeCreated() throws Exception
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().createProgressDialog();
                assertTrue(getActivity().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargeActivityCreateProgressDialog_GETEntryEndpointMessageReceived_ShouldBeVisible()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().createProgressDialog();
                eventBus.post(buildGETEntryEndpointMessageMock());
                assertTrue(getActivity().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargeActivityCreateProgressDialog_GETNoticeMessageReceived_ShouldBeDismissed()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().createProgressDialog();
                eventBus.post(buildGETNoticeMessageMock());
                assertFalse(getActivity().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testCreateProgressDialog_ErrorEvent_ShouldBeDismissed()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().createProgressDialog();
                eventBus.post(new ErrorMessage(R.string.no_connection_error));
                assertFalse(getActivity().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargeActivity_GETNoticeMessageReceived_NoticeFragmentShouldBeCreated()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                eventBus.post(buildGETNoticeMessageMock());
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.notice_fragment_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargeActivity_StartChargebackClicked_ApiRequestEndpointShouldBeCalled()
    {
        onView(withId(R.id.start_chargeback_button)).perform(click());
        verify(api).request(isA(GETEntryEndpointMessage.class));
    }

    @SmallTest
    public void testChargeActivity_StartChargebackClicked_ApiRequestNoticeShouldBeCalled()
    {
        onView(withId(R.id.start_chargeback_button)).perform(click());
        verify(api).request(isA(GETNoticeMessage.class));
    }
}