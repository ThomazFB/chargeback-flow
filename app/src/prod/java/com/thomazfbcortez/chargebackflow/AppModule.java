package com.thomazfbcortez.chargebackflow;

import com.thomazfbcortez.chargebackflow.api.API;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule
{
    @Provides
    @Singleton
    public API provideAPI()
    {
        return new API();
    }

    @Provides
    public EventBus provideEventBus()
    {
        return EventBus.getDefault();
    }
}
