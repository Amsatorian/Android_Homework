package com.example.android_expt03;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String[] names = new String[]{"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
    private final int[] imageIds = new int[]{R.drawable.lion, R.drawable.tiger, R.drawable.monkey, R.drawable.dog, R.drawable.cat, R.drawable.elephant};
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("header", imageIds[i]);
            listItem.put("animalName", names[i]);
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.simple_item,
                new String[]{"animalName", "header"}, new int[]{R.id.name, R.id.header});
        ListView list = findViewById(R.id.mylist);
        list.setAdapter(simpleAdapter);

        // 设置点击和长按事件
        list.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("-ZJH-", names[position] + "被单击了");
            Toast toast = Toast.makeText(MainActivity.this, names[position], Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    public void loginView(View source) {
        TableLayout loginForm = (TableLayout) getLayoutInflater().inflate(R.layout.login, null);
        new AlertDialog.Builder(this)
                .setTitle("ANDROID APP")
                .setView(loginForm)
                .setPositiveButton("Sign in", (dialog, which) -> {
                    // 处理登录逻辑
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // 取消操作
                })
                .create()
                .show();
    }

    @SuppressLint("NonConstantResourceId")
    public void showMenu(View view) {
        // 创建一个PopupMenu来显示菜单
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            TextView testText = findViewById(R.id.test_text);
            switch (item.getItemId()) {
                case R.id.font_small:
                    testText.setTextSize(10);
                    Toast.makeText(this, "选择了小字体", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.font_medium:
                    testText.setTextSize(16);
                    Toast.makeText(this, "选择了中字体", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.font_large:
                    testText.setTextSize(20);
                    Toast.makeText(this, "选择了大字体", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_show_toast:
                    Toast.makeText(this, "普通菜单项被点击", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.color_red:
                    testText.setTextColor(ContextCompat.getColor(this, R.color.red));
                    Toast.makeText(this, "选择了红色", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.color_black:
                    testText.setTextColor(ContextCompat.getColor(this, R.color.black));
                    Toast.makeText(this, "选择了黑色", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    public void showContextMenu(View view) {
        ListView listView = findViewById(R.id.listView);
        items = new ArrayList<>();

        // 初始化数据
        for (int i = 1; i <= 20; i++) {
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
                    selectedItems.sort(Collections.reverseOrder());

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
