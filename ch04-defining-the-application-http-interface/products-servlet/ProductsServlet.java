import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class ProductsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request,
	   HttpServletResponse response) throws ServletException, IOException {
	
	   try {
		   final String ean = request.getParameter("ean");
		   final Long eanCode = Long.parseLong(ean);
		   // Process request…
	
	   }
	   catch (NumberFormatException e) {
		   final int status = HttpServletResponse.SC_BAD_REQUEST;
		   response.sendError(status, e.getMessage());
	   }
	}
}