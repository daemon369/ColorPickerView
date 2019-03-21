# Changelog

## [0.1.6] - 2019-02-21

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
