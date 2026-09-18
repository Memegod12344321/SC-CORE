# SC-CORE Android Local Test Client

Standalone Android test client for the local SC-CORE protocol.

It sends the local Hello packet (10100) to `127.0.0.1:9339` and verifies the `20100` response.

This project is intentionally separate from the proprietary Hay Day client and is for local SC-CORE development/testing.

## Build

From Termux with Android SDK and Gradle available:

```sh
gradle :app:assembleDebug
```
