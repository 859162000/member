package com.wanda.ccs.member.ap2in;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


public class PropertyObject
{

    public PropertyObject()
    {
        m_htProperties = new Hashtable<String, String>();
    }

    public PropertyObject(IPropertyObject ref)
    {
        m_htProperties = new Hashtable<String, String>();
        if(ref == null)
            return;
        for(Enumeration<String> enm = ref.getPublicPropertyNames(); enm.hasMoreElements();)
        {
            String name = enm.nextElement();
            if(name != null)
            {
                String value = ref.getProperty(name);
                if(value != null)
                    m_htProperties.put(name, value);
            }
        }

    }

    public PropertyObject(String prop)
        throws UnsupportedEncodingException
    {
        m_htProperties = new Hashtable<String, String>();
        if(prop == null)
            return;
        for(StringTokenizer st = new StringTokenizer(prop, ";"); st.hasMoreElements();)
        {
            String str = st.nextToken();
            int i = str.indexOf(":");
            if(i > 0)
                m_htProperties.put(URLDecoder.decode(str.substring(0, i), "UTF-8"), URLDecoder.decode(str.substring(i + 1), "UTF-8"));
        }

    }

    public Enumeration<String> getPublicPropertyNames()
    {
        return m_htProperties.keys();
    }

    public String getProperty(String property)
    {
        if(property == null)
            return null;
        else
            return (String)m_htProperties.get(property);
    }

    public String getProperty(String property, String def)
    {
        if(property == null)
            return def;
        String str = (String)m_htProperties.get(property);
        if(str == null)
            return def;
        else
            return str;
    }

    public int getProperty(String paramName, int defaultValue)
    {
        try
        {
            return Integer.parseInt(getProperty(paramName));
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }

    public long getProperty(String paramName, long defaultValue)
    {
        try
        {
            return Long.parseLong(getProperty(paramName));
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }

    public float getProperty(String paramName, float defaultValue)
    {
        try
        {
            return Float.parseFloat(getProperty(paramName));
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }

    public double getProperty(String paramName, double defaultValue)
    {
        try
        {
            return Double.parseDouble(getProperty(paramName));
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }

    public boolean hasProperty(String property)
    {
        if(property == null)
            return false;
        else
            return m_htProperties.containsKey(property);
    }

    public void setProperty(String name, String property)
    {
        if(property == null)
            m_htProperties.remove(name);
        else
            m_htProperties.put(name, property);
    }

    public String serialize()
        throws UnsupportedEncodingException
    {
        StringBuffer sb = new StringBuffer();
        for(Enumeration<String> enm = getPublicPropertyNames(); enm.hasMoreElements(); sb.append(";"))
        {
            String name = enm.nextElement();
            sb.append(URLEncoder.encode(name, "UTF-8"));
            sb.append(":");
            sb.append(URLEncoder.encode(getProperty(name), "UTF-8"));
        }

        return sb.toString();
    }

    public boolean equals(PropertyObject po)
    {
        if(m_htProperties.size() != po.m_htProperties.size())
            return false;
        for(Iterator<String> iterator = m_htProperties.keySet().iterator(); iterator.hasNext();)
        {
            String key = iterator.next();
            if(!((String)m_htProperties.get(key)).equals(po.m_htProperties.get(key)))
                return false;
        }

        return true;
    }

    public Hashtable<String, String> m_htProperties;
}

