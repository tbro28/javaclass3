package web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CourtesyServlet
 */
public class CourtesyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ServletContext ctx;
    private String salutationXml;
    private String valedictionXml;
    private String defaultXml;
    
    /**
     * Default constructor. 
     */
    public CourtesyServlet() {
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig servletCfg) throws ServletException {
        ctx = servletCfg.getServletContext();
        String path = servletCfg.getInitParameter("salutation");
        String realPath = ctx.getRealPath(path);
        File f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            salutationXml = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading salutaion file.", e);
        }

        path = servletCfg.getInitParameter("valediction");
        realPath = ctx.getRealPath(path);
        f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            valedictionXml = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading valediction file.", e);
        }

        path = servletCfg.getInitParameter("default");
        realPath = ctx.getRealPath(path);
        f = new File(realPath);
        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(f))) {
            byte[] bytes = new byte[(int)f.length()];
            dataIn.readFully(bytes);
            defaultXml = new String(bytes, "UTF8");
        } catch (IOException e) {
            ctx.log("Error reading default file.", e);
        }
    }

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

	void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courtesy = request.getParameter("courtesyType");
        String responseXml = null;
        switch (courtesy) {
            case "salutation":
                responseXml = salutationXml;
                break;
            case "valediction":
                responseXml = valedictionXml;
                break;
            default:
                responseXml = defaultXml;
        }

        response.setContentType("text/xml");
        response.setContentLength(responseXml.length());
        response.getWriter().print(responseXml);
    }

}
