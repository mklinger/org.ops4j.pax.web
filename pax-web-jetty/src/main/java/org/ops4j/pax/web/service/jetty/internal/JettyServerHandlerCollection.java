/* Copyright 2007 Alin Dreghiciu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.service.jetty.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.ops4j.lang.NullArgumentException;
import org.ops4j.pax.web.service.spi.model.ContextModel;
import org.ops4j.pax.web.service.spi.model.ServerModel;

/**
 * Jetty Handler collection that calls only the handler (=context) that matches the request path after performing the
 * substring based matching of requests path to registered aliases.
 *
 * @author Alin Dreghiciu
 * @since 0.2.3, December 22, 2007
 */
class JettyServerHandlerCollection
    extends HandlerCollection
{

    private final ServerModel m_serverModel;

    JettyServerHandlerCollection( final ServerModel serverModel )
    {
        super( true );
        NullArgumentException.validateNotNull( serverModel, "Service model" );
        m_serverModel = serverModel;
    }

    @Override
    public void handle(
        final String target,
        final Request baseRequest,
        final HttpServletRequest request,
        final HttpServletResponse response )
        throws IOException, ServletException
    {
        if( !isStarted() )
        {
            return;
        }

//        super.handle(target, baseRequest, request, response);

        final ContextModel matched = m_serverModel.matchPathToContext( target );
        if( matched != null )
        {
            final ContextHandler context = ( (JettyServerWrapper) getServer() ).getContext( matched.getHttpContext() );
            try
            {
                context.handle( target, baseRequest, request, response );
            }
            catch( EofException e )
            {
                throw e;
            }
            catch( RuntimeException e )
            {
                throw e;
            }
            catch( Exception e )
            {
                throw new ServletException( e );
            }
        }
    }

}
