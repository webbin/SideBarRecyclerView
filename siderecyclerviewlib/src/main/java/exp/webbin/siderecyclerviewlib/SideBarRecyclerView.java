package exp.webbin.siderecyclerviewlib;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by webbin on 2018/1/12.
 */

public class SideBarRecyclerView extends RelativeLayout {

    private RecyclerView recyclerView;
    private SideBar sideBar;
    private TextView textHint;

    private int sideBarWidth;
    private Adapter mAdapter;
    private SideBarRecyclerAdapter sideBarRecyclerAdapter;
    private String[] sideBarStringData;
    private HashMap<BaseHeaderData, ArrayList<? extends BaseItemData>> mapData;
    private ArrayList<CommonData> mData;
    private SimpleArrayMap<Integer, Integer> headerIndexMap;

    private int hintTextWidth = 100;
    private int sidBarHorizontalPadding = 0;
    private LinearLayoutManager linearLayoutManager;

    private SideBar.TouchSideBarListener innerTouchSideBarListener;

    public SideBarRecyclerView(Context context) {
        super(context);
        init();
    }

    public SideBarRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SideBarRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SideBarRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        recyclerView = new RecyclerView(getContext());
        textHint = new TextView(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        sideBar = new SideBar(getContext());

        textHint.setBackgroundColor(Color.argb(50, 0, 0, 0));
        textHint.setTextSize(80);
//        int padding = dp2px(10);
//        textHint.setPadding(padding,padding,padding,padding);
        textHint.setTextColor(Color.WHITE);
        textHint.setGravity(Gravity.CENTER);
        textHint.setVisibility(INVISIBLE);
        sideBarWidth = dp2px(20);
        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sideBar.setTouchSideBarListener(new SideBar.TouchSideBarListener() {
            @Override
            public void onTouchItem(int index) {
                textHint.setText(sideBarStringData[index]);
                textHint.setVisibility(VISIBLE);
                final int position = findHeaderIndex(index);
                log("find header index = " + position);
//                recyclerView.scrollToPosition(position);
                linearLayoutManager.scrollToPositionWithOffset(position,0);
            }

            @Override
            public void onTouchItemUp(int index) {
                textHint.setVisibility(INVISIBLE);
            }
        });

        headerIndexMap = new SimpleArrayMap<>();
//        String[] letters = new String[26];
//        char letter = 'A';
//        for (int i = 0; i < 26; i++) {
//            letters[i] = String.valueOf(letter);
//            letter += 1;
//        }
//        sideBarStringData = letters;
//        int[] imageIds = new int[20];
//        for (int i = 0; i < 20; i++) {
//            imageIds[i] = R.drawable.ic_drakeet;
//        }
//        sideBar.setDataList(sideBarStringData);
//        sideBar.setDataList(imageIds);

        addView(recyclerView);
        addView(sideBar);
        LayoutParams layoutParams = new LayoutParams(dp2px(hintTextWidth),dp2px(hintTextWidth));
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(textHint,layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
        if (sidBarHorizontalPadding < 0 || sidBarHorizontalPadding > b - t)
            sidBarHorizontalPadding = 0;
        recyclerView.layout(l, t, r, b);
        sideBar.layout(r - sideBarWidth, t + sidBarHorizontalPadding, r, b - sidBarHorizontalPadding);
//        int textHintLeft = (r - hintTextSize) / 2;
//        int textHintTop = b / 2 - hintTextSize / 2;
//        textHint.layout(textHintLeft, textHintTop, textHintLeft + hintTextSize, textHintTop + hintTextSize);
//        textHint.setGravity(Gravity.CENTER);
//        log("on layout, l =" + l + ", t = " + t + ", b = " + b);
    }

    private void log(String log) {
        Log.e(getClass().getSimpleName(), log);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setAdapter(SideBarRecyclerAdapter adapter) {
        sideBarRecyclerAdapter = adapter;
        mAdapter.setController(adapter);
    }

    private int findHeaderIndex(int index) {
        int listIndex = 0;
        listIndex = headerIndexMap.get(index);
        return listIndex;
    }

    private ArrayList<CommonData> resolveMapData(HashMap<BaseHeaderData, ArrayList<? extends BaseItemData>> mapData) {
        ArrayList<CommonData> commonDataList = new ArrayList<>();
        int length = mapData.keySet().size();
        Set<BaseHeaderData> set = mapData.keySet();
        Object[] headers = set.toArray();
        ArrayList<BaseHeaderData> headerList = new ArrayList<>();
        for (Object h : headers) {
            if (h instanceof BaseHeaderData) {
//                log("is base header data");
                BaseHeaderData baseH = (BaseHeaderData) h;
                headerList.add(baseH);
            } else log("is not base header data");
//            log(h.getClass().getSimpleName());
        }
        Collections.sort(headerList, new Comparator<BaseHeaderData>() {
            @Override
            public int compare(BaseHeaderData header1, BaseHeaderData header2) {
                return header1.getHeaderName().compareTo(header2.getHeaderName());
            }
        });
        sideBarStringData = new String[length];
        int index = 0;
        int itemIndex = 0;
        for (BaseHeaderData headerData : headerList) {
            CommonData headerCommonData = new CommonData(CommonData.TYPE_HEADER, headerData);
            commonDataList.add(headerCommonData);
            sideBarStringData[index] = headerData.getHeaderName();
            headerIndexMap.put(index, itemIndex);
            ArrayList<? extends BaseItemData> items = mapData.get(headerData);
            for (BaseItemData itemData : items) {
                CommonData itemCommonData = new CommonData(CommonData.TYPE_ITEM, itemData);
                commonDataList.add(itemCommonData);
                itemIndex++;
            }
            index += 1;
            itemIndex++;
        }
        sideBar.setDataList(sideBarStringData);
        return commonDataList;
    }

    public HashMap<BaseHeaderData, ArrayList<? extends BaseItemData>> getMapData() {
        return mapData;
    }

    public void setMapData(HashMap<BaseHeaderData, ArrayList<? extends BaseItemData>> mapData) {
        this.mapData = mapData;
        mData = resolveMapData(mapData);
        mAdapter.setAdapterData(mData);
    }

    public void setTextHint(TextView textHint) {
        this.textHint = textHint;
    }

    private class Adapter extends RecyclerView.Adapter {

        private SideBarRecyclerAdapter controller;
        private ArrayList<CommonData> adapterData;

        Adapter() {
            adapterData = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (controller == null) return null;
            if (viewType == CommonData.TYPE_HEADER) {
                return controller.onCreateHeaderViewHolder(parent);
            } else if (viewType == CommonData.TYPE_ITEM) {
                return controller.onCreateItemViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                if (controller != null) {
                    CommonData data = adapterData.get(position);
                    if (data.getType() == CommonData.TYPE_HEADER) {
                        controller.onBindHeaderViewHolder(holder, (BaseHeaderData) data.getData());
                    } else if (data.getType() == CommonData.TYPE_ITEM) {
                        controller.onBindItemViewHolder(holder, (BaseItemData) data.getData());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemViewType(int position) {
            return adapterData.get(position).getType();
        }

        @Override
        public int getItemCount() {
            return adapterData.size();
        }

        void setController(SideBarRecyclerAdapter controller) {
            this.controller = controller;
            notifyDataSetChanged();
        }

        public ArrayList<CommonData> getAdapterData() {
            return adapterData;
        }

        public void setAdapterData(ArrayList<CommonData> adapterData) {
            log("set adapter data ,size = " + adapterData.size());
            this.adapterData = adapterData;
            notifyDataSetChanged();
        }
    }
}
