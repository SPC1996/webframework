package com.keessi.xml.util.file;

import com.lowagie.text.pdf.BaseFont;
import org.springframework.web.context.ContextLoader;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * PDF 生成工具，提供将HTML文件转化为PDF的功能
 */
public class PdfHelper {
    /**
     * 用户发起生成PDF文件的request
     */
    private HttpServletRequest request;

    /**
     * 用于回应对应的请求
     */
    private HttpServletResponse response;

    /**
     * 模板存放路径
     */
    private String templatePath;

    /**
     * 模板中用到的字体需要将对应的字体文件放入指定路径下，此为字体文件存放路径
     */
    private String fontPath;

    private static final String DEFAULT_TEMPLATE_PATH = "/static/template/";

    private static final String DEFAULT_IMAGE_PATH = "/static/image/";

    private static final String DEFAULT_FONT_PATH = "/static/font/";

    private static final String ROOT_PATH = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");

    public PdfHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.templatePath = DEFAULT_TEMPLATE_PATH;
        this.fontPath = DEFAULT_FONT_PATH;
    }

    /**
     * 根据模板名称和变量集合动态的生成PDF文件
     *
     * @param templateName 模板名称
     * @param fileName     生成的文件名称
     * @param variables    变量集合
     * @throws Exception
     */
    public void createPdf(String templateName, String fileName, Map<String, Object> variables) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/file");
        String encodedFileName;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent && agent.contains("MSIE")) {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        } else if (null != agent && agent.contains("Mozilla")) {
            encodedFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        } else {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

        OutputStream out = response.getOutputStream();

        String htmlStr = generate(variables, templateName);

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        File f = new File(ROOT_PATH + fontPath);
        if (f.isDirectory()) {
            File[] files = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".otf") || lower.endsWith(".ttf");
                }
            });
            for (int i = 0; i < files.length; i++) {
                fontResolver.addFont(files[i].getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
        }

        renderer.setDocumentFromString(htmlStr);
        renderer.layout();
        renderer.createPDF(out);

        out.close();
    }

    /**
     * 通过thymeleaf引擎将使用thymeleaf标签修饰的动态的模板文件转化为静态的HTML文件，返回字符串
     *
     * @param variables    变量集合
     * @param templateName 模板名称
     * @return
     * @throws Exception
     */
    private String generate(Map<String, Object> variables, String templateName) throws Exception {
        //创建模板解析器
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(ROOT_PATH + templatePath);
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);

        //创建模板引擎，并初始化解析器
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        //初始化输出流
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        //获取WEB上下文
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("basePath",
                request.getScheme() + "://" +
                        request.getServerName() + ":" +
                        request.getServerPort() + "/" +
                        request.getContextPath() + "/");
        System.out.println(context.getVariable("basePath"));
        context.setVariables(variables);
        engine.process(templateName, context, bufferedWriter);

        //关闭输出流
        stringWriter.flush();
        stringWriter.close();
        bufferedWriter.flush();
        bufferedWriter.close();

        //输出HTML字符串
        return stringWriter.toString();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }
}
