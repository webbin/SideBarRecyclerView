package exp.webbin.siderecyclerviewlib;

/**
 * Created by webbin on 2018/1/15.
 */

public class CommonData {

    public final static int TYPE_HEADER =  101;
    public final static int TYPE_ITEM = 102;


    private int type ;
    private BaseData data;

    public CommonData(int type,BaseData data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaseData getData() {
        return data;
    }

    public void setData(BaseData data) {
        this.data = data;
    }
}
