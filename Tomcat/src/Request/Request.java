package Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Request {
    private String method;
    private String Uri;
    private String version;
    private InputStream in;
    private HashMap<String, String> properties = new HashMap<>();

    public String getUri() {
        return Uri;
    }

    private HashMap header = new HashMap();

    public Request(InputStream in) throws IOException {
        this.in = in;
        AnalysisLine();
        if (!method.toLowerCase().equals("get")) {
            AnanlysisPost();
        }else {
            AnalysisHeader();
        }
    }

    public void AnalysisLine() {
        String message = ReadLine();
        String[] messages = message.split(" ");
        method = messages[0];
        Uri = messages[1];
        version = messages[2];
    }

    public void AnalysisHeader() {
        while (true) {
            String keyAndValue = ReadLine();
            if (keyAndValue.length() < 2) {
                break;
            }
            String key = keyAndValue.split(":")[0];
            String value = keyAndValue.split(":")[1];
            header.put(key, value);
        }
    }


    private String AnanlysisPost(){
        StringBuffer postmessage=new StringBuffer();
        AnalysisHeader();
        String slength=(String) header.get("Content-Length");
        slength=slength.trim();
        int su=Integer.parseInt(slength);
        try {
            for(int i=0;i<su;i++){
                int c=in.read();
                postmessage=postmessage.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] messages=postmessage.toString().split(" ");
        for(String message:messages){
            System.out.println(message);
        }
        return postmessage.toString();
    }

    private String ReadLine() {
        StringBuffer message = new StringBuffer();
        int pre;
        int now;
        pre = -1;
        now = -1;
            try {
                while (true) {
                    now = in.read();
                    if (pre == 13 && now == 10) {
                        break;
                    }
                    message = message.append((char) now);
                    pre = now;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message1=message.toString();
            message=null;
            return message1;
        }

        public void parseUri () {
            if (Uri.contains("?")) {
                String[] message = Uri.split("\\?");
                Uri = message[0];
                String Information = message[1];
                String keyAndvalues[] = Information.split("&");
                for (String keyAndvalue : keyAndvalues) {
                    if (keyAndvalue.split("=").length > 1)
                        properties.put(keyAndvalue.split("=")[0], keyAndvalue.split("=")[1]);
                    else
                        properties.put(keyAndvalue.split("=")[0], null);
                }
            }
        }

        public String getParamater (String name){
            return properties.get(name);
        }

        public String getMethod () {
            return method;
        }

        public String getVersion () {
            return version;
        }

        public HashMap getHeader () {
            return header;
        }
    }
