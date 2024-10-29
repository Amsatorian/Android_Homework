package com.example.a114124;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        items = new ArrayList<>();

        // 初始化数据
        for (int i = 1; i <= 10; i++) {
            items.add("Item " + i);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
        listView.setAdapter(adapter);

        // 设置 ListView 的多选模式
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        // 设置 ActionMode 回调
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private final ArrayList<Integer> selectedItems = new ArrayList<>();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selectedItems.add(position);
                } else {
                    selectedItems.remove((Integer) position);
                }
                mode.setTitle(selectedItems.size() + " selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    // 将 selectedItems 按降序排序
                    Collections.sort(selectedItems, Collections.reverseOrder());

                    // 删除选中的项目
                    for (int index : selectedItems) {
                        items.remove(index);
                    }

                    adapter.notifyDataSetChanged();
                    mode.finish(); // 关闭 ActionMode
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectedItems.clear();
            }
        });
    }
}
