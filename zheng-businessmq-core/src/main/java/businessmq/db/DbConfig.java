package businessmq.db;

/**
 * Created by alan.zheng on 2017/2/14.
 */
public class DbConfig {
    /**
     * driver
     */
    private String driver;
    /**
     * url
     */
    private String url;
    /**
     * username
     */
    private String username;
    /**
     * password
     */
    private String password;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
