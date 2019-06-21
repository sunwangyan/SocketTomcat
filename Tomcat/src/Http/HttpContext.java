package Http;

import Servlet.ServletReflex;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
* http协议相关内容
* */
public class HttpContext {
    public  static Map<String,String> MIME_MAPPING=new HashMap<>();
    public HttpContext(){
        Resolve();
    }
    private  static void Resolve(){
        SAXReader reader=new SAXReader();
        try {
            Document document=reader.read("conf/web.xml");
            Element bookstore=document.getRootElement();
            List<Element> it=bookstore.elements("mime-mapping");
            for(Element e:it){
                MIME_MAPPING.put(e.elementText("extension"),e.elementText("mime-type"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
