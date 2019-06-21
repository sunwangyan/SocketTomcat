package Response;

import Http.HttpContext;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class Response {
    private String version;
    private String code;
    private String description;
    private HashMap<String, String> Headers = new HashMap();
    private OutputStream os;
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public Response(OutputStream os) {
        this.os = os;
    }

    public void SendLine() {
        if (version == null || version.equals("")) {
            version = "HTTP/1.1";
        }
        if (code == null || code.equals("")) {
            code = "200";
        }
        if (description == null || description.equals("")) {
            description = "ok";
        }
        String message = version + " " + code + " " + description + "\n";
        for (char e : message.toCharArray()) {
            try {
                os.write((byte) e);
                os.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void write(String message) {
        SendLine();
       SendMessageHeader(message);
        try {
            os.write(13);
            os.write(10);
            os.write(message.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                System.out.println("关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getHeaders() {
        return Headers;
    }

    public void setHeaders(String message) {
        this.Headers.put(message.split(":")[0], message.split(":")[1]);
    }

    private void SendMessageHeader(String message) {
        if(Headers.isEmpty()){
        try {
            os.write("Content-Type: text/html;charset=UTF-8;".getBytes("UTF-8"));
            os.write(("Content-Length:" + message.length()).getBytes());
            os.write("Content-Language:zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }}
        else{
           Set<String> set= Headers.keySet();
           for(String e:set){
               try {
                   os.write((e+":"+Headers.get(e)).getBytes());
               } catch (IOException e1) {
                   e1.printStackTrace();
               }
           }
        }
    }

    public void SendHeader() {
        String type = file.getName().split("\\.")[1];
        setHeaders("Content-Length:" + file.length());
        String ConcentType = HttpContext.MIME_MAPPING.get(type);
        setHeaders("Content-Type:" + ConcentType);
        Set<String> keyset = Headers.keySet();
        String message = "";
        for (String e : keyset) {
            message = e + ":" + Headers.get(e) + "\n";
            try {
                os.write(message.getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void SendFile() throws IOException {
        InputStream in = new FileInputStream(file);
        byte[] message = new byte[1024];
        int i = -1;
        SendLine();
        SendHeader();
        os.write(13);
        os.write(10);
        while ((i = in.read(message, 0, 1024)) != -1) {
            os.write(message);
            os.flush();
        }
        os.close();
    }
}
