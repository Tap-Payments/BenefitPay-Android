# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ============================
# Kotlin & AndroidX
# ============================
-keep class kotlin.** { *; }
-keep class androidx.** { *; }
-dontwarn androidx.**
-dontwarn kotlin.**

# ============================
# Gson (Serialization / Reflection)
# ============================
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ============================
# Retrofit interfaces & models
# ============================
-keep interface company.tap.tapnetworkkit.* { *; }    # <-- Replace with your Retrofit interfaces package
-keep class company.tap.tapnetworkkit.** { *; }    # <-- Replace with your models package

# Retrofit & OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# Converter factories
-keep class retrofit2.converter.gson.** { *; }
-keep interface company.tap.tapnetworkkit.api.** { *; }
-keep class company.tap.tapnetworkkit.model.** { *; }
-keepattributes Signature, *Annotation*

# ============================
# Tap Payments SDKs
# ============================
-keep class com.tap.** { *; }
-dontwarn com.tap.**

# ============================
# Miscellaneous / other libraries
# ============================
-keep class com.intuit.sdp.** { *; }
-keep class com.github.Tap-Payments.CommonDataModelsWeb-Andriod.** { *; }

# ============================
# General rule for serialization
# ============================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object readResolve();
}
-keep class com.github.tap.payments.benefitpay.** { *; }
-keep class com.tap.** { *; }
-dontwarn com.tap.**



