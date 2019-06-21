import Http.HttpContext;
import Request.Request;
import Response.Response;
import Servlet.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat implements Runnable{
    private Socket socket;
    private static ServerSocket serverSocket;
    private ExecutorService threadpool;
    public void initTomcat(){
        HttpContext httpContext=new HttpContext();
        try {
            System.out.println("服务器初始化....");
            serverSocket =new ServerSocket(8080);
            threadpool= Executors.newFixedThreadPool(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                System.out.println("服务器初始化成功，等待连接");
                socket=serverSocket.accept();
                threadpool.execute(this);
                System.out.println("服务器连接成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        Request request=null;
        Response response=null;
        try {
            request=new Request(socket.getInputStream());
            response=new Response(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(request.getUri().split("\\?").length>1) {
            request.parseUri();
        }
        if(request.getUri().split("\\.").length>1){
            File file=null;
            file=new File("webapps/static"+request.getUri());
//          response.setHeaders("Content-Encoding:"+request.getHeader().get("Accept-Encoding"));
            System.out.println(request.getHeader().get("Accept-Language"));
            response.setHeaders("Content-Language:"+request.getHeader().get("Accept-Language"));
            try {
                response.setFile(file);
                response.SendFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
          ServletReflex servletReflex=new ServletReflex(request,response);
        }
    }


    public static void main(String[] Args){
        Tomcat tomcat=new Tomcat();
        tomcat.initTomcat();
    }
}
