Simple controller appication for an RC car using an ESP8266 using the MRPC protocol. 

The RC car must be connected to the same WiFi network as your device and export two MRPC services, "throttle" and "steering". The services must accept a float between 0 and 1.0 (0=left and 1.0=right).
