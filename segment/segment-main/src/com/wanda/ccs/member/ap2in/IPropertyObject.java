package com.wanda.ccs.member.ap2in;

import java.util.Enumeration;

public interface IPropertyObject
{

    public abstract Enumeration<String> getPublicPropertyNames();

    public abstract String getProperty(String s);

    public abstract boolean hasProperty(String s);
}

