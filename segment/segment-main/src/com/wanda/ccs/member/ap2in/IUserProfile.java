package com.wanda.ccs.member.ap2in;

public interface IUserProfile
extends IPropertyObject
{

public abstract boolean isAnonymous();

public static final String PROPERTY_ISANONYMOUS = "isanonymous";
public static final String PROPERTY_ID = "id";
public static final String KEEP_TIME = "keep";
}