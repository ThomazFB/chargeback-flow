package com.thomazfbcortez.chargebackflow;

import android.view.View;
import android.widget.EditText;

import com.thomazfbcortez.chargebackflow.android.fragment.ChargebackFragment;
import com.thomazfbcortez.chargebackflow.api.API;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;
import com.thomazfbcortez.chargebackflow.api.message.Message;
import com.thomazfbcortez.chargebackflow.api.message.POSTCardBlockMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.model.Chargeback;

import org.greenrobot.eventbus.EventBus;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class TestEnvironmentProvider
{
    public static String BROKEN_RESPONSE = "{\"links\":{\"notice\":\"";
    public static String EMPTY_RESPONSE = "{}";
    public static String ENTRY_ENDPOINT_RESPONSE = "{\"links\":{\"notice\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/notice\"}}}";
    public static String ALIKE_ENTRY_ENDPOINT_RESPONSE = "{\"links\":{\"hue\":{\"wat\":\"https://nu-mobile-hiring.herokuapp.com/notice\"}}}";
    public static String NOTICE_RESPONSE = "{\"title\":\"Antes de continuar\",\"description\":\"<p>Estamos com você nesta! Certifique-se dos pontos abaixo, são muito importantes:<br/><strong>• Você pode <font color=\\\"#6e2b77\\\">procurar o nome do estabelecimento no Google</font>. Diversas vezes encontramos informações valiosas por lá e elas podem te ajudar neste processo.</strong><br/><strong>• Caso você reconheça a compra, é muito importante pra nós que entre em contato com o estabelecimento e certifique-se que a situação já não foi resolvida.</strong></p>\",\"primary_action\":{\"title\":\"Continuar\",\"action\":\"continue\"},\"secondary_action\":{\"title\":\"Fechar\",\"action\":\"cancel\"},\"links\":{\"chargeback\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/chargeback\"}}}";
    public static String ALIKE_NOTICE_RESPONSE = "{\"links\":{\"notice\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/notice\"}}}";
    public static String CHARGEBACK_FORM_RESPONSE = "{\"comment_hint\":\"Nos conte <strong>em detalhes</strong> o que aconteceu com a sua compra em Transaction...\",\"id\":\"fraud\",\"title\":\"Não reconheço esta compra\",\"autoblock\":true,\"reason_details\":[{\"id\":\"merchant_recognized\",\"title\":\"Reconhece o estabelecimento?\"},{\"id\":\"card_in_possession\",\"title\":\"Está com o cartão em mãos?\"}],\"links\":{\"block_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_block\"},\"unblock_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_unblock\"},\"self\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/chargeback\"}}}";
    public static String CHARGEBACK_FORM_RESPONSE_AUTOBLOCK_FALSE = "{\"comment_hint\":\"Nos conte <strong>em detalhes</strong> o que aconteceu com a sua compra em Transaction...\",\"id\":\"fraud\",\"title\":\"Não reconheço esta compra\",\"autoblock\":false,\"reason_details\":[{\"id\":\"merchant_recognized\",\"title\":\"Reconhece o estabelecimento?\"},{\"id\":\"card_in_possession\",\"title\":\"Está com o cartão em mãos?\"}],\"links\":{\"block_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_block\"},\"unblock_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_unblock\"},\"self\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/chargeback\"}}}";
    public static String ALIKE_CHARGEBACK_FORM_RESPONSE = "{\"comment_hunt\":\"Nos conte <strong>em detalhes</strong> o que aconteceu com a sua compra em Transaction...\",\"id\":\"fraud\",\"title\":\"NÃ£o reconheÃ§o esta compra\",\"wut\":true,\"reason_details\":[{\"id\":\"merchant_recognized\",\"title\":\"Reconhece o estabelecimento?\"},{\"id\":\"card_in_possession\",\"title\":\"EstÃ¡ com o cartÃ£o em mÃ£os?\"}],\"links\":{\"block_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_block\"},\"unblock_card\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/card_unblock\"},\"self\":{\"href\":\"https://nu-mobile-hiring.herokuapp.com/chargeback\"}}}";
    public static String CHARGEBACK_SUBMISSION_JSON = "{\"comment\":\"One does not simply mocks a comment!\",\"reason_details\":[{\"response\":true,\"id\":\"Mock\"},{\"response\":true,\"id\":\"Mock\"}]}";
    public static String POST_OK_RESPONSE = "{\"status\":\"Ok\"}";
    public static String POST_ERROR_RESPONSE = "{\"status\":\"Error\"}";
    public static String COMMENT_INPUT = "testing testing testing";

    public static GETEntryEndpointMessage buildGETEntryEndpointMessageMock()
    {
        GETEntryEndpointMessage getEntryEndpointMessage = new GETEntryEndpointMessage("");
        getEntryEndpointMessage.setJsonResponse(ENTRY_ENDPOINT_RESPONSE);
        getEntryEndpointMessage.parse();
        return getEntryEndpointMessage;
    }

    public static GETNoticeMessage buildGETNoticeMessageMock()
    {
        GETNoticeMessage getNoticeMessage = new GETNoticeMessage(buildGETEntryEndpointMessageMock().getResult());
        getNoticeMessage.setJsonResponse(NOTICE_RESPONSE);
        getNoticeMessage.parse();
        return getNoticeMessage;
    }

    public static GETChargebackMessage buildGETChargebackMessageMockWithAutoblock(boolean autoblock)
    {
        GETChargebackMessage getChargebackMessage = new GETChargebackMessage("");
        if (autoblock)
            getChargebackMessage.setJsonResponse(CHARGEBACK_FORM_RESPONSE);
        else
            getChargebackMessage.setJsonResponse(CHARGEBACK_FORM_RESPONSE_AUTOBLOCK_FALSE);
        getChargebackMessage.parse();
        return getChargebackMessage;
    }

    public static Chargeback buildChargebackMock()
    {
        Chargeback chargeback = new Chargeback();
        List<HashMap<String, Object>> reasonDetails = new ArrayList<>();
        reasonDetails.add(new HashMap<String, Object>());
        reasonDetails.add(new HashMap<String, Object>());
        reasonDetails.get(0).put("id", "Mock");
        reasonDetails.get(1).put("id", "Mock");
        reasonDetails.get(0).put("response", true);
        reasonDetails.get(1).put("response", true);
        chargeback.setComment("One does not simply mocks a comment!");
        chargeback.setReasonDetails(reasonDetails);
        return chargeback;
    }

    public static String buildCommentWithLength(int length)
    {
        String minimumComment = "";
        for(int i = 0; i < length; i++)
        {
            minimumComment+= "a";
        }
        return minimumComment;
    }
    public static void setDefaultAPIMockBehaviour(API api) throws Exception
    {
        Mockito.reset(api);
        doCallRealMethod().when(api).request(any(Message.class));
        doReturn(ENTRY_ENDPOINT_RESPONSE).when(api).fetchJSONTo(isA(GETEntryEndpointMessage.class));
        doReturn(NOTICE_RESPONSE).when(api).fetchJSONTo(isA(GETNoticeMessage.class));
        doReturn(CHARGEBACK_FORM_RESPONSE).when(api).fetchJSONTo(isA(GETChargebackMessage.class));
        doReturn(POST_OK_RESPONSE).when(api).sendJSONFrom(isA(POSTCardBlockMessage.class));
        doReturn(POST_OK_RESPONSE).when(api).sendJSONFrom(isA(POSTChargebackMessage.class));
        doNothing().when(api).request(isA(POSTCardBlockMessage.class));
    }

    public static void setDefaultEventBusMockBehaviour(EventBus eventBus) throws Exception
    {
        Mockito.reset(eventBus);
        doNothing().when(eventBus).post(any(Object.class));
        doNothing().when(eventBus).register(any(Object.class));
        doNothing().when(eventBus).unregister(any(Object.class));
    }

    public static Matcher<View> withHint(final String expectedHint)
    {
        return new TypeSafeMatcher<View>()
        {

            @Override
            public boolean matchesSafely(View view)
            {
                if (!(view instanceof EditText))
                {
                    return false;
                }

                String hint = ((EditText) view).getHint().toString();

                return expectedHint.equals(hint);
            }

            @Override
            public void describeTo(Description description)
            {
            }
        };
    }
}
