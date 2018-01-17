package exp.webbin.siderecyclerviewlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by webbin on 2018/1/17.
 */

public class SimpleRecyclerView extends RecyclerView {


    private LinearLayoutManager mLinearLayoutManager;
    public SimpleRecyclerView(Context context) {
        super(context);
        init();
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(mLinearLayoutManager);

    }

    public LinearLayoutManager getmLinearLayoutManager() {
        return mLinearLayoutManager;
    }

    public void setmLinearLayoutManager(LinearLayoutManager mLinearLayoutManager) {
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.e(getClass().getSimpleName(),"request layout");
    }
}
