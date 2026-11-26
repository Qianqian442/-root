# OPPO A55 5G 临时 Root 管理器（Shark 适配分支）

## 目标设备
- 设备：OPPO A55 5G
- 型号标识：PEMM00
- 系统版本：PEMM00_11_C.17
- Android：12
- 已知内核：4.14.186（仅版本号；完整 `uname -r` 尚未取得）
- SoC：公开设备资料指向 MediaTek Dimensity 700 / MT6833，仍需从本机属性确认
- Bootloader：不解锁
- 使用方式：仅手机操作，优先使用 Android 无线调试 / Shizuku 类授权后端

## 兼容性结论（当前）
Shark 上游公开 README 的主要执行链为 6.6、6.12、6.18 系列，没有 4.14.186 的 OPPO A55 5G 配置。本设备当前状态为 **未验证 / 不支持执行**。

### 4.14 临时 Root 开源候选
- [EV-Toolkit/GhostLock](https://github.com/EV-Toolkit/GhostLock)：公开 GhostLock 实现及按完整 `uname -r` 匹配的内核 profile 机制。
- [R0rtiz2/GhostLock 4.14 branch index](https://github.com/R0rtiz2/GhostLock)：社区索引提到其 4.14 默认分支，但公开目标主要是 Amazon Fire / TCL T653T02 等设备；没有找到 PEMM00 / MT6833 / `PEMM00_11_C.17` 的已验证记录。
- 因此只能将 GhostLock 作为 4.14 研究候选登记，**不能把其他设备的 4.14 profile、地址或二进制直接打包给本机使用**。不同厂商的 4.14 内核即使版本号相同，内存布局、符号、缓解措施和驱动也可能不同。
- GhostLock 的 profile porting 文档要求精确匹配完整内核版本并为目标内核单独生成配置；MediaTek 设备还可能需要额外的物理地址信息。仅凭 `4.14.186` 无法生成安全可用 profile。

## 下一步设备取证（不需要电脑）
通过本机无线调试配对的 Shizuku shell / 本地 ADB shell 读取以下信息：
```sh
uname -a
getprop ro.product.model
getprop ro.product.device
getprop ro.build.fingerprint
getprop ro.build.version.security_patch
getprop ro.board.platform
getprop ro.hardware
getprop ro.boot.hardware
getconf PAGE_SIZE
```
请保留完整输出。不要运行来历不明的 exploit 或 profile。

## 开发顺序
1. 中文化界面与设备信息采集。
2. 显示型号、SoC/ABI、Android 版本、完整内核版本、构建指纹、安全补丁日期和页大小。
3. 精确匹配 profile；没有匹配时明确显示“不支持”，并阻止执行。
4. 接入可审计的无线调试 / Shizuku 授权后端与 Root 管理器状态检测。
5. 只有在存在合法、公开且针对 PEMM00_11_C.17 验证的适配方案时，才评估集成 GhostLock 或其他执行链。
6. 在实体 OPPO A55 5G 上完成稳定性验证后才发布 APK。

## 安全边界
- 不解锁 Bootloader，不刷写 boot/vendor_boot/recovery，不修改启动分区。
- 不包含未验证的内核提权载荷，不将其他机型的 exploit 强行移植到 4.14.186。
- Root 结果必须以实际权限验证为准，不以界面提示代替。
- 临时 Root 应在完整重启后失效；不实现隐蔽持久化。
- 执行内核级测试前备份重要数据；未验证的竞态利用可能导致内核崩溃或自动重启。

## 上游与许可
基础项目： https://github.com/kpgc10kai/Shark
4.14 研究候选： https://github.com/EV-Toolkit/GhostLock
Shark / GhostLock 的许可证、第三方组件许可证和版权声明必须分别核验；不得把不同项目的许可证混为一谈。集成或修改时保留许可证、版权声明及第三方 NOTICE，并遵守对应组件的独立许可证。

## 当前状态
- [x] 确认目标机型与限制
- [x] 取得系统版本号与内核主版本号
- [x] 找到 4.14 GhostLock 开源研究候选
- [x] 确认候选项目没有 PEMM00 的已验证支持记录
- [ ] 获取设备完整内核版本、安全补丁日期、构建指纹、平台与页大小
- [ ] 完成设备检测页与中文界面
- [ ] 完成无线调试 / 授权后端兼容性测试
- [ ] 找到并验证适用于该设备固件的安全适配方案
- [ ] 构建并在实体 OPPO A55 5G 上测试 APK

**注意：当前仓库仍是适配说明文档，尚未包含 Shark 应用源代码、可用 Root 执行链或可安装 APK。**
