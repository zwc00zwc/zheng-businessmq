package businessmq.db.model;

/**
 * Created by Administrator on 2017/1/18.
 */
public class QueryPageModel extends BaseModel {
    /**
     * 查询起始条数
     */
    private Integer startRow;
    /**
     * 查询结束条数
     */
    private Integer endRow;
    /**
     * 当前页
     */
    private Integer currPage=1;
    /**
     * 页大小
     */
    private Integer pageSize=10;

    public Integer getStartRow() {
        return (currPage-1)*pageSize;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return currPage*pageSize;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
