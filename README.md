

# SideBarRecyclerView
This is a view that contains a list and a side bar using RecyclerView on Android.

这是一个有侧边栏的列表，列表中的数据是分组的，每个组都有一个section（可以说是一个组名），侧边栏放置了所有的section，点击侧边栏上的某一项，列表就会自动滚动到对应section的位置。（常见于联系人列表界面）
## 用法
- 在布局文件中使用exp.webbin.siderecyclerviewlib.SideBarRecyclerView 这个类：

```xml
    <exp.webbin.siderecyclerviewlib.SideBarRecyclerView
        android:id="@+id/side_bar_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </exp.webbin.siderecyclerviewlib.SideBarRecyclerView>

```

- 建立对应的类，section为一个类，item为一个类，基类是固定的：

```Java
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
```

- 重写recyclerView需要用到的adapter，这里需要继承一个SideBarRecyclerAdapter ：

```
        recyclerAdapter = new SideBarRecyclerAdapter() {
        // create a class for the header view holder 
            class HeaderViewHolder extends RecyclerView.ViewHolder{
                TextView header;
                HeaderViewHolder(View itemView) {
                    super(itemView);
                    header = itemView.findViewById(R.id.item_section_header_text);
                }
            }

        // create a class for the item view holder
            class ItemViewHolder extends RecyclerView.ViewHolder{

                TextView name;
                ItemViewHolder(View itemView) {
                    super(itemView);
                    name = itemView.findViewById(R.id.item_list_name);
                }
            }
            @Override
            public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            // use your header layout file
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_section_header,parent,false);
                return new HeaderViewHolder(view);
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
            // use your item layout file
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
                return new ItemViewHolder(view);
            }

            @Override
            public void onBindHeaderViewHolder(RecyclerView.ViewHolder headerHolder, BaseHeaderData headerData) {
                HeaderViewHolder holder = (HeaderViewHolder) headerHolder;
                // cast the data to what you have passed in
                SimpleHeaderData simpleHeaderData = (SimpleHeaderData) headerData;
                holder.header.setText(simpleHeaderData.getHeaderName());
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder itemHolder, BaseItemData itemData) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) itemHolder;
                // cast the data to what you have passed in
                SimpleItemData simpleItemData = (SimpleItemData) itemData;
                itemViewHolder.name.setText(simpleItemData.getName());
            }
        };

```

- 将你的数据填充到指定的结构中，这里需要一个HashMap<BaseHeaderData,ArrayList<? extends BaseItemData>>的对象，由于前面你已经定义了自己的bean，这里直接可以用


```java
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
            // put your own header and item object
            mapData.put(headerData,generateItemList());
            start++;
        }
    }
```

