# Changelog

## [1.1.0] - 2019-04-24

move painter implementations to separate module

将painter的实现拆分为单独的模块看，用户可以灵活选择不同的painter实现

### Added

### Changed

- move painter implementations to separate module

### Removed

### 添加

### 变更

- 将painter的实现拆分为单独的模块

### 删除

## [1.0.2] - 2019-04-11

optimize

优化

### Added

### Changed

- make DefaultPalettePainter and some fields of it overridable

### Removed

- remove `IndicatorPainter`
- remove `Gravity.UNKNOWN`

### 添加

### 变更

- 允许`DefaultPalettePainter`及其部分属性可复写

### 删除

- 移除用处有限的`IndicatorPainter`
- 移除不需要的`Gravity.UNKNOWN`

## [1.0.1] - 2019-04-01

support vertical `BrightnessView` & `AlphaView`

支持垂直方向的`BrightView`与`AlphaView`

### Added

- add `daemon_cp_brightnessOrientation` attribute to support vertical `BrightnessView`
- add `daemon_cp_alphaOrientation` attribute to support vertical `AlphaView`

### Changed

### Removed
    
### 添加

- 增加`daemon_cp_brightnessOrientation`属性以支持垂直方向的`BrightnessView`
- 增加`daemon_cp_alphaOrientation`属性以支持垂直方向的`AlphaView`

### 变更

### 删除

## [1.0.0] - 2019-03-26

add several attributes to customize position and size of brightness view and alpha view

增加亮度View与透明度View的位置与尺寸属性

### Added

- add position attributes and size attributes for brightness view and alpha view

### Changed

### Removed
    
### 添加

- 新增多个属性以调整亮度View与透明度View的位置与尺寸

### 变更

### 删除

## [0.1.7] - 2019-03-21

refactoring; support brightness & alpha adjustment

重构库的实现，增加了对亮度与透明度的调整的支持

### Added

- add `PaletteView` & `IPalettePainter` to draw palette view
- add `BrightnessView` & `IBrightnessPainter` to draw brightness view
- add `AlphaView` & `IAlphaPainter` to draw alpha view

### Changed

- refactor color picker view, change it from `View` to `ViewGroup`
- set `resourcePrefix`('daemon_cp_') for the library to avoid conflict to another library or project

### Removed

### 添加

- 添加`PaletteView` & `IPalettePainter`用来绘制调色板视图
- 添加`BrightnessView` & `IBrightnessPainter`用来绘制亮度视图
- 添加`AlphaView` & `IAlphaPainter`用来绘制透明度视图

### 变更

- 重构颜色选择器的实现方式，由`View`改为`ViewGroup`
- 设置`resourcePrefix`('daemon_cp_')属性已添加统一的资源前缀，防止和其他库或项目冲突

### 删除


## [0.1.6] - 2019-02-21

providing more flexible palette position and radius

提供更加灵活的调色板位置与半径

### Added

- add `paletteRadius`、`paletteGravity`、`paletteOffsetX` and `paletteOffsetY` to provide more flexible palette position and radius

### Changed

### Removed

- remove `palettePadding` attribute

### 添加

- 添加 `paletteRadius`、`paletteGravity`、`paletteOffsetX` 以及 `paletteOffsetY` 提供更加灵活的调色板的位置与大小

### 变更

### 删除

- 删除 `palettePadding` 属性

## [0.1.5] - 2019-02-19

remove 'square' attribute & 'initialColor' field

### Added

- add [ColorPicker](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/internal/ColorPicker.kt) to manager color

### Changed

- replace `jitpack` with `jcenter`

### Removed

- remove `initialColor` field
- remove `square` attribute

### 添加

- 添加 [ColorPicker](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/internal/ColorPicker.kt) 管理颜色

### 变更

- 使用 `jcenter` 替换 `jitpack`

### 删除

- 删除 `initialColor` 变量
- 删除 `square` 属性

## [0.1.4] - 2019-02-14

add custom palette painter support

### Added

- add [PalettePainter](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/painter/PalettePainter.kt) for user to customize palette painter
- add [BrightnessProvider](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/BrightnessProvider.kt) to set brightness(to be improved)
- add `square` attribute to automatic measure ColorPickerView as square

### Changed

- convert java code to kotlin
- move [IndicatorPainter](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/painter/IndicatorPainter.kt) to `painter` package
- fix `"Custom view ColorPickerView overrides onTouchEvent but not performClick"` warning

### Removed

- remove `disabledColor` attribute, let customized palette painter to do it, see sample[`DisabledStatePalettePainter`](https://github.com/daemon369/ColorPickerView/blob/master/app/src/main/java/me/daemon/colorpicker/demo/DisabledStatePalettePainter.java)

### 添加

- 添加 [PalettePainter](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/painter/PalettePainter.kt) 以便用户自定义调色板的绘制
- 添加 [BrightnessProvider](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/BrightnessProvider.kt) 用来修改明亮度（功能不完善，还需要优化）
- 添加 `square` 属性，为`true`时在`ColorPickerView`的`onMeasure`中自动调整形状为方形，已宽和高中最小的值为边长

### 变更

- Java代码转化为Kotlin代码
- 移动 [IndicatorPainter](https://github.com/daemon369/ColorPickerView/blob/master/colorpickerview/src/main/kotlin/me/daemon/colorpicker/painter/IndicatorPainter.kt) 到 `painter` 包下
- 解决 `"Custom view ColorPickerView overrides onTouchEvent but not performClick"` 警告

### 删除

- 移除 `disabledColor` 属性，需要此功能可以在自定义的`PalettePainter`中自行实现，可以参考示例[`DisabledStatePalettePainter`](https://github.com/daemon369/ColorPickerView/blob/master/app/src/main/java/me/daemon/colorpicker/demo/DisabledStatePalettePainter.java)
