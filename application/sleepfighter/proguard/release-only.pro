# RELEASE ONLY PROGUARD SETTINGS.

# Don't want log-cat in release builds.
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}