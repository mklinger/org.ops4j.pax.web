package org.ops4j.pax.web.service.internal.model;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.osgi.service.http.HttpContext;

public class FilterModel
    extends Model
{

    private final Filter m_filter;
    private final String[] m_urlPatterns;
    private final String[] m_servletNames;
    private final Map<String, String> m_initParams;

    public FilterModel( final HttpContext httpContext,
                        final Filter filter,
                        final String[] urlPatterns,
                        final String[] servletNames,
                        final Dictionary initParams )
    {
        super( httpContext );

        if( urlPatterns == null && servletNames == null )
        {
            throw new IllegalArgumentException(
                "Registered filter must have at least one url pattern or servlet mapping"
            );
        }

        m_filter = filter;
        m_urlPatterns = urlPatterns;
        m_servletNames = servletNames;
        m_initParams = convertToMap( initParams );
    }

    public Filter getFilter()
    {
        return m_filter;
    }

    public String[] getUrlPatterns()
    {
        return m_urlPatterns;
    }

    public String[] getServletNames()
    {
        return m_servletNames;
    }

    public Map<String, String> getInitParams()
    {
        return m_initParams;
    }

    private static Map<String, String> convertToMap( final Dictionary dictionary )
    {
        Map<String, String> converted = null;
        if( dictionary != null )
        {
            converted = new HashMap<String, String>();
            Enumeration enumeration = dictionary.keys();
            try
            {
                while( enumeration.hasMoreElements() )
                {
                    String key = (String) enumeration.nextElement();
                    String value = (String) dictionary.get( key );
                    converted.put( key, value );
                }
            }
            catch( ClassCastException e )
            {
                throw new IllegalArgumentException(
                    "Invalid init params for the servlet. The key and value must be Strings."
                );
            }
        }
        return converted;
    }

    @Override
    public String toString()
    {
        return new StringBuilder()
            .append( this.getClass().getSimpleName() )
            .append( "{" )
            .append( "id=" ).append( m_id )
            .append( ",urlPatterns=" ).append( Arrays.toString( m_urlPatterns ) )
            .append( ",servletNames=" ).append( Arrays.toString( m_servletNames ) )
            .append( ",filter=" ).append( m_filter )
            .append( ",httpContext=" ).append( m_httpContext )
            .append( "}" )
            .toString();
    }

}