# ColorPickerView
A color picker view for Android
Android颜色选择器

[![Release](https://jitpack.io/v/daemon369/ColorPickerView.svg)](https://jitpack.io/#daemon369/ColorPickerView)

----

# 使用方法

## 1. 项目依赖

项目根目录下`build.gradle`中加入：
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
添加依赖：
```
dependencies {
    implementation 'com.github.daemon369:ColorPickerView:v0.0.6'
}
```

## 2. 使用

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ff00aa00"
    android:clipChildren="false"
    android:gravity="center"
    android:orientation="vertical">

    <me.daemon.colorpicker.ColorPickerView
        android:id="@+id/color_picker"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:background="@android:color/white"
        app:disabledColor="@android:color/darker_gray"
        app:disallowInterceptTouchEvent="true"
        app:initialColor="@android:color/white"
        app:palettePadding="10dp" />

</LinearLayout>
```