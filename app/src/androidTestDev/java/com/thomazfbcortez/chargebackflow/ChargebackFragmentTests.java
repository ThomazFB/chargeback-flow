package com.thomazfbcortez.chargebackflow;

import android.support.test.espresso.Espresso;
import android.support.test.filters.SmallTest;
import android.text.Html;

import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.android.fragment.ChargebackFragment;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTCardBlockMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.model.Chargeback;
import com.thomazfbcortez.chargebackflow.model.ChargebackForm;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.CHARGEBACK_FORM_RESPONSE_AUTOBLOCK_FALSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.COMMENT_INPUT;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.POST_OK_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildChargebackMock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildCommentWithLength;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETChargebackMessageMockWithAutoblock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.withHint;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
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
public class ChargebackFragmentTests extends InjectedActivityTestCase
{

    @Override
    protected void tearDown() throws Exception
    {
        ChargebackFragment chargebackFragment = getChargebackFragment();
        if (chargebackFragment != null) chargebackFragment.dismiss();
        super.tearDown();
    }

    @SmallTest
    public void testChargebackFragment_CancelButtonPressed_FragmentShouldBeDismissed()
    {
        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.cancel_chargeback_button)).perform(click());
        onView(withId(R.id.chargeback_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testChargebackFragment_AutoblockTrue_UIShouldBeUpdated() throws Exception
    {
        doCallRealMethod().when(api).request(isA(POSTCardBlockMessage.class));
        doReturn(POST_OK_RESPONSE).when(api).sendJSONFrom(isA(POSTCardBlockMessage.class));

        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.card_blocked_info)).check(matches(withText(R.string.card_blocked_text)));
        assertTrue(getChargebackFragment().getCardBlockedLockIcon().isActivated());
    }

    @SmallTest
    public void testChargebackFragment_AutoblockFalse_UIShouldNotBeUpdated() throws Exception
    {
        doCallRealMethod().when(api).request(isA(POSTCardBlockMessage.class));
        doReturn(CHARGEBACK_FORM_RESPONSE_AUTOBLOCK_FALSE).when(api).fetchJSONTo(isA(GETChargebackMessage.class));
        doReturn(POST_OK_RESPONSE).when(api).sendJSONFrom(isA(POSTCardBlockMessage.class));

        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.card_blocked_info)).check(matches(withText(R.string.card_unblocked_text)));
        assertFalse(getChargebackFragment().getCardBlockedLockIcon().isActivated());
    }

    @SmallTest
    public void testChargebackFragment_KeyboardHidden_CardOptionsShouldBeVisible()
    {
        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.chargeback_comment)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.card_options_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_KeyboardVisible_CardOptionsShouldBeHidden()
    {
        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.chargeback_comment)).perform(click());
        try
        {
            onView(withId(R.id.card_options_layout)).check(matches(isDisplayed()));
        }
        catch (AssertionFailedError exception)
        {
            return;
        }
        Assert.fail();
    }

    @SmallTest
    public void testChargebackFragment_OnFragmentResumed_CardOptionsShouldBeVisible()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getChargebackFragment().onPause();
                getChargebackFragment().onResume();
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.card_options_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_POSTCardblockMessageError_ShouldDisplayConnectionErrorDialog() throws Exception
    {
        doCallRealMethod().when(api).request(isA(POSTCardBlockMessage.class));
        doThrow(Exception.class).when(api).sendJSONFrom(isA(POSTCardBlockMessage.class));

        openChargebackFragmentWithAutoblock(true);
        onView(withText(R.string.no_connection_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_POSTCardblockMessageParsingError_ShouldDisplayAPIErrorDialog() throws Exception
    {
        doCallRealMethod().when(api).request(isA(POSTCardBlockMessage.class));
        doReturn(BROKEN_RESPONSE).when(api).sendJSONFrom(isA(POSTCardBlockMessage.class));

        openChargebackFragmentWithAutoblock(true);
        onView(withText(R.string.api_data_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_POSTChargebackMessageError_ShouldDisplayConnectionErrorDialog() throws Exception
    {
        doThrow(Exception.class).when(api).sendJSONFrom(isA(POSTChargebackMessage.class));

        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(COMMENT_INPUT));
        onView(withId(R.id.submit_chargeback_button)).perform(click());
        onView(withText(R.string.no_connection_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_POSTChargebackMessageParsingError_ShouldDisplayAPIErrorDialog() throws Exception
    {
        doReturn(BROKEN_RESPONSE).when(api).sendJSONFrom(isA(POSTChargebackMessage.class));

        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(COMMENT_INPUT));
        onView(withId(R.id.submit_chargeback_button)).perform(click());
        onView(withText(R.string.api_data_error)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_POSTChargebackMessageError_FragmentShouldBeDismissed() throws Exception
    {
        doThrow(Exception.class).when(api).sendJSONFrom(isA(POSTChargebackMessage.class));

        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(COMMENT_INPUT));
        onView(withId(R.id.submit_chargeback_button)).perform(click());
        onView(withId(R.id.chargeback_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testChargebackFragment_BackButtonWhileEditingText_FragmentShouldNotBeDismissed()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        pressBack();
        onView(withId(R.id.chargeback_fragment_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_ChargebackButtonShouldBeEnabled()
    {
        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(buildCommentWithLength(ChargebackFragment.MINIMUM_COMMENT_LENGTH)));
        onView(withId(R.id.submit_chargeback_button)).check(matches(isEnabled()));
    }

    @SmallTest
    public void testChargebackFragment_ChargebackButtonShouldBeDisabled()
    {
        openChargebackFragmentWithAutoblock(true);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(buildCommentWithLength(ChargebackFragment.MINIMUM_COMMENT_LENGTH - 1)));
        try
        {
            onView(withId(R.id.submit_chargeback_button)).check(matches(isEnabled()));
        }
        catch (AssertionFailedError exception)
        {
            return;
        }
        Assert.fail();
    }

    @SmallTest
    public void testChargebackFragment_BackButtonPressed_FragmentShouldBeDismissed()
    {
        openChargebackFragmentWithAutoblock(true);
        pressBack();
        onView(withId(R.id.chargeback_fragment_layout)).check(doesNotExist());
    }

    @SmallTest
    public void testChargebackFragmentCreateProgressDialog_ShouldBeCreated() throws Exception
    {
        openChargebackFragmentWithAutoblock(false);
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getChargebackFragment().createProgressDialog();
                assertTrue(getChargebackFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargebackFragmentCreateProgressDialog_POSTChargebackMessageReceived_ShouldBeDismissed()
    {
        openChargebackFragmentWithAutoblock(false);
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getChargebackFragment().createProgressDialog();
                eventBus.post(new POSTChargebackMessage("", buildChargebackMock()));
                assertFalse(getChargebackFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargebackFragmentCreateProgressDialog_ErrorEvent_ShouldBeDismissed()
    {
        openChargebackFragmentWithAutoblock(false);
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                getChargebackFragment().createProgressDialog();
                eventBus.post(new ErrorMessage(R.string.no_connection_error));
                assertFalse(getChargebackFragment().getProgressDialog().isShowing());
            }
        });
    }

    @SmallTest
    public void testChargebackFragment_POSTChargebackMessageReturned_FinishFragmentShouldBeCreated()
    {
        openChargebackFragmentWithAutoblock(true);
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                eventBus.post(new POSTChargebackMessage("", buildChargebackMock()));
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.finish_fragment_layout)).check(matches(isDisplayed()));
    }

    @SmallTest
    public void testChargebackFragment_SubmitChargebackClicked_ApiRequestPOSTChargebackShouldBeCalled()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(COMMENT_INPUT));
        onView(withId(R.id.submit_chargeback_button)).perform(click());
        verify(api).request(isA(POSTChargebackMessage.class));
    }

    @SmallTest
    public void testChargebackFragment_AutoblockTrue_ApiRequestPOSTCardblockShouldBeCalled()
    {
        openChargebackFragmentWithAutoblock(true);
        verify(api).request(isA(POSTCardBlockMessage.class));
    }

    @SmallTest
    public void testChargebackFragment_SwitchesClicked_ShouldBuildChargebackWithSwitchActivated()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_first_reason_switch)).perform(click());
        onView(withId(R.id.chargeback_second_reason_switch)).perform(click());
        Chargeback chargeback = getChargebackFragment().buildChargeback();
        assertTrue(Boolean.valueOf(chargeback.getReasonDetails().get(0).get("response").toString()));
        assertTrue(Boolean.valueOf(chargeback.getReasonDetails().get(1).get("response").toString()));
    }

    @SmallTest
    public void testChargebackFragment_CommentEdited_ShouldBuildChargebackCorrectly()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.chargeback_comment)).perform(click());
        onView(withId(R.id.chargeback_comment)).perform(typeText(COMMENT_INPUT));
        Chargeback chargeback = getChargebackFragment().buildChargeback();
        assertEquals(chargeback.getComment(), COMMENT_INPUT);
    }

    @SmallTest
    public void testChargebackFragment_ChargebackFormDataReceived_TitleShouldBePresented()
    {
        openChargebackFragmentWithAutoblock(true);
        ChargebackForm chargebackForm = getChargebackFragment().getChargebackForm();
        onView(withId(R.id.chargeback_title)).check(matches(withText(chargebackForm.getTitle())));
    }

    @SmallTest
    public void testChargebackFragment_ChargebackFormDataReceived_LabelsShouldBePresented()
    {
        openChargebackFragmentWithAutoblock(false);
        ChargebackForm chargebackForm = getChargebackFragment().getChargebackForm();
        onView(withId(R.id.chargeback_first_reason_info)).check(matches(withText(chargebackForm.getReasonDetails().get(0).get("title"))));
        onView(withId(R.id.chargeback_second_reason_info)).check(matches(withText(chargebackForm.getReasonDetails().get(1).get("title"))));
    }

    @SmallTest
    public void testChargebackFragment_ChargebackFormDataReceived_HintShouldBePresented()
    {
        openChargebackFragmentWithAutoblock(false);
        ChargebackForm chargebackForm = getChargebackFragment().getChargebackForm();
        onView(withId(R.id.chargeback_comment)).check(matches(withHint(Html.fromHtml(chargebackForm.getHint()).toString())));
    }

    @SmallTest
    public void testChargebackFragment_ChargebackFormDataReceived_ButtonsShouldBePresented()
    {
        openChargebackFragmentWithAutoblock(false);
        onView(withId(R.id.cancel_chargeback_button)).check(matches(withText(R.string.cancel_chargeback_button_text)));
        onView(withId(R.id.submit_chargeback_button)).check(matches(withText(R.string.submit_chargeback_button_text)));
    }

    private void openChargebackFragmentWithAutoblock(final boolean autoblock)
    {
        getInstrumentation().runOnMainSync(new Runnable()
        {
            @Override
            public void run()
            {
                ChargebackFragment.newInstance(buildGETChargebackMessageMockWithAutoblock(autoblock).getResult())
                        .show(getActivity().getFragmentManager(), ChargeActivity.CHARGEBACK_FRAGMENT_TAG);
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}