# Wireless ADB bridge implementation status

## Added in this commit

- The Android app home screen now has a 无线 ADB 中转 section.
- It provides a button that opens Android Developer Options (with a Settings fallback).
- The screen explains that wireless ADB pairing is a shell transport, not root.
- The app explicitly reports that no ADB session is paired yet and does not claim root access.

## Device-side pairing requirements

On Android 11/12, the user must enable Developer options and Wireless debugging, then pair using a trusted ADB pairing client. The pairing code and pairing endpoint are short-lived. The app must never collect or transmit the pairing code to a remote server.

## Remaining engineering work (not yet implemented)

1. Add a maintained ADB client/transport implementation suitable for an Android app, with license and provenance review.
2. Implement pairing service discovery and the ADB TLS pairing protocol; do not treat a successful TCP connection as a paired session.
3. Persist only the local pairing key material using Android Keystore-backed storage; provide disconnect and key-reset controls.
4. Expose a restricted diagnostic API first (getprop, uname, id, SELinux state); do not expose an unrestricted command terminal by default.
5. Add optional Shizuku integration as a separate transport/provider. Shizuku authorization is not root and must be labeled as shell-level access.
6. Only pass verified device facts to a device-specific temporary-root adapter. Fail closed when the exact model, firmware fingerprint, kernel build, ABI, and security patch do not match.
7. Verify actual privilege transitions independently (UID, capabilities, SELinux domain); never infer root from ADB pairing.

## Security constraints

- No bootloader unlock, boot image flashing, or preloader writing.
- Pairing code and private keys remain on-device.
- Require explicit user confirmation before executing any diagnostic or privileged action.
- ADB shell (typically UID 2000) is not UID 0.
- Reboot/disconnect must invalidate the temporary ADB session.
- No exploit payload is enabled by this UI change.

## Build status

This source change alone does not prove the app builds or that ADB pairing works on a phone. The Android CI result and a real Android 12 device pairing test must be checked separately.
