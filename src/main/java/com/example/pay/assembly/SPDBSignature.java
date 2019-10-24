package com.example.pay.assembly;

import com.example.pay.util.HttpsUtils;
import com.example.pay.util.Md5Util;
import com.example.pay.util.SpdBankUtil;
import okhttp3.Response;
import org.jsoup.Connection;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: SPDBSignature
 * Description:
 * date: 2019/7/23 17:58
 *
 * @author 陈杰
 * @version 1.0
 * @since JDK 1.8
 * .........┌─┐              ┌─┐
 * ...┌──┘  ┴───────┘  ┴──┐
 * ...│                                  │
 * ...│          ───                  │
 * ...│     ─┬┘       └┬─          │
 * ...│                                  │
 * ...│           ─┴─                 │
 * ...│                                  │
 * ...└───┐                  ┌───┘
 * ...........│                  │
 * ...........│                  │
 * ...........│                  │
 * ...........│                  └──────────────┐
 * ...........│                                                │
 * ...........│                                                ├─┐
 * ...........│                                                ┌─┘
 * ...........│                                                │
 * ...........└─┐    ┐    ┌───────┬──┐    ┌──┘
 * ...............│  ─┤  ─┤              │  ─┤  ─┤
 * ...............└──┴──┘              └──┴──┘
 * --------------------------------神兽保佑--------------------------------
 * --------------------------------代码无BUG!------------------------------
 */
public class SPDBSignature {


    public static Object  getSPDBSignature(String urlStr , String content, String interfacename, String flag) {
        //请求报文体
        //   String content = "{\"name\":\"zhangshan\"}";
        HashMap<String ,String> map= new  HashMap<String ,String>();
        //   HashMap<String ,String> filemap= new  HashMap<String ,String>();
        //X-SPDB-ClientID-Secret
        String secret = "nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD";//   nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD
        // 防重放参数doPostRequest
        boolean forbidden = false;// 是否防重放
        String newBodyData = content;
        //   String FilesMD5= Md5Utility.String2MD5(content);
        //   filemap.put("FilesMD5",FilesMD5);
        if (forbidden) {
            //X-SPDB-Timestamp
            long timestamp = System.currentTimeMillis();//毫秒,调用时请取实际毫秒
            //X-SPDB-Nonce
            String nonce = "TRANS10145581";
            newBodyData = content + timestamp + nonce;
            System.out.println("X-SPDB-Timestamp："+timestamp);
            System.out.println("X-SPDB-Nonce："+nonce);
            System.out.println("签名原文：" + newBodyData);
        }
        String sign = SpdBankUtil.sign(newBodyData, secret);
        map.put("content",newBodyData);
        map.put("sign",sign);
        try {
            Connection.Response rs= HttpsUtils.post(urlStr,map);
            //  System.out.println(rs.body());
            return  rs.body();
        //    JSONObject o= JSONObject.parseObject(rs.body().toString());
       //     return  o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("原报文体："+newBodyData);
        System.out.println("X-SPDB-ClientID-Secret："+secret);
        System.out.println("X-SPDB-SIGNATURE:" + sign);
        System.out.println("验签：" + SpdBankUtil.validateSign(newBodyData, secret, sign));
        return  null;
    }



    public static Object  getSPDBSignature(String urlStr , String content, String interfacename, String flag, Map<String, File> fileMap)   {
        //请求报文体
     //   String content = "{\"name\":\"zhangshan\"}";
        HashMap<String ,String> map= new  HashMap<String ,String>();
     //   HashMap<String ,String> filemap= new  HashMap<String ,String>();
        //X-SPDB-ClientID-Secret
        String secret = "nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD";//
        // 防重放参数doPostRequest
        boolean forbidden = false;// 是否防重放
        String newBodyData = content;
      //  String FilesMD5= Md5Utility.String2MD5(content);
     //   filemap.put("FilesMD5",FilesMD5);
        String FilesMD5="";
        try {
            // 添加文件参数
            if (null != fileMap && !fileMap.isEmpty()) {
                //   inputStreamList = new ArrayList<InputStream>();
                InputStream in = null;
                File file = null;
                Set<Map.Entry<String, File>> set = fileMap.entrySet();
                for (Map.Entry<String, File> e : set) {
                    file = e.getValue();
                }
                FilesMD5=Md5Util.getFileMD5String(file);
                newBodyData=FilesMD5;
                map.put("content", content);
                map.put("clientID", "d83fa07c-0b7f-40e5-b53a-8c21f3c0437f");
                map.put("clientSecret","nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD");
                map.put("FilesMD5", FilesMD5);
            } else {
                map.put("content", newBodyData);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if (forbidden) {


            //X-SPDB-Timestamp
            long timestamp = System.currentTimeMillis();//毫秒,调用时请取实际毫秒
            //X-SPDB-Nonce
            String nonce = "TRANS10145581";
            newBodyData = content + timestamp + nonce;
            System.out.println("X-SPDB-Timestamp："+timestamp);
            System.out.println("X-SPDB-Nonce："+nonce);
            System.out.println("签名原文：" + newBodyData);
        }
        String sign = SpdBankUtil.sign(newBodyData, secret);


        map.put("sign",sign);
        try {
            Response rs= HttpsUtils.post(urlStr,map,fileMap);
        //   System.out.println(rs.body());
        //    JSONObject json= new JSONObject();
       //     System.out.println(rs.body().string());
       //     JSONObject o= JSONObject.parseObject(rs.body().string());
       //     return  o;
            return  rs.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("原报文体："+newBodyData);
        System.out.println("X-SPDB-ClientID-Secret："+secret);
        System.out.println("X-SPDB-SIGNATURE:" + sign);
        System.out.println("验签：" + SpdBankUtil.validateSign(newBodyData, secret, sign));
        return  null;
    }

}
