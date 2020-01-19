/**
 * POST跳转至收付直通车页面的接口方式
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.cib;

import java.util.Map;


public abstract class IRedirectService {

    /**
     * 生成跳转中间页的整页HTML代码
     *
     * @return 整个html页面代码
     */
    protected static String buildRedirectFullPage(String url, Map<String, String> params) {

        StringBuffer html = new StringBuffer();
        html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        html.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>收付直通车跳转接口</title></head>");
        html.append(buildRedirectHtml(url, params));
        html.append("<body></body></html>");

        return html.toString();
    }

    /**
     * 生成跳转JS代码
     *
     * @return 跳转JS代码
     */
    protected static String buildRedirectHtml(String url, Map<String, String> params) {

        StringBuffer html = new StringBuffer();
        html.append("<form id=\"epayredirect\" name=\"epayredirect\" action=\"" + url + "\" method=\"post\">");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            html.append("<input type=\"hidden\" name=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>");
        }

        html.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        html.append("<script>document.forms['epayredirect'].submit();</script>");

        return html.toString();
    }

    public abstract String build(Map<String, String> params);
}
