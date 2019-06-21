package Http;

import Request.Request;
import Response.Response;

public interface HttpServlet {
    public void doPost(Request request, Response response);
    public void doGet(Request request,Response response);
    public void Service(Request request,Response response);

}
