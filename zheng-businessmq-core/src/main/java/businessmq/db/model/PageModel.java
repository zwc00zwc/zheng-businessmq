package businessmq.db.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class PageModel<T> extends BaseModel {
    private List<T> model;
    private int currpage;
    private int pagecount;
    private int totalcount;
    private int pagesize;

    public PageModel(List<T> _model,int _currpage,int _totalcount,int _pagesize){
        setModel(_model);
        setTotalcount(_totalcount);
        setCurrpage(_currpage);
        setPagesize(_pagesize);
    }

    public List<T> getModel() {
        return model;
    }

    public void setModel(List<T> model) {
        this.model = model;
    }

    public int getCurrpage() {
        return currpage;
    }

    public void setCurrpage(int currpage) {
        this.currpage = currpage;
    }

    public int getPagecount() {
        int tp=totalcount%pagesize;
        if (tp==0){
            return totalcount/pagesize;
        }
        return totalcount/pagesize+1;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
