-keepclassmembers class * {
    static java.lang.String *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class company.tap.tapcardformkit.** { *; }
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
-keep class com.tap.commondatamodels.** { *; }

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