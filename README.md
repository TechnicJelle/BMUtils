# BMUtils
A small library with a collection of useful functions for BlueMap addons.

Should work with any plugin/mod-loader. Currently only tested with Paper.
Please let me know if you have any issues when using other platforms!

## Installation
There are two ways to use this library:

### Import as a Maven/Gradle dependency
Visit https://jitpack.io/#TechnicJelle/BMUtils for detailed instructions
on how to add this library to your project.

You may want to shade the library!

### Copy the source code
You can also just copy only the specific source code files you need into your own project.\
This is useful if you only need a few functions and don't want to add a whole new dependency.

Please make sure to keep the license and author information in the source code files,
and to abide by the [license](LICENSE) terms!

## Usage
Please see the javadoc for the full API reference: [technicjelle.com/BMUtils](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils.html)

### Copy Jar Resource to BlueMap
This function copies any resource file from your jar to the BlueMap assets folder.\
Useful for adding custom icons, scripts, or styles from your own addon.
```java
copyJarResourceToBlueMap(BlueMapAPI, ClassLoader, String fromResource, String toAsset, boolean overwrite)
```

### Copy Any File to BlueMap
This function copies any file to the BlueMap assets folder.\
Useful for copying user-provided assets to BlueMap,
from a configuration directory for example.
```java
copyFileToBlueMap(BlueMapAPI, Path from, String toAsset, boolean overwrite)
```

### Copy Any Stream to BlueMap
This function copies any stream to the BlueMap assets folder.\
Useful for when you have a stream of data, for example from a URL.
```java
copyStreamToBlueMap(BlueMapAPI, InputStream in, String toAsset, boolean overwrite)
```

### Get Player Head Icon Address
This function returns the address of a player head icon.\
Useful when you want to use a playerhead from the map.
For example, when adding custom icons to the map that involve the player head.
```java
getPlayerHeadIconAddress(BlueMapAPI, UUID playerUUID, BlueMapMap blueMapMap)
```

## Contributing
If you have any suggestions for more useful functions to add, please let me know by creating an issue on GitHub.

To get support with this library, join the [BlueMap Discord server](https://bluecolo.red/map-discord)
and ask your questions in [#3rd-party-support](https://discord.com/channels/665868367416131594/863844716047106068). You're welcome to ping me, @TechnicJelle.
