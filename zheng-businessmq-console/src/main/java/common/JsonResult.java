package common;

/**
 * Created by XR on 2016/8/25.
 */
public class JsonResult {
    private int code;
    private String msg;
    private Object data;

    public JsonResult(){
        this.code=1;
    }

    public JsonResult(Integer _code,String _msg){
        this.code=_code;
        this.msg=_msg;
    }

    public JsonResult(Integer _code,String _msg,Object _data){
        this.code=_code;
        this.msg=_msg;
        this.data=_data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
