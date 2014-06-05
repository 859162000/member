package com.wanda.ccs.member.ap2in;

import java.io.UnsupportedEncodingException;

public class WinletUserProfile extends PropertyObject
    implements IUserProfile
{

    public WinletUserProfile()
    {
    }

    public WinletUserProfile(IPropertyObject ref)
    {
        super(ref);
    }

    public WinletUserProfile(String prop)
        throws UnsupportedEncodingException
    {
        super(prop);
    }

    public String getProperty(String property)
    {
        if(property == null)
            return null;
        if(property.equalsIgnoreCase("isanonymous"))
            return isAnonymous() ? "T" : "F";
        else
            return super.getProperty(property);
    }

    public boolean hasProperty(String property)
    {
        if(property == null)
            return false;
        if(property.equalsIgnoreCase("isanonymous"))
            return true;
        else
            return super.hasProperty(property);
    }

    public boolean isAnonymous()
    {
        return m_htProperties.get("id") == null;
    }

    public void setId(String id)
    {
        setProperty("id", id);
    }

    public void setName(String name)
    {
        setProperty("name", name);
    }

    public String getId()
    {
        return getProperty("id");
    }

    public String getName()
    {
        return getProperty("name");
    }

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_EMAIL = "email";
}


