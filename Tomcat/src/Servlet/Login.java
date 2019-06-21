package Servlet;

import Http.HttpServlet;
import Request.Request;
import Response.Response;

public class Login  implements HttpServlet {
    @Override
    public void doPost(Request request, Response response) {

    }

    @Override
    public void Service(Request request, Response response) {
          System.out.println(request.getVersion());
          System.out.println(request.getParamater("user"));
          response.write("<!DOCTYPE html>\n" +
                  "<html lang=\"en\">\n" +
                  "<head>\n" +
                  "    <meta charset=\"UTF-8\">\n" +
                  "    <title>Title</title>\n" +
                  "</head>\n" +
                  "<body>\n" +
                  "\n<p>你好</p>" +
                  "</body>\n" +
                  "</html>");
    }
    @Override
    public void doGet(Request request, Response response) {

    }
}
