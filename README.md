# Freeze-Dry Tray Calculator (Android - sideload APK)

What it does:
- 5 trays per batch
- Enter Start and End weight (grams) for each tray
- Calculates totals + water removed (g and %)
- Simple rehydration estimate: water to add back ~= water removed (1g ~= 1mL)
- Portioning:
  - Choose dry grams per portion -> number of portions
  - Choose number of portions -> dry per portion + water per portion

## Build APK (Debug)
1) Install Android Studio
2) Open this folder as a project
3) Let Gradle sync
4) Build -> Build Bundle(s) / APK(s) -> Build APK(s)
5) Locate the APK:
   app/build/outputs/apk/debug/app-debug.apk

## Install (Option C / sideload)
- Copy APK to phone (Downloads)
- Allow "Install unknown apps" for Files/Chrome
- Tap the APK -> Install

(USB/ADB also works: adb install -r app-debug.apk)
