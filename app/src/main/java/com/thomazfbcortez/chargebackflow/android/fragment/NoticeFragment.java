package com.thomazfbcortez.chargebackflow.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.api.API;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.model.Notice;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;

public class NoticeFragment extends BaseDialogFragment
{
    @Bind(R.id.notice_title) TextView noticeTitle;
    @Bind(R.id.notice_description) TextView noticeDescription;
    @Bind(R.id.primary_action_button) TextView noticePrimaryAction;
    @Bind(R.id.secondary_action_button) TextView noticeSecondaryAction;

    public static NoticeFragment newInstance(Notice notice)
    {
        NoticeFragment noticeFragment = new NoticeFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIALOG_INFO_KEY, notice);
        noticeFragment.setArguments(arguments);
        return noticeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialogInfo = (Notice) getArguments().getSerializable(DIALOG_INFO_KEY);
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    protected void initializeDialogUserInterface()
    {
        Notice noticeInfo = (Notice) dialogInfo;
        noticeTitle.setText(noticeInfo.getTitle());
        noticeDescription.setText(Html.fromHtml(noticeInfo.getDescription()));
        noticePrimaryAction.setText(noticeInfo.getPrimaryAction().getTitle());
        noticeSecondaryAction.setText(noticeInfo.getSecondaryAction().getTitle());
        noticePrimaryAction.setOnClickListener(dialogButtonClickListener());
        noticeSecondaryAction.setOnClickListener(dialogButtonClickListener());
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
                    case R.id.primary_action_button:
                        noticeSecondaryAction.setEnabled(false);
                        requestChargebackInfo();
                        createProgressDialog();
                        break;

                    case R.id.secondary_action_button:
                        noticePrimaryAction.setEnabled(false);
                        dismiss();
                        break;
                }
            }
        };
    }

    private void requestChargebackInfo()
    {
        Notice noticeInfo = (Notice) dialogInfo;
        String url = noticeInfo.getLinks().getChargebackEndpoint().getHref();
        GETChargebackMessage getChargebackMessage = new GETChargebackMessage(url);
        api.request(getChargebackMessage);
    }

    @Subscribe
    public void onChargebackInfoReceivedEvent(GETChargebackMessage message)
    {
        if (progressDialog != null) progressDialog.dismiss();
        this.dismiss();
        ChargebackFragment.newInstance(message.getResult()).show(getFragmentManager(), ChargeActivity.CHARGEBACK_FRAGMENT_TAG);
    }

    public Notice getNotice()
    {
        return (Notice) dialogInfo;
    }
}