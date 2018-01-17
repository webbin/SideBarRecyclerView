package exp.webbin.siderecyclerviewlib;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 *
 * Created by webbin on 2018/1/15.
 */

public abstract class SideBarRecyclerAdapter {
    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);
    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, BaseHeaderData headerData) ;
    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder itemHolder, BaseItemData itemData) ;

}
