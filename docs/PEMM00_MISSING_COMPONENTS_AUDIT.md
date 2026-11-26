# PEMM00 临时 Root 缺失项完整审计

审计日期：2026-11-26

目标约束：
- OPPO A55 5G，用户报告设备代号 PEMM00
- MediaTek Dimensity 700 / MT6833（公开资料；仍需本机属性核验）
- Android 12
- 用户报告 Linux 4.14.186
- 不解锁 Bootloader、不用电脑、只要当前开机周期临时 Root；完整重启后 Root 应失效

## 结论

当前仓库**不是可用的 Root 工具**，而是设备检测 APK 骨架、适配目录和上游源码。已经导入 KernelSU 与 KernelSU Next Legacy 源码，但尚未把它们集成到 PEMM00 的厂商内核，也没有找到经过验证、匹配 PEMM00/MT6833/4.14.186/当前固件的临时提权执行链。

KernelSU 是 Root 管理/内核后端，不是漏洞利用入口。只导入 KernelSU 源码、安装 Manager APK 或添加一个通用驱动文件，都不能让锁定 Bootloader 的原厂内核自动获得 Root。

## 已有内容

- [x] Android 项目骨架与基础设备信息页面。
- [x] KernelSU 上游源码导入工作流。
- [x] KernelSU Next Legacy 上游源码导入工作流。
- [x] KernelSU Next Legacy 固定到上游 commit `e5f5ec998de88b9fc43c62cff8f7ae12d3f4800a`。
- [x] Legacy 源码导入 GitHub Actions 成功（run 45850999809）。
- [x] 登记 4.14 GhostLock 研究候选，但明确标注 Fire TV/TCL/MT6893 配置不可移植到 PEMM00。
- [x] 兼容性安全门：未精确匹配的设备配置不得执行。

## 缺失项清单

### P0 — 必须先补齐，否则不能判断是否存在可用路径

1. **本机精确设备指纹**
   - 完整 `uname -a` 和 `uname -r`（不能只知道 4.14.186）。
   - `ro.product.model`、`ro.product.device`、`ro.product.name`、`ro.build.fingerprint`。
   - `ro.build.version.incremental`、Android build ID、`ro.build.version.security_patch`。
   - `ro.board.platform`、`ro.hardware`、`ro.boot.hardware`。
   - CPU ABI、`getconf PAGE_SIZE`、SELinux enforcing 状态。
   - 当前系统是否为 PEMM00_11_C.17，及 OTA 后是否变更。
   - 这些数据目前未完整采集；公开型号资料不能代替本机读数。

2. **针对精确内核/固件的临时提权入口**
   - 必须找到公开、可审计、许可允许使用的 loader / 漏洞实现。
   - 上游支持表必须明确匹配 PEMM00（或有可证明等价的精确板级配置）、MT6833、完整内核 release 和固件指纹。
   - 目前未找到这样的已验证 profile。
   - 4.14 GhostLock 候选只说明存在旧内核研究代码；其公开配置是特定 Fire TV/TCL 等设备，不是 MT6833 手机配置。
   - 不得用相同内核版本号、相同 SoC 家族或其他 OPPO 型号推断兼容。

3. **与目标内核 ABI 对应的 KernelSU LKM**
   - 需要与目标内核的架构、配置、符号、页大小、编译器 ABI 和 vendor 修改相符。
   - 目前只有通用上游源码；没有 PEMM00 专用、编译并验证过的 `.ko`。
   - KernelSU 的 late-load 模式只是把已构建的内核模块加载进运行中的内核，不提供提权漏洞本身。

4. **可行的加载权限通道**
   - 必须证明 loader 能在当前锁定启动链下取得所需权限，并能把模块交给内核加载。
   - Shizuku/无线调试只是 shell 授权/传输方式，不等于 Root；也不能代替内核漏洞。
   - 当前 APK 没有 Shizuku SDK/服务集成、无线调试引导流程或实际 loader 通道。

