package businessmq.config;

/**
 * Created by alan.zheng on 2017/2/14.
 */
public enum MessageType {
    /**
     * mysql
     */
    MYSQL("START"),
    /**
     * mongodb
     */
    MONGO("MONGO"),
    ;
    private String type;

    private MessageType(String _command){
        type=_command;
    }

    public String getType() {
        return type;
    }
}
