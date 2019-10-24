package com.example.pay.web;

import com.example.pay.acp.sdk.SDKConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
/**
 * 
 * @ClassName AutoLoadServlet
 * @Description 
 * @date 2017-3-29 下午1:40:53
 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
 */
public class AutoLoadServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		super.init();
	}
}
