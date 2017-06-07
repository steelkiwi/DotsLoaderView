# DotsLoaderView

[![Made in SteelKiwi](https://github.com/steelkiwi/IndicatorView/blob/master/assets/made_in_steelkiwi.png)](http://steelkiwi.com/blog/)
[![Download](https://api.bintray.com/packages/soulyaroslav/maven/dots-loader-view/images/download.svg)](https://bintray.com/soulyaroslav/maven/dots-loader-view/_latestVersion)

Wonderful dots loader view inspired this [Design](https://material.uplabs.com/posts/loading-animation-dots)

# View

![](https://github.com/steelkiwi/DotsLoaderView/blob/master/assets/dots-loader-view.gif)

# Download

## Gradle

```gradle
compile 'com.steelkiwi:dots-loader-view:1.0.0'
```

# Usage

Add DotsLoaderView to your xml layout

```xml
<steelkiwi.com.library.DotsLoaderView
    android:id="@+id/dotsLoaderView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:visibility="visible"
    app:dlv_item_drawable="@drawable/circle_background"
    app:dlv_line_color="@color/point_color"/>
```

You can customize view, through this attributes

* app:dlv_item_drawable - background shape for dot view
* app:dlv_line_color - view bottom line color

To use it in the code you need only call this methods

```java
// to show loading
dotsLoaderView.show();
// to hide loading
dotsLoaderView.hide();
```

# License

```
Copyright © 2017 SteelKiwi, http://steelkiwi.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
