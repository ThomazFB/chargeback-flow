package com.thomazfbcortez.chargebackflow.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.api.message.POSTCardBlockMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.model.Chargeback;
import com.thomazfbcortez.chargebackflow.model.ChargebackForm;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

public class ChargebackFragment extends BaseDialogFragment
{
    @Bind(R.id.chargeback_title) TextView chargebackTitle;
    @Bind(R.id.card_blocked_lock_icon) ImageView cardBlockedLockIcon;
    @Bind(R.id.card_blocked_info) TextView cardBlockedInfo;
    @Bind(R.id.chargeback_first_reason_info) TextView firstReasonInfo;
    @Bind(R.id.chargeback_first_reason_switch) SwitchCompat firstReasonSwitch;
    @Bind(R.id.chargeback_second_reason_info) TextView secondReasonInfo;
    @Bind(R.id.chargeback_second_reason_switch) SwitchCompat secondReasonSwitch;
    @Bind(R.id.chargeback_comment) EditText chargebackCommentEditText;
    @Bind(R.id.card_options_layout) LinearLayout cardOptionsLayout;
    @Bind(R.id.cancel_chargeback_button) TextView cancelButton;
    @Bind(R.id.submit_chargeback_button) TextView chargebackButton;
    public static int MINIMUM_COMMENT_LENGTH = 4;
    private int previousHeightDifference = 0;

    public static ChargebackFragment newInstance(ChargebackForm chargebackForm)
    {
        ChargebackFragment chargebackFragment = new ChargebackFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIALOG_INFO_KEY, chargebackForm);
        chargebackFragment.setArguments(arguments);
        return chargebackFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        dialogInfo = (ChargebackForm) getArguments().getSerializable(DIALOG_INFO_KEY);
        setKeyboardVisibilityEvent();
        return inflater.inflate(R.layout.fragment_chargeback, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        cardOptionsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initializeDialogUserInterface()
    {
        ChargebackForm chargebackForm = getChargebackForm();
        chargebackTitle.setText(chargebackForm.getTitle());
        cardBlockedLockIcon.setActivated(false);
        cardBlockedInfo.setText(R.string.card_unblocked_text);
        firstReasonInfo.setText(chargebackForm.getReasonDetails().get(0).get("title"));
        secondReasonInfo.setText(chargebackForm.getReasonDetails().get(1).get("title"));
        chargebackCommentEditText.setHint(Html.fromHtml(chargebackForm.getHint()));
        chargebackCommentEditText.addTextChangedListener(editTextTextWatcher());
        cancelButton.setOnClickListener(dialogButtonClickListener());
        chargebackButton.setOnClickListener(dialogButtonClickListener());
        checkAutoblock();
    }

    private void checkAutoblock()
    {
        if (getChargebackForm().isAutoblock())
        {
            sendCardBlockAction(getChargebackForm().getLinks().getBlockCardEndpoint().getHref());
        }
    }

    private View.OnClickListener dialogButtonClickListener()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.cancel_chargeback_button:
                        chargebackButton.setEnabled(false);
                        chargebackCommentEditText.clearFocus();
                        dismiss();
                        break;

                    case R.id.submit_chargeback_button:
                        cancelButton.setEnabled(false);
                        createProgressDialog();
                        sendChargeback();
                        break;
                }
            }
        };
    }

    private void sendCardBlockAction(String url)
    {
        POSTCardBlockMessage postCardBlockMessage = new POSTCardBlockMessage(url);
        api.request(postCardBlockMessage);
    }

    private void sendChargeback()
    {
        String url = getChargebackForm().getLinks().getSelfEndpoint().getHref();
        POSTChargebackMessage postChargebackMessage = new POSTChargebackMessage(url, buildChargeback());
        api.request(postChargebackMessage);
    }

    @Subscribe
    public void onCardBlockedEvent(POSTCardBlockMessage message)
    {
        cardBlockedLockIcon.setActivated(true);
        cardBlockedInfo.setText(R.string.card_blocked_text);
    }

    @Subscribe
    public void onChargebackReturnEvent(POSTChargebackMessage message)
    {
        if (progressDialog != null) progressDialog.dismiss();
        FinishFragment.newInstance().show(getFragmentManager(), ChargeActivity.FINISH_FRAGMENT_TAG);
        this.dismiss();
    }

    private void setKeyboardVisibilityEvent()
    {
        final View activityRootView = getActivity().findViewById(R.id.charge_activity_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                int heightDifference = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDifference > previousHeightDifference) //keyboard is showing
                {
                    cardOptionsLayout.setVisibility(View.GONE);
                }
                else //keyboard is hidden
                {
                    cardOptionsLayout.setVisibility(View.VISIBLE);
                }
                previousHeightDifference = heightDifference;
            }
        });
    }

    public Chargeback buildChargeback()
    {
        Chargeback chargeback = new Chargeback();
        List<HashMap<String, Object>> reasonDetails = new ArrayList<>();
        for (int i = 0; i < 2; i++) reasonDetails.add(i, buildReasonHashMap(i));
        reasonDetails.get(0).put("response", firstReasonSwitch.isChecked());
        reasonDetails.get(1).put("response", secondReasonSwitch.isChecked());
        chargeback.setReasonDetails(reasonDetails);
        chargeback.setComment(chargebackCommentEditText.getText().toString());
        return chargeback;
    }

    private HashMap<String, Object> buildReasonHashMap(int position)
    {
        HashMap<String, Object> reason = new HashMap<>();
        reason.put("id", getChargebackForm().getReasonDetails().get(position).get("id"));
        return reason;
    }

    private TextWatcher editTextTextWatcher()
    {
        return new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable inputText)
            {
                if (inputText.length() >= MINIMUM_COMMENT_LENGTH)
                {
                    chargebackButton.setEnabled(true);
                }
                else
                {
                    chargebackButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
        };
    }

    public ChargebackForm getChargebackForm()
    {
        return (ChargebackForm) dialogInfo;
    }

    public ImageView getCardBlockedLockIcon()
    {
        return cardBlockedLockIcon;
    }

    public TextView getCardBlockedInfo()
    {
        return cardBlockedInfo;
    }

}