package edu.uw.tjb;

import edu.uw.ext.quote.AlphaVantageQuote;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Servlet implementation class StockQuoteServlet.
 */
@SuppressWarnings("CheckStyle")
public class StockQuoteServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;
    AlphaVantageQuote alphaVantageQuote;
    StringBuffer xmlDoc;


    /**
     * Default constructor. 
     */
    public StockQuoteServlet() {}


    /**
     * //@see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig servletCfg) throws ServletException {}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    serviceRequest(request, response);
	}


	/**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serviceRequest(request, response);
    }


    /**
     *
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
	void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            getAlphaVantageQuote(request.getParameter("symbol"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        createXml();

        request.setAttribute("symbol", alphaVantageQuote.getSymbol());
        request.setAttribute("price", alphaVantageQuote.getPrice());

        response.setContentType("text/xml");
        response.setContentLength(xmlDoc.length());
        response.getWriter().print(xmlDoc);
    }


    /**
     * Get the stock quote.
     *
     * @param stock
     * @throws IOException
     */
    public void getAlphaVantageQuote(String stock) throws IOException {
        alphaVantageQuote = AlphaVantageQuote.getQuote(stock);
    }


    /**
     * Create the xml document for the symbol and price.
     */
    private void createXml() {
        xmlDoc = new StringBuffer();
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlDoc.append("<quote>");
        xmlDoc.append("<symbol>"+alphaVantageQuote.getSymbol()+"</symbol>");
        xmlDoc.append("<price>"+formatPrice()+"</price>");
        xmlDoc.append("</quote>");
    }


    /**
     * Formats the price, since it comes back with a decimal.
     *
     * @return the formatted price.
     */
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
