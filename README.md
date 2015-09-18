# R-Kit Android
Welcome to R-Kit android, the new way of conducting research studies.

R-kit is a mobile app SDK and node.js application designed to help researchers build android-based research studies quickly and easily.

Data collected by the mobile application can be collected and stored using the server, or any other server capable of storing incoming POST data.

The platform has been designed to be easily extendable, for example to add additional data-processing functionality, or can act as a reference platform to create your own server.

[![Build Status](https://magnum.travis-ci.com/paperclipmonkey/R-Kit.svg?token=SsrjjdmEtzcJsGDqjxQw)](https://magnum.travis-ci.com/paperclipmonkey/R-Kit)


Use this sample application as a base from which to develop your research study app.


Data is stored using locally [SugarORM](http://satyan.github.io/sugar/), a wrapper for SQLite, and uploaded using the [Android Upload Service](https://github.com/alexbbb/android-upload-service) library.

The project runs in Android Studio and uses the Gradle build platform, although it can be easily modified for other platforms.

The backwards compatibility extends to over 90% of the current Android market.


The code relies on other modules. You will need to install these dependancies before running the code.

A test suite is included which covers models and running of the sample tasks inside an emulator or on a real device.

If you want to submit code upstream ensure any new code is covered by tests (>90% coverage) and that any code is formatted correctly.