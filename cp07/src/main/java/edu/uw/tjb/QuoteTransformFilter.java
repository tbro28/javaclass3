package edu.uw.tjb;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


/**
 * Servlet Filter to transform an XML response from the QuoteServlet into
 * other formats (json, plain, html). Also accepts json encoded request bodies,
 * for illustative purposes.
 */
public class QuoteTransformFilter implements Filter {


    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service.
     *
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     *
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws a ServletException
     * <li>Does not return within a time period defined by the web container
     * </ol>
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}


    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time a request/response pair is passed through the
     * chain due to a app request for a resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass
     * on the request and response to the next entity in the chain.
     *
     * <p>A typical implementation of this method would follow the following
     * pattern:
     * <ol>
     * <li>Examine the request
     * <li>Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering
     * <li>Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering
     * <li>
     * <ul>
     * <li><strong>Either</strong> invoke the next entity in the chain
     * using the FilterChain object
     * (<code>chain.doFilter()</code>),
     * <li><strong>or</strong> not pass on the request/response pair to
     * the next entity in the filter chain to
     * block the request processing
     * </ul>
     * <li>Directly set headers on the response after invocation of the
     * next entity in the filter chain.
     * </ol>
     *
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String outputType = request.getParameter("rstype");

        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse)response);
        chain.doFilter(request, wrapper);

        StringReader xmlReader = new StringReader(wrapper.toString());
        StringBuffer xmlDoc = new StringBuffer();

        int cont = xmlReader.read();
        while(cont != -1) {
            xmlDoc.append((char)cont);
            cont = xmlReader.read();
        }
        xmlReader.close();

        StringWriter output = new StringWriter();

        String symbol = xmlDoc.substring((xmlDoc.lastIndexOf("<symbol>")+8),(xmlDoc.lastIndexOf("</symbol>")));
        String price = xmlDoc.substring((xmlDoc.lastIndexOf("<price>")+7),(xmlDoc.lastIndexOf("</price>")));

        /*
            application/xml if not readable from casual users (RFC 3023, section 3)
            text/xml if readable from casual users (RFC 3023, section 3)
            application/json
            text/plain
            text/html
        */
        switch (outputType) {
            case "xml":
                output.append(xmlDoc);
                break;

            case "html":
                response.setContentType("text/html");
                String htmlDoc = "<!DOCTYPE html><html><body>" + xmlDoc.substring(xmlDoc.indexOf("?>")+2, xmlDoc.length()) + "</body></html>";
                output.append(htmlDoc);
                break;

            case "json":
                response.setContentType("application/json");
                output.append( "{\"symbol\":\"" + symbol + "\", \"price\":\""+ price +"\"}");
                break;

            case "plain":
                response.setContentType("text/plain");
                output.append("Ticker symbol "
                        + symbol
                        + " has a price of: "
                        + price
                );
                break;
        }

        String respStr = output.toString();
        response.setContentLength(respStr.length());
        response.getWriter().write(output.toString());
    }


    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     *
     * <p>This method is only called once all threads within the filter's
     * doFilter method have exited or after a timeout period has passed.
     * After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     *
     * <p>This method gives the filter an opportunity to clean up any
     * resources that are being held (for example, memory, file handles,
     * threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {
    }
}
