# BMUtils
A small library with a collection of useful functions for BlueMap addons.

Should work with any plugin/mod-loader. Currently only tested with Paper. Please let me know if you have any issues when using other platforms!

## Installation
Visit https://jitpack.io/#TechnicJelle/BMUtils for details on how to install this library.

## Usage
Please see the javadoc for the full API reference: [technicjelle.com/BMUtils](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils.html)

### Copy Jar Resource To BlueMap
This function copies any file from your jar to the BlueMap assets folder. This is useful for adding custom icons, scripts, or styles from your own addon.
```java
copyJarResourceToBlueMap(ClassLoader, BlueMapAPI, String fromResource, String toAsset, boolean overwrite)
```

### Get Player Head Icon Address
This function returns the address of a player head icon. This is useful when you want to use a playerhead on the map. For example, when adding custom icons to the map.
```java
getPlayerHeadIconAddress(BlueMapAPI, UUID playerUUID, BlueMapMap blueMapMap)
```

## Contributing
If you have any suggestions for more useful functions to add, please let me know by creating an issue on GitHub.

To get support with this library, join the [BlueMap Discord server](https://bluecolo.red/map-discord)
and ask your questions in [#3rd-party-support](https://discord.com/channels/665868367416131594/863844716047106068). You're welcome to ping me, @TechnicJelle.
