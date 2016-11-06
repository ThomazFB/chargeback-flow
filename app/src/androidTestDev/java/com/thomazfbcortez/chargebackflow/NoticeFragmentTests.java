package com.thomazfbcortez.chargebackflow;

import android.support.test.filters.SmallTest;
import android.text.Html;

import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.android.fragment.NoticeFragment;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.model.Notice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETChargebackMessageMockWithAutoblock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETNoticeMessageMock;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
/*
    Tests in this class make calls to the UI,
    make sure your device is active and with the screen awake
    so everything can run in the intended environment

    Also, in order to the Espresso tests work as expected,
    the framework documentation asks for the testing device to be
    with all the animations options disabled at the device Developer Options
*/
public class NoticeFragmentTests extends InjectedActivityTestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        openNoticeFragment();
    }

    @Override
    protected void tearDown() throws Exception
    {
        NoticeFragment noticeFragment = getNoticeFragment();
        if(noticeFragment != null) noticeFragment.dismiss();
        super.tearDown();
    }

    @SmallTest
    public void testNoticeFragment_CancelButtonPressed_FragmentShouldBeDismissed()
    {
        onView(withId(R.id.secondary_action_button)).perform(click());
        onView(withId(R.id.notice_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testNoticeFragment_NoticeDataReceived_TitleShouldBePresented()
    {
        Notice notice = getNoticeFragment().getNotice();
        onView(withId(R.id.notice_title)).check(matches(withText(notice.getTitle())));
    }

    @SmallTest
    public void testNoticeFragment_NoticeDataReceived_ButtonsShouldBePresented()
    {
        Notice notice = getNoticeFragment().getNotice();
        onView(withId(R.id.primary_action_button)).check(matches(withText(notice.getPrimaryAction().getTitle())));
        onView(withId(R.id.secondary_action_button)).check(matches(withText(notice.getSecondaryAction().getTitle())));
    }

    @SmallTest
    public void testNoticeFragment_NoticeDataReceived_DescriptionShouldBeParsedFromHTML()
    {
        Notice notice = getNoticeFragment().getNotice();
        String fromHTMLText = Html.fromHtml(notice.getDescription()).toString();
        onView(withId(R.id.notice_description)).check(matches(withText(fromHTMLText)));
    }

    @SmallTest
    public void testNoticeFragment_GETChargebackMessageError_ShouldDisplayConnectionErrorDialog() throws Exception
    {
        doThrow(Exception.class).when(api).fetchJSONTo(isA(GETChargebackMessage.class));

        onView(withId(R.id.primary_action_button)).perform(click());
        onView(withText(R.string.no_connection_error)).check(matches(isDisplayed()));
    }


    @SmallTest
    public void testNoticeFragment_GETChargebackMessageParsingError_ShouldDisplayAPIErrorDialog() throws Exception
    {
        doReturn(BROKEN_RESPONSE).when(api).fetchJSONTo(isA(GETChargebackMessage.class));

        onView(withId(R.id.primary_action_button)).perform(click());
        onView(withText(R.string.api_data_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testNoticeFragment_GETChargebackMessageError_FragmentShouldBeDismissed() throws Exception
    {
        doThrow(Exception.class).when(api).fetchJSONTo(isA(GETChargebackMessage.class));

        onView(withId(R.id.primary_action_button)).perform(click());
        onView(withId(R.id.notice_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testNoticeFragment_BackButtonPressed_FragmentShouldBeDismissed()
    {
        pressBack();
        onView(withId(R.id.notice_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testNoticeFragmentCreateProgressDialog_ShouldBeCreated() throws Exception
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getNoticeFragment().createProgressDialog();
                assertTrue(getNoticeFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testNoticeFragmentCreateProgressDialog_GETChargebackMessageReceived_ShouldBeDismissed()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getNoticeFragment().createProgressDialog();
                eventBus.post(buildGETChargebackMessageMockWithAutoblock(false));
                assertFalse(getNoticeFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testNoticeFragmentCreateProgressDialog_ErrorEvent_ShouldBeDismissed()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getNoticeFragment().createProgressDialog();
                eventBus.post(new ErrorMessage(R.string.no_connection_error));
                assertFalse(getNoticeFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testNoticeFragment_GETChargebackMessageReceived_ChargebackFragmentShouldBeCreated()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                eventBus.post(buildGETChargebackMessageMockWithAutoblock(false));
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.chargeback_fragment_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testNoticeFragment_PrimaryActionClicked_ApiRequestChargebackShouldBeCalled()
    {
        onView(withId(R.id.primary_action_button)).perform(click());
        verify(api).request(isA(GETChargebackMessage.class));
    }

    private void openNoticeFragment()
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                NoticeFragment.newInstance(buildGETNoticeMessageMock().getResult())
                        .show(getActivity().getFragmentManager(), ChargeActivity.NOTICE_FRAGMENT_TAG);
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}