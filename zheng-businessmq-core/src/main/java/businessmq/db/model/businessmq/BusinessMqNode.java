package businessmq.db.model.businessmq;


import businessmq.db.model.BaseModel;

/**
 * Created by alan.zheng on 2017/2/15.
 */
public class BusinessMqNode extends BaseModel {
    /**
     * id
     */
    private Long id;
    /**
     * 节点
     */
    private Integer node;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

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
