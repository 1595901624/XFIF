package com.lhy.xfif;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

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

    //RxPermissions
    private final RxPermissions rxPermissions = new RxPermissions(this);
    private final static String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ANDROID 6.0以上申请权限
        initPermissions();
        //初始化视图
        intiView();
        //初始化配置文件
        configPreferences = ConfigPreferences.getInstance();
        config = configPreferences.getDefaultConfig();

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
    private void initPermissions() {
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
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
            case R.id.toolbar_action_github:
                Uri uri = Uri.parse("https://github.com/1595901624/XFIF");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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