package com.example.pay.acp.sdk;

import java.io.*;
import java.util.Properties;




/**
 * ClassName: EciticSDKConfig
 * Description:
 * date: 2019/9/3 17:10
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
public class EciticSDKConfig {

    public static final String FILE_NAME = "ecitic_sdk.properties";

    /** 签名证书路径. */
    private String signCertPath;
    /** 签名证书密码. */
    private String signCertPwd;
    /** 加密公钥证书路径. */
    private String encryptCertPath;
    /** 验证签名公钥证书目录. */
    private String validateCertDir;
    /** 安全密钥(SHA256和SM3计算时使用) */
    private String secureKey;
    /** 根证书路径  */
    private String rootCertPath;

    /** 操作对象. */
    private static EciticSDKConfig config = new EciticSDKConfig();
    /** 属性文件对象. */
    private Properties properties;



    /** 配置文件中签名证书路径常量. */
    public static final String SDK_SIGNCERT_PATH = "eciticsdk.signCert.path"; //signCertPath
    /** 配置文件中签名证书密码常量. */
    public static final String SDK_SIGNCERT_PWD = "eciticdk.signCert.pwd";

    /** 配置文件中安全密钥 */
    public static final String SDK_SECURITYKEY = "eciticsdk.secureKey.key";
    /** 配置文件中根证书路径常量  */
 //   public static final String SDK_ROOTCERT_PATH = "acpsdk.rootCert.path";
    private EciticSDKConfig() {
        super();
    }

    /**
     * 获取config对象.
     * @return
     */
    public static EciticSDKConfig getConfig() {
        return config;
    }

    public String getSignCertPath() {
        return signCertPath;
    }

    public void setSignCertPath(String signCertPath) {
        this.signCertPath = signCertPath;
    }

    public String getSignCertPwd() {
        return signCertPwd;
    }

    public void setSignCertPwd(String signCertPwd) {
        this.signCertPwd = signCertPwd;
    }

    public String getEncryptCertPath() {
        return encryptCertPath;
    }

    public void setEncryptCertPath(String encryptCertPath) {
        this.encryptCertPath = encryptCertPath;
    }

    public String getValidateCertDir() {
        return validateCertDir;
    }

    public void setValidateCertDir(String validateCertDir) {
        this.validateCertDir = validateCertDir;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getRootCertPath() {
        return rootCertPath;
    }

    public void setRootCertPath(String rootCertPath) {
        this.rootCertPath = rootCertPath;
    }

    /**
     * 从properties文件加载
     *
     * @param rootPath
     *            不包含文件名的目录.
     */
    public void loadPropertiesFromPath(String rootPath) {
        if (rootPath != null && !"".equals(rootPath.trim())) {
            LogUtil.writeLog("从路径读取配置文件: " + rootPath+ File.separator+FILE_NAME);
            File file = new File(rootPath + File.separator + FILE_NAME);
            InputStream in = null;
            if (file.exists()) {
                try {
                    in = new FileInputStream(file);
                    properties = new Properties();
                    properties.load(in);
                    loadProperties(properties);
                } catch (FileNotFoundException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                } catch (IOException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                } finally {
                    if (null != in) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            LogUtil.writeErrorLog(e.getMessage(), e);
                        }
                    }
                }
            } else {
                // 由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
                LogUtil.writeErrorLog(rootPath + FILE_NAME + "不存在,加载参数失败");
            }
        } else {
            loadPropertiesFromSrc();
        }

    }

    /**
     * 从classpath路径下加载配置参数
     */
    public void loadPropertiesFromSrc() {
        InputStream in = null;
        try {
            LogUtil.writeLog("从classpath: " +SDKConfig.class.getClassLoader().getResource("").getPath()+" 获取属性文件"+FILE_NAME);
            in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
            if (null != in) {
                properties = new Properties();
                try {
                    properties.load(in);
                } catch (IOException e) {
                    throw e;
                }
            } else {
                LogUtil.writeErrorLog(FILE_NAME + "属性文件未能在classpath指定的目录下 "+SDKConfig.class.getClassLoader().getResource("").getPath()+" 找到!");
                return;
            }
            loadProperties(properties);
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 根据传入的 {@link #load(Properties)}对象设置配置参数
     *
     * @param pro
     */
    public void loadProperties(Properties pro) {
        LogUtil.writeLog("开始从属性文件中加载配置项");
        String value = null;

        value = pro.getProperty(SDK_SIGNCERT_PATH);
        if (!SDKUtil.isEmpty(value)) {
            this.signCertPath = value.trim();
            LogUtil.writeLog("配置项：私钥签名证书路径==>" + SDK_SIGNCERT_PATH + "==>" + value + " 已加载");
        }
        value = pro.getProperty(SDK_SIGNCERT_PWD);
        if (!SDKUtil.isEmpty(value)) {
            this.signCertPwd = value.trim();
            LogUtil.writeLog("配置项：私钥签名证书密码==>" + SDK_SIGNCERT_PWD + " 已加载");
        }

       /* value = pro.getProperty(SDK_ENCRYPTCERT_PATH);
        if (!SDKUtil.isEmpty(value)) {
            this.encryptCertPath = value.trim();
            LogUtil.writeLog("配置项：敏感信息加密证书==>" + SDK_ENCRYPTCERT_PATH + "==>" + value + " 已加载");
        }*/
        value = pro.getProperty(SDK_SECURITYKEY);
        if (!SDKUtil.isEmpty(value)) {
            this.secureKey = value.trim();
        }
    }

}
