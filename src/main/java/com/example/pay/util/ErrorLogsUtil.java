package com.example.pay.util;

import com.example.pay.demo.MySqlConfig;
import com.example.pay.exception.MyException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorLogsUtil {

    /**
     * @Author  chenjie
     * @param: [e]
     * @return  void
     * @Date 10:33 2019/2/28
     * @Description  打印错误日志并保存到数据库
     *         ┌─┐              ┌─┐
     *   ┌──┘  ┴───────┘  ┴──┐
     *   │                                  │
     *   │          ───                  │
     *   │     ─┬┘       └┬─          │
     *   │                                  │
     *   │           ─┴─                 │
     *   │                                  │
     *   └───┐                  ┌───┘
     *           │                  │
     *           │                  │
     *           │                  │
     *           │                  └──────────────┐
     *           │                                                │
     *           │                                                ├─┐
     *           │                                                ┌─┘
     *           │                                                │
     *           └─┐    ┐    ┌───────┬──┐    ┌──┘
     *               │  ─┤  ─┤              │  ─┤  ─┤
     *               └──┴──┘              └──┴──┘
     *                神兽保佑
     *               代码无BUG!
     */
    public static void error(Exception e) {
        StackTraceElement stackTraceElement= e.getStackTrace()[0];
        Connection con = null;
        try {
            Class.forName(MySqlConfig.driver);
            con = (Connection) DriverManager.getConnection(MySqlConfig.url, MySqlConfig.username, MySqlConfig.password);
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        PreparedStatement ps = null;
        String sql = "INSERT INTO pay_error_logs VALUES (getdate(), ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            //打印日志，错在第几行
            String errorInfo = e.toString()+",errorMassage:"+stackTraceElement+"---"+e.getMessage()+","+"errorLine:"+stackTraceElement.getLineNumber();
            ps.setString(1, errorInfo);
            ps.setInt(2, 1); //错误类型为4
            ps.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
                if(con != null) {
                    con.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }



    public static void error(MyException e) {
        StackTraceElement stackTraceElement= e.getStackTrace()[0];
        Connection con = null;
        try {
            Class.forName(MySqlConfig.driver);
            con = (Connection) DriverManager.getConnection(MySqlConfig.url, MySqlConfig.username, MySqlConfig.password);
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        PreparedStatement ps = null;
        String sql = "INSERT INTO pay_error_logs VALUES (getdate(), ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            //打印日志，错在第几行
            String errorInfo = e.toString()+",errorMassage:"+stackTraceElement+"---"+e.getMessage()+","+"errorLine:"+stackTraceElement.getLineNumber();
            ps.setString(1, e.getMsg());
            ps.setInt(2, e.getCode()); //错误类型为4
            ps.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if(ps != null) {
                    ps.close();
                }
                if(con != null) {
                    con.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }
}
