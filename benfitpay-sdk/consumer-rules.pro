# ============================
# Keep all SDK classes
# ============================
-keep class company.tap.tapcardformkit.** { *; }
-keep class company.tap.tapbenefitpay.** { *; }
-keep class company.tap.tapnetworkkit.** { *; }
-keep class com.tap.** { *; }
-keep class com.tap.commondatamodels.** { *; }
-keep class com.github.tap.payments.benefitpay.** { *; }

# ============================
# Gson serialization / Retrofit models
# ============================
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.google.gson.**

# ============================
# Kotlin & AndroidX
# ============================
-keep class kotlin.** { *; }
-dontwarn kotlin.**

-keep class androidx.** { *; }
-dontwarn androidx.**

# ============================
# WebView JS Interfaces
# ============================
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ============================
# Serializable support
# ============================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object readResolve();
}
