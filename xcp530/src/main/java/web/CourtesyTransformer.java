package web;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Servlet Filter implementation class CourtesyTransformer
 */
public class CourtesyTransformer implements Filter {
    private ServletContext ctx;
    private String toHtmlXslt;
    private String toJsonXslt;
    private String toPlainXslt;

    /**
     * Default constructor. 
     */
    public CourtesyTransformer() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse)response);
	    chain.doFilter(request, wrapper);
	    
	    StringReader xmlReader = new StringReader(wrapper.toString());
	    Source xmlSrc = new StreamSource(xmlReader);
	    
        StringWriter output = new StringWriter();

	    try {
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        String formatTypeStr = request.getParameter("format");
	        String xslt;
	        switch (formatTypeStr) {
	            case "html":
	               xslt = toHtmlXslt;
	               response.setContentType("text/html");
	               break;
	            case "json":
	                xslt = toJsonXslt;
	                response.setContentType("text/plain");
	                break;
	            case "plain":
	                xslt = toPlainXslt;
	                response.setContentType("text/plain");
	                break;
	            default:
	                xslt = null;
	                response.setContentType("text/xml");
	                break;
	        }
	        
	        Transformer transformer;
	        if (xslt != null) {
	           StringReader xsltReader = new StringReader(xslt);
	           Source xsltSrc = new StreamSource(xsltReader);
	           transformer =transformerFactory.newTransformer(xsltSrc);
	        } else {
	            transformer = transformerFactory.newTransformer(); // identity transform
	        }
	        StreamResult result = new StreamResult(output);
	        transformer.transform(xmlSrc, result);
	    } catch(Exception ex) {
	        ctx.log("Error processing transform.", ex);
	    }
	    String respStr = output.toString();
	    response.setContentLength(respStr.length());
	    response.getWriter().write(output.toString());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterCfg) throws ServletException {
	    ctx = filterCfg.getServletContext();
        String path = filterCfg.getInitParameter("htmlXslt");
        String realPath = ctx.getRealPath(path);
        File f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            toHtmlXslt = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading HTMP transform file.", e);
        }

        path = filterCfg.getInitParameter("jsonXslt");
        realPath = ctx.getRealPath(path);
        f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            toJsonXslt = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading JSON transform file.", e);
        }

        path = filterCfg.getInitParameter("plainXslt");
        realPath = ctx.getRealPath(path);
        f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            toPlainXslt = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading plain text transform file.", e);
        }
	}

}
