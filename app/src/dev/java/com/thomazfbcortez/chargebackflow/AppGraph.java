package com.thomazfbcortez.chargebackflow;

import com.thomazfbcortez.chargebackflow.android.activity.ChargeActivity;
import com.thomazfbcortez.chargebackflow.android.fragment.BaseDialogFragment;
import com.thomazfbcortez.chargebackflow.api.NetworkAsyncTask;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppGraph
{
    void inject(ChargeActivity activity);

    void inject(BaseDialogFragment fragment);

    void inject(NetworkAsyncTask asyncTask);

    void inject(InjectedActivityTestCase test);

    void inject(InjectedInstrumentationTestCase test);

    final class Initializer
    {
        public static AppGraph init(boolean injectMock)
        {
            return DaggerAppGraph.builder()
                    .appModule(new AppModule(injectMock))
                    .build();
        }
    }
}
