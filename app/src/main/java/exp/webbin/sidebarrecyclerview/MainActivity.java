package exp.webbin.sidebarrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import exp.webbin.siderecyclerviewlib.BaseHeaderData;
import exp.webbin.siderecyclerviewlib.BaseItemData;
import exp.webbin.siderecyclerviewlib.SideBarRecyclerAdapter;
import exp.webbin.siderecyclerviewlib.SideBarRecyclerView;

public class MainActivity extends AppCompatActivity {

    private SideBarRecyclerView sideBarRecyclerView;
    private HashMap<BaseHeaderData,ArrayList<? extends BaseItemData>> mapData;

    private SideBarRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        sideBarRecyclerView = (SideBarRecyclerView) findViewById(R.id.side_bar_recycler_view);
        generateRecyclerAdapter();
        generateMapData();
        sideBarRecyclerView.setAdapter(recyclerAdapter);
        sideBarRecyclerView.setMapData(mapData);
    }
    private void generateRecyclerAdapter() {
        recyclerAdapter = new SideBarRecyclerAdapter() {
            class HeaderViewHolder extends RecyclerView.ViewHolder{
                TextView header;
                HeaderViewHolder(View itemView) {
                    super(itemView);
                    header = itemView.findViewById(R.id.item_section_header_text);
                }
            }

            class ItemViewHolder extends RecyclerView.ViewHolder{

                TextView name;
                ItemViewHolder(View itemView) {
                    super(itemView);
                    name = itemView.findViewById(R.id.item_list_name);
                }
            }
            @Override
            public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_section_header,parent,false);
                return new HeaderViewHolder(view);
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
                return new ItemViewHolder(view);
            }

            @Override
            public void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, BaseHeaderData headerData) {
                HeaderViewHolder holder = (HeaderViewHolder) headerHolder;
                SimpleHeaderData simpleHeaderData = (SimpleHeaderData) headerData;
                holder.header.setText(simpleHeaderData.getHeaderName());
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder itemHolder, BaseItemData itemData) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) itemHolder;
                SimpleItemData simpleItemData = (SimpleItemData) itemData;
                itemViewHolder.name.setText(simpleItemData.getName());
            }
        };

    }

    private ArrayList<SimpleItemData> generateItemList() {
        ArrayList<SimpleItemData> itemList = new ArrayList<>();
        for (int i=0;i<3;i++){
            SimpleItemData itemData =new SimpleItemData();
            itemData.setName("name + "+i);
            itemList.add(itemData);
        }
        return itemList;
    }

    private void generateMapData() {
        mapData = new HashMap<>();
        char start = 'A';
        for (int i=0;i<26;i++){
            SimpleHeaderData headerData = new SimpleHeaderData();
            headerData.setHeaderName(String.valueOf(start));
            mapData.put(headerData,generateItemList());
            start++;
        }
    }

    private class SimpleHeaderData extends BaseHeaderData{
        // any fields you want
    }

    private class SimpleItemData extends BaseItemData{
        String name;
        // other fields you want
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
