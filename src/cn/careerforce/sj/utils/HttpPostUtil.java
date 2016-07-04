package cn.careerforce.sj.utils;

import cn.careerforce.config.Configuration;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class HttpPostUtil {
    private URL url;
    private HttpURLConnection conn;
    private String boundary = "--------httppost123";
    private Map<String, String> textParams = new HashMap<String, String>();
    private Map<String, MultipartFile> fileParams = new HashMap<String, MultipartFile>();
    private DataOutputStream ds;

    public HttpPostUtil() throws Exception {
        this.url = new URL(Configuration.getValue("uploader_url"));
        textParams.put("token", Configuration.getValue("open_token"));
        textParams.put("clientid", Configuration.getValue("open_clientid"));
    }

    //增加一个文件到form表单数据中
    public void addFileParameter(String name, MultipartFile value) {
        fileParams.put(name, value);
    }

    // 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
    public byte[] send() throws Exception {
        initConnection();
        conn.connect();
        ds = new DataOutputStream(conn.getOutputStream());
        writeFileParams();
        writeStringParams();
        paramsEnd();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        conn.disconnect();
        return out.toByteArray();
    }

    //文件上传的connection的一些必须设置
    private void initConnection() throws Exception {
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000); //连接超时为10秒
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    //普通字符串数据
    private void writeStringParams() throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
            ds.writeBytes("\r\n");
            ds.writeBytes(encode(value) + "\r\n");
        }
    }

    //文件数据
    private void writeFileParams() throws Exception {
        Set<String> keySet = fileParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            MultipartFile value = fileParams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + encode(value.getOriginalFilename()) + "\"\r\n");
            ds.writeBytes("Content-Type: " + value.getContentType() + "\r\n");
            ds.writeBytes("\r\n");
            ds.write(getBytes(value));
            ds.writeBytes("\r\n");
        }
    }

    //把文件转换成字节数组
    private byte[] getBytes(MultipartFile f) throws Exception {
        InputStream in = f.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    //添加结尾数据
    private void paramsEnd() throws Exception {
        ds.writeBytes("--" + boundary + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    // 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static List<String> upload(MultipartFile[] file) throws Exception {
        HttpPostUtil u = new HttpPostUtil();
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            u.addFileParameter("file", file[i]);
            byte[] b = u.send();
            String result = new String(b);
            JSONObject jsonObject = JSONObject.fromObject(result);
            urls.add(jsonObject.get("message").toString());
        }
        return urls;
    }
}
