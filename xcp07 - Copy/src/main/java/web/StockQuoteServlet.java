package web;

import edu.uw.ext.quote.AlphaVantageQuote;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import edu.uw.ext.quote.AlphaVantageQuote;

/**
 * Servlet implementation class StockQuoteServlet
 */
@SuppressWarnings("CheckStyle")
public class StockQuoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ServletContext ctx;
    private String salutationXml;
    private String valedictionXml;
    private String defaultXml;

    AlphaVantageQuote alphaVantageQuote;
    StringBuffer xmlDoc;

    //    public AlphaVantageQuote getAlphaVantageQuote(String stock) throws IOException {
    public void getAlphaVantageQuote(String stock) throws IOException {
        alphaVantageQuote = AlphaVantageQuote.getQuote(stock);
        System.out.println(alphaVantageQuote.getSymbol());
        System.out.println(alphaVantageQuote.getPrice());
    }


    /**
     * Default constructor. 
     */
    public StockQuoteServlet() {
    }

    /**
     * //@see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig servletCfg) throws ServletException {


    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.getWriter().print("Do GET!!!!!!!!!!!!");
	    serviceRequest(request, response);
	}

	/**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serviceRequest(request, response);
    }

	void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        <?xml version="1.0" encoding="UTF-8"?>
        <courtesy>
          <type>salutation</type>
          <phrase>Hello</phrase>
        </courtesy>
        */

        try {
            //getAlphaVantageQuote("BA");
            getAlphaVantageQuote(request.getParameter("symbol"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String courtesy = request.getParameter("courtesyType");
        //String responseXml = "<html>Do post? One time."+alphaVantageQuote.getPrice()+"</html>";
        //String responseXml = "<html>Do post? One time."+alphaVantageQuote.getPrice()+"</html>";
        createXml();

        request.setAttribute("symbol", alphaVantageQuote.getSymbol());
        request.setAttribute("price", alphaVantageQuote.getPrice());

        /*
        set content type and filter?
                application/xml if not readable from casual users (RFC 3023, section 3)
                text/xml if readable from casual users (RFC 3023, section 3)
                application/json
                text/plain
                text/html
        */

        String outputType = request.getParameter("rstype");
        CharResponseWrapper charResponseWrapper = null;

        QuoteTransformFilter quoteTransformFilter = null;
        FilterChain filterChain = null;
        FilterConfig filterConfig = null;
        //This is GET, not SET: filterConfig.getInitParameter(outputType);

        //filterChain.doFilter(request, response);  //?????????????????????????????
        //filterChain.doFilter(request, response);

/*        switch (outputType) {

            case "xml":
                response.setContentType("text/xml");
                *//*response.setContentLength(xmlDoc.length());
                response.getWriter().print(xmlDoc);*//*
                break;
            case "html":
                response.setContentType("text/html");
                //charResponseWrapper = new CharResponseWrapper(response);

*//*
                quoteTransformFilter.init(filterConfig);  //???????????????????
                quoteTransformFilter.doFilter(request, response, filterChain);
*//*

                break;
            case "json":
                response.setContentType("application/json");

                break;
            case "plain":
                response.setContentType("text/plain");

                break;
        }*/

        response.setContentType("text/xml");
        response.setContentLength(xmlDoc.length());
        response.getWriter().print(xmlDoc);

//        quoteTransformFilter.doFilter(request, response, filterChain);
/*
        if( outputType == "xml") {
        }
        else {
            FilterConfig filterConfig;
            filterConfig.
        }
*/
    }

    private void createXml() {
        xmlDoc = new StringBuffer();
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlDoc.append("<quote>");
        xmlDoc.append("<symbol>"+alphaVantageQuote.getSymbol()+"</symbol>");
        xmlDoc.append("<price>"+formatPrice()+"</price>");
        xmlDoc.append("</quote>");
    }


    private StringBuffer formatPrice() {

        String priceString = String.valueOf(alphaVantageQuote.getPrice());
        StringBuffer price = new StringBuffer();

        price.append("$");
        price.append(priceString, 0, priceString.length()-2);
        price.append(".");
        price.append(priceString, priceString.length()-2, priceString.length());

        return price;
    }

}
