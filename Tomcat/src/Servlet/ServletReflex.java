package Servlet;

import Request.Request;
import Response.Response;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ServletReflex {
    private Request request;
    private Response response;
    private static HashMap<String,String> servlet=new HashMap<>();
    private static HashMap<String,String> servletMapping=new HashMap<>();
    public ServletReflex(Request request,Response response) {
        this.request = request;
        this.response= response;
        AnalysisMapping();
        InvokeServletClass();
    }

    private void InvokeServletClass() {
        String uriname = request.getUri();
        String servletname= servletMapping.get(uriname);
        String Classname= servlet.get(servletname);
        System.out.println(uriname);
        String methodType = request.getMethod();
        try {
            Class cl = Class.forName(Classname);
            Object o = cl.newInstance();
            if (methodType.toLowerCase().equals("get")) {
                Method method = cl.getMethod("doGet",Request.class,Response.class);
                method.invoke(o,request,response);
                Method method1 = cl.getMethod("Service", Request.class, Response.class);
                method1.invoke(o,request,response);
            } else if (methodType.toLowerCase().equals("post")) {
                Method method = cl.getMethod("doPost", Request.class, Response.class);
                method.invoke(o,request,response);
                Method method1 = cl.getMethod("Service", Request.class, Response.class);
                method1.invoke(o,request,response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void AnalysisMapping(){
        SAXReader reader=new SAXReader();
        try {
            Document document=reader.read("webapps/static/WEB-INF/web.xml");
            Element bookstore=document.getRootElement();
            List<Element> it=bookstore.elements("servlet");
            for(Element e:it){
                servlet.put(e.elementText("servlet-name"),e.elementText("servlet-class"));
            }
            List<Element> it1=bookstore.elements("servlet-mapping");
            for(Element e:it1){
                System.out.println(e.elementText("url-pattern"));
                servletMapping.put(e.elementText("url-pattern"),e.elementText("servlet-name"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
