package web;

import edu.uw.ext.quote.AlphaVantageQuote;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Servlet Filter to transform an XML response from the QuoteServlet into
 * other formats (json, plain, html). Also accepts json encoded request bodies,
 * for illustative purposes.
 */
public class QuoteTransformFilter implements Filter {

    AlphaVantageQuote alphaVantageQuote;

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
    public void init(FilterConfig filterConfig) throws ServletException {

        //filterConfig.getInitParameter()

    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time a request/response pair is passed through the
     * chain due to a client request for a resource at the end of the chain.
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

        //request.

        try {
            //getAlphaVantageQuote("BA");
            getAlphaVantageQuote(request.getParameter("symbol"));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        StringBuffer xmlDoc = new StringBuffer();
        xmlDoc.append("<quote>");
        xmlDoc.append("<symbol>"+alphaVantageQuote.getSymbol()+"</symbol>");
        xmlDoc.append("<price>"+alphaVantageQuote.getPrice()+"</price>");
        xmlDoc.append("</quote>");*/

        String outputType = request.getParameter("rstype");
        QuoteTransformFilter quoteTransformFilter = null;
        FilterConfig filterConfig = null;

        //response.set
        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse)response);
        chain.doFilter(request, wrapper);
        //chain.doFilter(request, response);

        StringReader xmlReader = new StringReader(wrapper.toString());



        StringBuffer xmlDoc = new StringBuffer();

        //xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");


        int cont = xmlReader.read();
        while(cont != -1) {
            //do something with data...
            //doSomethingWithData(data);
            xmlDoc.append((char)cont);
            cont = xmlReader.read();
        }
        xmlReader.close();

        //Source xmlSrc = new StreamSource(xmlReader);
        StringWriter output = new StringWriter();
        //StreamResult result = new StreamResult(output);


/*
        xmlDoc.append(xmlDoc.lastIndexOf("<symbol>")+8);
        xmlDoc.append("---");
        xmlDoc.append(xmlDoc.lastIndexOf("</symbol>")-1);
        xmlDoc.append("---");
        xmlDoc.append(xmlDoc.substring((xmlDoc.lastIndexOf("<symbol>")+8),(xmlDoc.lastIndexOf("</symbol>"))));
        xmlDoc.append(xmlDoc.substring((xmlDoc.lastIndexOf("<price>")+7),(xmlDoc.lastIndexOf("</price>"))));
*/

        String symbol = xmlDoc.substring((xmlDoc.lastIndexOf("<symbol>")+8),(xmlDoc.lastIndexOf("</symbol>")));
        String price = xmlDoc.substring((xmlDoc.lastIndexOf("<price>")+7),(xmlDoc.lastIndexOf("</price>")));

        //price.

        //xmlReader.
        //xmlSrc.

        //output.append("<p>HEHE</p>");
        //output.append(request.getParameter("price"));

        switch (outputType) {
            case "xml":
                //response.setContentType("text/xml");
/*                response.setContentType("text/xml");
                response.setContentLength(xmlSrc.length());
                response.getWriter().print(xmlSrc);*/
                output.append(xmlDoc);
                break;
            case "html":
                response.setContentType("text/html");

                //int xmlTag = xmlDoc.indexOf("?>");

                String htmlDoc = "<!DOCTYPE html><html><body>" + xmlDoc.substring(xmlDoc.indexOf("?>")+2, xmlDoc.length()) + "</body></html>";

                output.append(htmlDoc);
                //charResponseWrapper = new CharResponseWrapper(response);
                //quoteTransformFilter.init(filterConfig);  //???????????????????
                //quoteTransformFilter.doFilter(request, response, chain);

/*                response.setContentLength(xmlDoc.length());
                response.getWriter().print(xmlDoc);*/
                break;
            case "json":
                response.setContentType("application/json");
                //wrapper.getWriter().print("Filter JSON break!!!!!!!!!!!!");

                //{"type":"salutation", "phrase":"Hello"}
                output.append( "{\"symbol\":\"" + symbol + "\", \"price\":\""+ price +"\"}");

                break;
            case "plain":
                response.setContentType("text/plain");

                output.append("Ticker symbol: "
                        + symbol
                        + " has a price of: "
                        + price
                );

                break;
        }





//        Transformer transformer;
 //       transformer = transformerFactory.newTransformer(); // identity transform
 //       transformer.transform(xmlSrc, result);

        String respStr = output.toString();
        response.setContentLength(respStr.length());
        response.getWriter().write(output.toString());



/*        CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse)response);
        chain.doFilter(request, wrapper);
        wrapper.setContentType("text/xml");
        wrapper.setContentLength(xmlDoc.length());
        wrapper.getWriter().print(xmlDoc);*/




    }

    public void getAlphaVantageQuote(String stock) throws IOException {
        alphaVantageQuote = AlphaVantageQuote.getQuote(stock);
        System.out.println(alphaVantageQuote.getSymbol());
        System.out.println(alphaVantageQuote.getPrice());
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
