# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-verbose

# For now:
-keep class se.toxbee.** { *; }

# OBFUSCATION: FOSS = NONO.
-dontobfuscate

# OPTIMIZATIONS: Dalvik doesn't like them.
-dontoptimize
-dontpreverify

# PRINT STUFF: getting weird bug...
#-whyareyoukeeping
#-printseeds seeds.txt
#-printusage unused.txt
#-printmapping mapping.txt

# DONT SKIP:
-dontskipnonpubliclibraryclasses

# IGNORE:
-dontwarn javax.swing.**
-dontwarn java.awt.**
-dontwarn java.beans.**

# KEEP: Annotations
-keepattributes *Annotation*

# KEEP: JNI (Native)
-keepclasseswithmembers class * {
    native <methods>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

## --------------- Start ANDROID specifics --------------- ##

# KEEP: methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# KEEP: R class.
-keepattributes InnerClasses
-keep class **.R
-keepclassmembers class **.R$* {
    public static <fields>;
}

# KEEP: the BuildConfig
-keep class com.example.BuildConfig { *; }

# KEEP setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keep public class * extends android.view.View {}
-keep public class * extends android.view.ViewGroup {}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
# Custom view components might be accessed from your layout files
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributeSet);
   public <init>(android.content.Context, android.util.AttributeSet, int);
}

# KEEP: Android intent:able classes.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# KEEP: Android, "serialization"
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# KEEP: Android, more stuff.
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-dontwarn **CompatHoneycomb
-dontwarn **CompatCreatorHoneycombMR2
-dontwarn **AccessibilityServiceInfoCompatJellyBeanMr2

# ANDROID: Support libraries.
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn android.support.v4.view.**
-dontwarn android.support.v4.media.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.app.Fragment {}

## ----------------- End ANDROID specifics --------------- ##
