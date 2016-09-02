package home.jereh.com.gridviewtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private GridView gd;
    private MyAdapter mAdapter;
    private ArrayList<Person> persons;
    private Button bt_selectall;
    private Button bt_cancel;
    private Button bt_deselectall;
    private Button bt_sumit;
    private int checkNum; // 记录选中的条目数量
    private TextView tv_show;// 用于显示选中的条目数量
    List<String> listItemID = new ArrayList<String>();
    //HashMap<Integer,String> listItemID=new HashMap<Integer,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* 实例化各个控件 */
        gd = (GridView) findViewById(R.id.gd);
        bt_selectall = (Button) findViewById(R.id.bt_selectall);
        bt_cancel = (Button) findViewById(R.id.bt_cancelselectall);
        bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
        bt_sumit=(Button) findViewById(R.id.bt_submit);
        tv_show = (TextView) findViewById(R.id.tv);
        persons = new ArrayList<Person>();

        // 为Adapter准备数据
        initPersonData();

        // 实例化自定义的MyAdapter
        mAdapter = new MyAdapter(persons, this);
        // 绑定Adapter
        gd.setAdapter(mAdapter);

        // 全选按钮的回调接口
        bt_selectall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < persons.size(); i++) {
                    MyAdapter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                checkNum = persons.size();
                // 刷新listview和TextView的显示
                dataChanged();

            }
        });
        // 取消按钮的回调接口
        bt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < persons.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        checkNum--;// 数量减1
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();

            }
        });

        // 反选按钮的回调接口
        bt_deselectall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的设为未选，未选的设为已选
                for (int i = 0; i < persons.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        checkNum--;
                    } else {
                        MyAdapter.getIsSelected().put(i, true);
                        checkNum++;
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });
        bt_sumit.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                listItemID.clear();
                for(int i=0;i< mAdapter.getIsSelected().size();i++){
                    if( mAdapter.getIsSelected().get(i)){
                        listItemID.add(persons.get(i).getId());

                    }
                }

                if(listItemID.size()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("没有选中任何记录");
                    builder1.show();
                }else{
                    StringBuilder sb = new StringBuilder();

                    for(int i=0;i<listItemID.size();i++){

                        sb.append("ID="+listItemID.get(i)+"  ");

                    }
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setMessage(sb.toString());
                    builder2.show();
                }
            }

        });
        // 绑定listView的监听器
        gd.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                MyAdapter.getIsSelected().put(arg2, holder.cb.isChecked());
                // 调整选定条目
                if (holder.cb.isChecked() == true) {
                    checkNum++;
                } else {
                    checkNum--;
                }
                // 用TextView显示
                tv_show.setText("已选中"+checkNum+"项");

            }
        });
    }
    // 初始化数据
    /**
     * 模拟数据
     */
    private void initPersonData(){
        Person mPerson;
        for(int i=1;i<=40;i++){
            mPerson = new Person();
            mPerson.setName("Andy"+i);
            mPerson.setId(Character.valueOf((char)(i+65))+" ");
            persons.add(mPerson);
        }
    }

    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        mAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        tv_show.setText("已选中" + checkNum + "项");
    }
}
