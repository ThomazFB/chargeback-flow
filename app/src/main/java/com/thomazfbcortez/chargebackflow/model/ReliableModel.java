package com.thomazfbcortez.chargebackflow.model;

import java.io.Serializable;

public abstract class ReliableModel implements Serializable
{
    public abstract boolean isModelNullable();
}
