# Legacy Linux 4.14 temporary-root integration

## Goal

Add a separate legacy-kernel adapter track for Linux 4.14 devices. KernelSU remains a root-management backend; it is not treated as the privilege-escalation method.

## Candidates found

| Upstream | Target | Reported kernel | Current assessment |
|---|---|---:|---|
| https://github.com/rubnet/ghostlock-RMX3031 | realme GT Neo RMX3031 / MediaTek MT6893 | 4.14.186+ | Experimental, stage-1 only; not an OPPO A55 profile |
| https://github.com/R0rt1z2/GhostLock | Fire TV Stick 4K Max 1st Gen / kara | 4.14 | Device-specific; not transferable to phones |

The RMX3031 project is the closest kernel-version lead because it names 4.14.186+, but its SoC is MT6893. The target phone is OPPO A55 5G PEMM00 with Dimensity 700 MT6833. Same kernel version does not imply matching kernel layout, offsets, vendor configuration, or exploit reliability.

## Target profile

- Device: OPPO A55 5G
- Device code: PEMM00
- SoC: MediaTek Dimensity 700 / MT6833
- Android: 12
- Kernel: 4.14.186
- Bootloader unlock: not allowed
- Computer: not available
- Compatibility: UNVERIFIED

## Integration gates

1. Confirm the upstream project has a usable license or obtain explicit permission. A public repository without a license is not automatically reusable as open-source code.
2. Audit source provenance, dependencies, and build scripts before importing code.
3. Keep device profiles separate. Never reuse RMX3031 MT6893 offsets on PEMM00 MT6833.
4. Require exact device code, full kernel release, build fingerprint, architecture, page size, and vendor patch state before marking a profile supported.
5. Keep mismatched profiles blocked. Do not run an exploit merely because the kernel string starts with 4.14.
6. Test only on the owner's device with recovery options and data backup; kernel exploit failures can crash or reboot the phone.

## Current implementation status

- The app detects and displays kernel information.
- The adapter catalogue now records the two legacy 4.14 candidates and the PEMM00 target profile.
- The UI explains that these candidates are not verified for PEMM00 and does not expose a run action for them.
- No 4.14 exploit payload is bundled or executed yet.
- A usable, legally reusable, device-matched PEMM00/MT6833 implementation has not been found or validated.
