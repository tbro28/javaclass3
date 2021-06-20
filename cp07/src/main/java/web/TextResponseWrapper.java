package web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TextResponseWrapper extends HttpServletResponseWrapper {

    //StringArr

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response
     * @throws IllegalArgumentException if the response is null
     */
    public TextResponseWrapper(HttpServletResponse response) {
        super(response);
    }



}
