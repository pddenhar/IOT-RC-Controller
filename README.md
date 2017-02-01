MRPC RC Controller
==================

Simple controller appication for an RC car using an ESP8266 using the MRPC protocol. 

Dependencies
------------
Clone [android-mrpc](https://github.com/alex-sherman/android-mrpc) into the same parent directory that you cloned IOT-RC-Controller into. 

It is required for the project to compile. If you want to clone it somewhere else, change the projectDir setting in settings.gradle to the correct path. 

Usage
-----

The RC car must be connected to the same WiFi network as your device and export two MRPC services, "throttle" and "steering". The services must accept a float between 0 and 1.0 (0=left and 1.0=right).
