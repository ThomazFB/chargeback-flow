package com.thomazfbcortez.chargebackflow;

import com.thomazfbcortez.chargebackflow.api.API;

import org.greenrobot.eventbus.EventBus;
import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule
{
    private final boolean mockMode;

    public AppModule(boolean injectMock)
    {
        mockMode = injectMock;
    }

    @Provides
    @Singleton
    public API provideAPI()
    {
        return Mockito.mock(API.class);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus()
    {
        if(mockMode)
            return Mockito.mock(EventBus.class);
        else
            return EventBus.getDefault();
    }
}
