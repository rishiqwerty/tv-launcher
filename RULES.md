# Coding Rules and Engineering Standards — Glass TV Launcher

These rules must be followed across all development phases of the Glass TV Launcher project.

---

## 1. Platform & API Restrictions

1. **Strictly Public Android APIs Only:**
   - Never use private, hidden (`@hide`), or undocumented Android APIs.
   - Never use reflection to bypass framework restrictions.
2. **Zero Root / Privileged Workarounds:**
   - Do not require root access, device-owner mode, or accessibility-service hacks.
   - If a feature (e.g., Google TV internal recommendation row or proprietary HDMI switching) cannot be achieved via standard public APIs, document the limitation and omit it or provide a safe fallback.
3. **Permission Discipline:**
   - Declare only necessary permissions.
   - `QUERY_ALL_PACKAGES` is permitted and required solely for launcher app discovery.
   - Do not request `ACCESS_FINE_LOCATION`, `RECORD_AUDIO`, `READ_CONTACTS`, `CAMERA`, or external storage permissions.
4. **Privacy & Offline By Default:**
   - Core launcher functionality must operate 100% offline.
   - No tracking SDKs, analytics, ads, or unsolicited background network calls.

---

## 2. Remote-First UI & Navigation Conventions

1. **No Touch Interaction Assumptions:**
   - The primary input device is a D-pad remote controller (Up, Down, Left, Right, Enter/Select, Back).
   - Every interactive element must be explicitly focusable and reachable via remote.
2. **Focus Must Be Immediately Visible:**
   - Focus must never rely on subtle color changes alone.
   - Use an unmistakable focus indicator: a 1.05x–1.08x scale animation, high-contrast border ring, and elevated depth.
3. **Deterministic Back Button Behavior:**
   - Back button must dismiss overlays, sub-menus, or navigate back to the home row.
   - On the top-level Home row, pressing Back must restore focus to the first item and never crash, exit to a black screen, or terminate the launcher process.
4. **TV Safe Areas & Overscan:**
   - All interactive elements and text must reside within the TV safe display zone (minimum 5% margin on all sides).
5. **Reduced Motion Respect:**
   - When the user enables "Reduced Motion" or the system has animations disabled, scale jumps and transition durations must collapse to instantaneous or minimal fades.

---

## 3. Architecture & Code Conventions

1. **Unidirectional Data Flow (UDF):**
   - ViewModels expose immutable `StateFlow<UiState>`.
   - UI emits user actions/events to ViewModels.
   - No direct mutable state leaks from data/domain layers to the UI.
2. **Asynchronous & Non-Blocking Execution:**
   - `PackageManager` queries, image decoding, and DataStore reads/writes must occur on background dispatchers (`Dispatchers.IO` / `Dispatchers.Default`).
   - The main thread must never be blocked during cold start or app transitions.
3. **No Heavy or Redundant Dependencies:**
   - Do not introduce large cross-platform libraries, bulky DI frameworks (like Dagger/Hilt unless strictly needed), or complex network stacks.
   - Use standard Android Jetpack libraries (`androidx.tv`, `androidx.lifecycle`, `androidx.datastore`).
4. **Resilience to Package Changes & Edge Cases:**
   - App discovery must gracefully handle apps without TV banners (fallback to app icon or generated initials tile).
   - Uninstalled apps must not crash the launcher; favorites lists must reconcile gracefully.
   - Very long app labels must truncate neatly with ellipsis without distorting tile dimensions.

---

## 4. Verification & Testing Standards

1. **Build Verification:**
   - Every meaningful code change must be verified with `./gradlew assembleDebug` or `./gradlew test`.
   - Never claim a build or test passes without executing it and verifying the exit code.
2. **Unit Tests:**
   - Unit tests are required for domain logic: sorting algorithms, favorites reconciliation, and preference serialization.
3. **Hardware & D-pad QA Checklist:**
   - Test directly on Android TV / Google TV hardware or emulator using remote key events (`adb shell input keyevent ...`).
4. **Documentation Updates:**
   - Update `CHANGELOG.md` with every meaningful functional change.
   - Update `ARCHITECTURE.md` and `README.md` if architectural patterns or user flows evolve.
