# GBKSTEPPER

Custom Step view.

![](images/stepview.gif)

## Installation
Add to the top level gradle file:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' } 
    }
}
```

Add to the app level gradle:
```
dependencies {
    implementation 'com.github.gbksoft:Stepper:v1.0.0'
}
```

## Capabilities

- You can set progressDrawable
- You can change the number of steps and the current step (at least 2 steps)
- You can change the drawable for the active and inactive steps
- You can transfer an array of signatures for steps (optional, since signatures can be unnecessary) 
- You can transfer the text style for the active step and inactive.


## How to use

```
<com.gbksoft.stepper.StepView
        android:id="@+id/step"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:progressStepCount="6"
        app:activeStepDrawable="@drawable/ic_step_act"
        app:activeTextColor="@color/colorText"
        app:activeTextSize="14sp"
        app:nonActivetextSize="14sp"
        app:nonActiveStepDrawable="@drawable/ic_step_non_act"
        app:nonActivetextColor=@color/colorNonActive
    />
```

`app:progressStepCount="6"` - max steps

`app:activeStepDrawable="@drawable/ic_step_act"` - Active Step Drawable

`app:nonActiveStepDrawable="@drawable/ic_step_non_act"` - Non Active Step Drawable

`app:activetextSize="14sp"` - Active Step Text Size

`app:activetextColor="@color/colorText"` - Active Step Color Text

`app:nonActivetextSize="14sp"` - Non Active Step Text Size

`app:nonActivetextColor=@color/colorNonActive"` - Active Step Color Text



```
public void setProgressText(List<String> args)
```
```
public void setProgress(int progress)
```


# Let us know
This scrollable view android adjustment is not our only original decision. Contact us by email [hello@gbksoft.com](hello@gbksoft.com) to find out more about our projects! Share your feedback and tell us about yourself. 