# Kadence MVI -  framework based on MVI + LiveData + ViewModel for Single Activity Applcation.


Features:
- Simple save and restore ViewState when activity was destroyed in background. (Limitation for ViewState 0.5 MB)
- Function for parent's fragments and child's fragments for single activity Applcation .
- Improved portability fragments between screens by reduced relations between fragments with using childs ViewModels in parent ViewModel associated with fragments.

You can see example of using

https://github.com/dmkenza/example-kadence-mvi    

and extra information

https://proandroiddev.com/best-architecture-for-android-mvi-livedata-viewmodel-71a3a5ac7ee3


# How to install

 

To use library add 

`implementation 'com.github.dmkenza:kadence-mvi:0.2'`


and

``allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}``