### P1 — 有了精确入口后仍必须实现

5. **KernelSU 用户态组件**
   - 与所选内核后端版本匹配的 Manager、`ksud`/守护进程和通信接口。
   - LKM late-load、守护进程启动、SELinux 兼容和授权 UI 的端到端连接。
   - 当前 app 只有一个检测 Activity，没有 Manager/daemon 接入。

6. **目标设备适配资料**
   - 目标内核源码、准确 defconfig、内核配置、符号信息、vendor patch、模块签名/加载限制。
   - 如果某个 loader 依赖设备专用内存布局/偏移/驱动信息，必须由 PEMM00 自身生成和验证；不能复制其他型号参数。
   - 当前仓库没有 PEMM00 的完整内核源码树或已验证构建配置。

7. **真正的 Root 状态验证**
   - 不能只看 APK 显示“成功”。
   - 必须验证有效 UID、实际 `su` 调用、所需 capability、SELinux 状态及授权应用隔离。
   - 当前 app 没有执行验证，也没有自动区分 shell UID、Shizuku 和 UID 0。

8. **临时会话生命周期**
   - Root daemon / LKM 的启动、失败回滚、重复启动保护、软重启行为和完整重启清理。
   - 完整重启后必须重新回到未 Root 状态；不做隐藏持久化。
   - 当前没有会话管理和状态恢复逻辑。

### P2 — 发布前质量与安全要求

9. 精确机型/固件白名单、payload 哈希、来源证明、版本回滚保护。
10. 对不匹配设备 fail-closed；不能提供“强行兼容”或自动套用近似 profile。
11. 对崩溃、竞态失败、模块加载失败和权限不足提供可读日志与安全退出。
12. 许可审查：KernelSU 内核部分、用户态组件、loader、第三方库和每个 payload 分别核验许可证与再分发义务。
13. 真机测试：PEMM00 原机型、当前固件，验证启动成功率、稳定性、温度/耗电、相机/网络/电话功能和完整重启清除。
14. 正式 APK 签名、版本管理、可复现构建、构建产物校验和发布说明。

## 约束冲突：KernelSU 内核编译安装路线

将 KernelSU 静态集成进厂商内核并重新编译，通常需要设备专用内核源码、构建工具链和可安装自定义内核的启动链。对本项目来说，这条路线不能作为“不解锁 Bootloader、不刷写分区”的交付方案。它只能用于源码研究或另一个明确允许解锁/刷写的分支，不能冒充本项目的临时 Root 路径。

## 下一步优先顺序

1. 先补齐本机只读指纹采集，确认完整固件/内核身份。
2. 用该指纹逐项核对临时 Root 上游支持表；没有精确命中就继续保持禁用。
3. 对候选 loader 只做源码、许可、架构和 profile 审核；不在未匹配设备上试跑。
4. 只有找到合法、明确支持该精确构建的 loader，才实现对应 LKM/管理器/加载通道。
5. 最后才做真机验证和 APK 发布。

## 当前状态总表

| 组件 | 当前状态 |
|---|---|
| Android 检测 APK 骨架 | 已有，功能很少 |
| KernelSU 上游源码 | 已导入 |
| KernelSU Next Legacy 4.14 源码 | 已导入，CI 成功 |
| PEMM00 内核源码/配置 | 缺失 |
| PEMM00 完整设备指纹 | 缺失 |
| PEMM00 专用 KernelSU LKM | 缺失 |
| 精确匹配的临时提权 loader | 未找到 |
| PEMM00 exploit/profile | 未找到/未验证 |
| Shizuku/无线调试通道 | 未集成 |
| KernelSU Manager/daemon 接入 | 未集成 |
| Root 实际验证 | 未实现 |
| 真机测试 | 未进行 |
| 可发布的临时 Root APK | 尚不存在 |

**不要把“源码导入成功”或“APK 编译成功”写成“Root 已支持”。**
