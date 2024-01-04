# AndroidSniffer
 This is an android application that utilizes the phone's wireless adapter to detect and retrieve information from nearby wi-fi enable devices. It then sends information such as the devices name and MAC address to a database, where a python server will cross check it to see if the address is reserved by the proper company. For example, if a device named "iphone" has an address that is reserved by DELL, this would be a potential suspicious device. The same will be done for devices with bluetooth enabled. Some devices will allow distance measurement via ping as well, allowing for triangulation if 3 or more scanning devices are present. 
 The android device does not need to be rooted, and the server can be run on a Raspberry Pi
