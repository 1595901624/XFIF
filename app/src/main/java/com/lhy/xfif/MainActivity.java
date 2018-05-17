package com.lhy.xfif;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwitchCompat switchCompat;
    private TextView scoreTipLeftTextView;
    private TextView scoreTipTextView;
    private EditText scoreEditText;
    private Button okButton;

    private boolean isOpen;
    private int score;
    private Config config;
    private ConfigPreferences configPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        intiView();
        //初始化配置文件
        configPreferences = ConfigPreferences.getInstance();
        config = configPreferences.getDefaultConfig();
        //ANDROID 6.0以上申请权限
        requestCurrentPermissions();

        //获取状态
        isOpen = config.isOpen();
        score = config.getScore();
        //判断是否开启修改分数
        if (isOpen) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
        modifyEnable(isOpen);

        //设置switch状态
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                modifyEnable(b);
            }
        });

        //按钮点击修改分数
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = scoreEditText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    if (Integer.parseInt(s) > 100) {
                        // TODO: 2018-04-26-0026 提示
                    }
                    //保存文件
                    configPreferences.putDefaultConfig("score", Integer.parseInt(s));
                    //修改页面显示
                    scoreTipTextView.setText(s);
                } else {
                    Toast.makeText(MainActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 申请读写内存卡权限
     */
    private void requestCurrentPermissions() {
        //申请权限
        List<PermissionItem> permissionItemList = new ArrayList<>();
        permissionItemList.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取内存卡", R.drawable.permission_ic_storage));
        permissionItemList.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入内存卡", R.drawable.permission_ic_storage));
        HiPermission.create(this)
                .permissions(permissionItemList)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }

    /**
     * 修改状态
     */
    private void modifyEnable(boolean isOpen) {
        if (!isOpen) {
            //设置不可用
            scoreTipLeftTextView.setText("当前状态不可用");
            scoreTipTextView.setText("");
            scoreEditText.setEnabled(false);
            okButton.setEnabled(false);
            configPreferences.putDefaultConfig("open", false);
        } else {
            scoreTipLeftTextView.setText("当前分数已被修改为：");
            scoreTipTextView.setText(configPreferences.getDefaultConfig().getScore() + "");
            scoreEditText.setEnabled(true);
            okButton.setEnabled(true);
            configPreferences.putDefaultConfig("open", true);
        }
    }

    public void dialog(String title, String text) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.show();
    }

    /**
     * 初始化视图
     */
    private void intiView() {
        toolbar = findViewById(R.id.toolbar);
        switchCompat = findViewById(R.id.switch_compat);
        scoreEditText = findViewById(R.id.score_text);
        scoreTipTextView = findViewById(R.id.score_tip);
        okButton = findViewById(R.id.ok_btn);
        scoreTipLeftTextView = findViewById(R.id.score_tip_left);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_action_update:
                dialog("更新日志", getString(R.string.update_log));
                return true;
            case R.id.toolbar_action_about:
                dialog("关于", getString(R.string.about));
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}