# JOS VPN — Android Studio starter

This is an original Jetpack Compose Android VPN app starter with a dashboard, Android VPN permission flow, a `VpnService` entry point, and a GitHub Actions APK workflow.

## Important status

**This is not yet a functioning VPN tunnel.** It deliberately does not create a fake TUN interface or claim that traffic is protected. A real VPN requires a maintained tunnel engine (recommended: the official WireGuard Android tunnel implementation), a valid WireGuard client profile, and a reachable server you own or are authorized to use. Those cannot safely be guessed or fabricated.

## Open and build

1. Install Android Studio with JDK 17 and Android SDK Platform 35.
2. Extract/open this folder in Android Studio.
3. Allow Gradle sync to complete.
4. Run the `app` configuration on an Android device or emulator.
5. To build locally, run `gradle assembleDebug` from the project root after Gradle is installed, or use Android Studio **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
6. Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions

1. Create a GitHub repository named `JOSVPN`.
2. Upload all files and folders from this project, including `.github/workflows/build-apk.yml`.
3. Open **Actions → Build JOS VPN APK → Run workflow**.
4. Open the completed run and download the `JOSVPN-debug-apk` artifact.

The workflow uses Gradle installed on the runner, so this starter does not depend on a missing `gradle-wrapper.jar`. For a conventional Android Studio project, you may later generate and commit the Gradle wrapper files (`gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar`).

## Next steps to enable real VPN traffic

1. Deploy WireGuard on a Linux VPS you control.
2. Generate server and client keys on trusted systems. Never commit private keys to GitHub.
3. Integrate the maintained WireGuard Android tunnel library and its required native components according to its upstream documentation and license.
4. Import client profiles securely; don't embed private keys in source code.
5. Implement connection lifecycle, routing, DNS, IPv4/IPv6 handling, network changes, and error reporting.
6. Test with traffic-leak tests and confirm the public IP changes before claiming protection.

## Safety and privacy

- Do not connect to endpoints you do not own or have permission to use.
- Keep secrets out of source control and build logs.
- This starter collects no browsing history and has no backend.
- The UI intentionally labels the tunnel as unconfigured.
