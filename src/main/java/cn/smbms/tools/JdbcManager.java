package cn.smbms.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JdbcManager {
    private static JdbcManager jdbcManager;
    private static Properties properties;
    private JdbcManager(){
        properties = new Properties();
        InputStream is = JdbcManager.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //声明静态内部类实例化JdbcManager对象
    public static class JdbcManagerInit{
        private static final JdbcManager INSTANCE = new JdbcManager();
    }

    //全局访问点
    public static JdbcManager getInstance(){
        jdbcManager = JdbcManagerInit.INSTANCE;
        return jdbcManager;
    }

    //获取配置文件中的value
    public String getValue(String key){
        return properties.getProperty(key);
    }
}
