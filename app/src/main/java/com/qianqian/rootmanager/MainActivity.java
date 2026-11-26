package com.qianqian.rootmanager;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private String readKernel() {
        try {
            Process p = new ProcessBuilder("uname", "-a").redirectErrorStream(true).start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = r.readLine();
            p.waitFor();
            return line == null ? Build.VERSION.RELEASE : line;
        } catch (Exception e) {
            return "读取失败：" + e.getClass().getSimpleName();
        }
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        int pad = (int)(20 * getResources().getDisplayMetrics().density);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(pad, pad, pad, pad);
        page.setBackgroundColor(0xFFF5F6FA);

        TextView title = new TextView(this);
        title.setText("临时 Root 管理器");
        title.setTextSize(25);
        title.setTextColor(0xFF172033);
        title.setGravity(Gravity.CENTER_VERTICAL);
        page.addView(title);

        TextView status = new TextView(this);
        status.setText("\n当前状态：尚未取得 Root\n\n此版本为设备检测与适配框架预览，不会执行未验证的漏洞或修改启动分区。");
        status.setTextSize(16);
        status.setTextColor(0xFF9A5B00);
        page.addView(status);

        String kernelInfo = readKernel();
        boolean isLegacy414 = kernelInfo.contains("4.14");

        TextView device = new TextView(this);
        device.setText("\n设备信息\n型号：" + Build.MODEL
                + "\n设备代号：" + Build.DEVICE
                + "\n品牌：" + Build.BRAND
                + "\nAndroid：" + Build.VERSION.RELEASE
                + "\nSDK：" + Build.VERSION.SDK_INT
                + "\nABI：" + (Build.SUPPORTED_ABIS.length > 0 ? Build.SUPPORTED_ABIS[0] : "未知")
                + "\n内核：" + kernelInfo);
        device.setTextSize(14);
        device.setTextColor(0xFF263247);
        page.addView(device);

        TextView note = new TextView(this);
        String legacyStatus = isLegacy414
                ? "\n\n旧内核检测：发现 Linux 4.14 系列。已登记 4.14.186 候选项目：Realme GT Neo（MT6893）GhostLock Stage-1、Fire TV kara 专用 GhostLock。它们不是 PEMM00/MT6833 配置，当前不能用于本机。"
                : "";
        note.setText("\n适配原则\n仅在设备型号、完整内核版本、系统指纹和补丁状态均匹配已验证配置时，才会开放对应适配器。KernelSU 管理核心与临时 Root 执行链分离。"
                + legacyStatus
                + "\n\n本机目标：OPPO A55 5G（PEMM00 / MT6833）。目前尚无已验证的本机临时 Root 配置；不会套用其他机型偏移，也不会自动运行不匹配的利用链。");
        note.setTextSize(14);
        note.setTextColor(0xFF46536A);
        page.addView(note);


        TextView adbTitle = new TextView(this);
        adbTitle.setText("\\n无线 ADB 中转");
        adbTitle.setTextSize(20);
        adbTitle.setTextColor(0xFF172033);
        page.addView(adbTitle);

        TextView adbInfo = new TextView(this);
        adbInfo.setText("用于后续临时提权适配的通信通道。ADB 配对本身不会授予 Root。请在本机开发者选项中开启无线调试，并使用可信的配对客户端完成配对。配对成功后仍需单独验证 shell 权限与提权能力。");
        adbInfo.setTextSize(14);
        adbInfo.setTextColor(0xFF46536A);
        page.addView(adbInfo);

        android.widget.Button adbSettings = new android.widget.Button(this);
        adbSettings.setText("打开开发者选项");
        adbSettings.setOnClickListener(v -> {
            try {
                startActivity(new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            } catch (Exception e) {
                try {
                    startActivity(new android.content.Intent(android.provider.Settings.ACTION_SETTINGS));
                } catch (Exception ignored) { }
            }
        });
        page.addView(adbSettings);

        TextView adbStatus = new TextView(this);
        adbStatus.setText("\\nADB 状态：尚未建立配对会话\\n下一步：接入 Shizuku/ADB transport，并在用户授权后执行只读设备诊断。当前构建不会伪称已配对，也不会执行 Root 命令。");
        adbStatus.setTextSize(13);
        adbStatus.setTextColor(0xFF9A5B00);
        page.addView(adbStatus);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(page);
        setContentView(scroll);
    }
}
