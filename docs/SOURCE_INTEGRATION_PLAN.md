# Root Manager Source Integration Plan

This repository is the integration workspace for a Chinese-language Android temporary-root manager.

## 1. KernelSU base

- Upstream: https://github.com/tiann/KernelSU
- Purpose: root authorization manager, userspace daemon and kernel-side integration.
- License boundary: `kernel/` is GPL-2.0-only; the remaining project components (except separately licensed files) are GPL-3.0-or-later. Preserve upstream notices and each file's license.
- Important compatibility note: KernelSU supports older kernels from 4.14, but those kernels require a device kernel build/integration; this does not itself provide a locked-bootloader temporary-root entry point.
- Integration status: source import not yet performed. Do not label this repository as containing the complete KernelSU source until the upstream tree is actually imported and verified.

## 2. Temporary-root loader catalogue

The following projects are candidates for a modular adapter catalogue, not a promise that every device is supported. Each adapter must retain its upstream license, notices, provenance, supported-build table and hardware-test status.

| Project | Public source / index | Scope / integration note |
|---|---|---|
| Shark | https://github.com/kpgc10kai/Shark | Selected OnePlus, OPPO, Realme, vivo/iQOO and Xiaomi chains; inspect bundled component licenses before redistribution. |
| GhostLock | https://github.com/EV-Toolkit/GhostLock | Device/kernel-profile based; exact full kernel release and device profile required. |
| DirtyFrag | https://github.com/evdenis/DirtyFrag | Temporary in-memory root research for specific vulnerable builds; exact target matching required. |
| Root My Galaxy | https://github.com/fynks/awesome-temporary-root | Samsung-specific loader family; use upstream project links and live firmware tables. |
| Root My Galaxy Next / Ultra / Extended | https://github.com/fynks/awesome-temporary-root | Separate Samsung forks/variants; do not combine payloads without checking provenance and license. |
| GhostSam | https://github.com/fynks/awesome-temporary-root | Samsung-focused DirtyFrag/GhostLock integrations. |
| RootMyVivo | https://github.com/fynks/awesome-temporary-root | vivo/iQOO-specific loader; verify repository license and exact device matrix. |
| DFRoot | https://github.com/fynks/awesome-temporary-root | DirtyFrag-based loader; check its own license and target requirements. |
| TimerRoot / Pixel Timer | https://github.com/fynks/awesome-temporary-root | Timer-exploit research and device-specific profiles; do not generalize Pixel profiles to other vendors. |
| Root My Device (Witaqua / tqmane) | https://github.com/fynks/awesome-temporary-root | Separate device-targeted apps; inspect each repository's license independently. |
| GhostLock OnePlus ports | https://github.com/fynks/awesome-temporary-root | OnePlus/OPPO/Xiaomi device-specific ports; some sources may lack a project-wide redistribution license. |
| ANB Root | https://github.com/fynks/awesome-temporary-root | HONOR-specific integration; verify supported build and license. |
| RootMyS24 / SamTimer / GhostKey | https://github.com/fynks/awesome-temporary-root | Samsung-specific experiments and variants; verify each source and payload provenance independently. |
| Device-specific ports (Motorola, ASUS, Meizu, Xiaomi/Redmi, vivo/iQOO, HONOR, Nothing, Meta Quest, Fire TV, etc.) | https://github.com/fynks/awesome-temporary-root | Discover from the maintained index, then add only after license and exact-device support review. |

Catalogue index:
- https://github.com/fynks/awesome-temporary-root
- https://github.com/awesome-android-root/awesome-android-root

## 3. App architecture

- Chinese UI and a device-information screen.
- Read-only device detection: model, product/device codename, SoC/platform, Android build fingerprint, security patch, full `uname -r`, ABI and page size.
- Adapter registry: each adapter declares upstream URL, license, supported exact builds, required privilege channel (Shizuku/ADB/etc.), payload hashes, and test status.
- Compatibility gate: refuse to run when the exact target is absent or mismatched; never fall back to a profile from another phone.
- KernelSU manager integration is separate from exploit/loader adapters.
- Root verification must test actual effective UID/capabilities; a successful loader message alone is not proof.
- Full reboot should clear an in-memory temporary-root session where the upstream method specifies that behavior.

## 4. OPPO A55 5G target

Known user-provided target:
- Model: OPPO A55 5G (PEMM00)
- Android: 12
- Kernel version reported: 4.14.186
- Bootloader: must remain locked
- Operation: phone-only; no PC

This is not enough to identify a compatible exploit profile. Before enabling any adapter, collect the full `uname -a`, build fingerprint, security patch, board platform, hardware properties and page size. No PEMM00 profile is considered supported until the exact firmware/kernel combination has a documented, repeatable hardware test.

## 5. Safety, licensing and release gates

- Do not redistribute source or binary payloads unless the specific upstream license permits it.
- Keep GPL-2.0-only KernelSU kernel files and GPL-3.0-or-later userspace components' notices and source obligations intact.
- Do not bundle unknown binaries, downloaded payloads without hashes, or repositories with unclear redistribution rights.
- Do not flash boot, vendor_boot or recovery, unlock the bootloader, or modify boot partitions for this project's locked-bootloader mode.
- No release may claim a device is supported based only on a matching Android version or kernel major/minor version.
- Build and test each adapter independently; a failed adapter must not block the app or other adapters.
