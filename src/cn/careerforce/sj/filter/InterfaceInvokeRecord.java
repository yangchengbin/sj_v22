package cn.careerforce.sj.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 16-4-18
 * Time: 上午11:17
 * To change this template use File | Settings | File Templates.
 */
public class InterfaceInvokeRecord implements Filter {

    private SimpleDateFormat format;
    private FileOutputStream fos;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String logPath = filterConfig.getServletContext().getRealPath("/logs");
        try {
            File file = new File(logPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            fos = new FileOutputStream(logPath + "/interface_invoke_record.log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer url = new StringBuffer(format.format(new Date()) + "\t\t").append(request.getRemoteAddr() + "\t\t").append(request.getRequestURL());
        String method = request.getMethod();
        if ("GET".equals(method)) {
            String query = request.getQueryString();
            fos.write((url.toString() + "?" + query + "\r\n").getBytes());
        } else {
            Enumeration<String> names = request.getParameterNames();
            StringBuffer params = new StringBuffer("");
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.append(name).append("=").append(request.getParameter(name)).append("&");
            }
            if (params.length() > 1) {
                params = params.deleteCharAt(params.length() - 1);
                fos.write((url.toString() + "?" + params + "\r\n").getBytes());
            } else {
                fos.write((url.toString() + "\r\n").getBytes());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
