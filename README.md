# BMUtils
[![Latest Release](https://repo.bluecolored.de/api/badge/latest/releases/com/technicjelle/BMUtils?name=Latest%20Release&prefix=v)](https://repo.bluecolored.de/#/releases/com/technicjelle/BMUtils)

A small library with a collection of useful functions for BlueMap addons,
that I have created while working on my own BlueMap addons.

Please check them out if you're interested:
[TechnicJelle.com BlueMap Addons](https://technicjelle.com/#%F0%9F%8C%90-bluemap-addons)

Should work with any plugin/mod-loader. Currently only tested with Paper.
Please let me know if you have any issues when using other platforms!  
Now also has functions for Native BlueMap Addons!

## Install as dependency in Maven/Gradle
Visit https://repo.bluecolored.de/#/releases/com/technicjelle/BMUtils
for instructions on how to add this library as a dependency to your project.

You may want to shade the library!

## Usage/Overview of Features
Please see the javadoc for the full API reference:
- main (latest commit): https://technicjelle.com/BMUtils
- latest release: https://repo.bluecolored.de/javadoc/releases/com/technicjelle/BMUtils/latest
  - Also has docs for previous releases (v4.2 and up)

This section just contains a brief overview of some of the most useful features of this library:
- [Copying Assets](#copying-assets-docs)
- [Get Player Head Icon Address](#get-player-head-icon-address-docs)
- [Create Marker around a Claimed Area](#create-marker-around-a-claimed-area-docs)
- [Expand/Shrink a Shape](#expandshrink-a-shape-docs)
- [BMUtils for Native BlueMap Addons](#utilities-for-native-bluemap-addons-docs)
  - [Config Directory](#config-directory-docs)
  - [Metadata](#metadata-docs)
  - [Logger](#logger-docs)

### Copying Assets ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html))
The BMCopy class has functions to copy files to various places where BlueMap may need them.  
The following table shows from where you can copy, and where you can copy to.

| ↓ Source ╲ Destination →                                                                        | BlueMapMap                                                                                                                                                                                                            | WebApp                                                                                                                                                                                                                      |
|-------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [File](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/nio/file/Path.html)    | [`fileToMap()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#fileToMap(de.bluecolored.bluemap.api.BlueMapMap,java.nio.file.Path,java.lang.String,boolean))                                   | [`fileToWebApp()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#fileToWebApp(de.bluecolored.bluemap.api.BlueMapAPI,java.nio.file.Path,java.lang.String,boolean))                                   |
| Jar Resource                                                                                    | [`jarResourceToMap()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#jarResourceToMap(de.bluecolored.bluemap.api.BlueMapMap,java.lang.ClassLoader,java.lang.String,java.lang.String,boolean)) | [`jarResourceToWebApp()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#jarResourceToWebApp(de.bluecolored.bluemap.api.BlueMapAPI,java.lang.ClassLoader,java.lang.String,java.lang.String,boolean)) |
| [Stream](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/io/InputStream.html) | [`streamToMap()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#streamToMap(de.bluecolored.bluemap.api.BlueMapMap,java.io.InputStream,java.lang.String,boolean))                              | [`streamToWebApp()`](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMCopy.html#streamToWebApp(de.bluecolored.bluemap.api.BlueMapAPI,java.io.InputStream,java.lang.String,boolean))                              |

Each of these functions has an `overwrite` parameter,
with which you can specify whether to overwrite the file if it already exists.

#### Sources
Copying from a **jar resource** is useful for copying any resource file from your own addon's jar.

Copying from a **file** is useful for copying user-provided assets to BlueMap, from a configuration directory, for example.

Copying from a **stream** is useful for when you have a stream of data, for example, from a URL.

In some cases, I copy something from a jar resource to my addon's configuration directory,
and then copy that file to BlueMap.

#### Destinations
Copying to the **webapp** is useful for e.g. adding custom icons, scripts, or styles.  
Any scripts or styles you copy with these functions will be automatically registered with BlueMap.

Copying to a **specific map** copies it into that
map's [asset storage](https://repo.bluecolored.de/javadoc/releases/de/bluecolored/bluemap-api/2.7.3/raw/de/bluecolored/bluemap/api/AssetStorage.html),
which means it can be stored on disk, or in a database.  
This is useful for adding e.g. custom icons or other assets to a specific map.  
It can also make managing all the custom assets slightly easier for people who are using custom webservers,
because maps are automatically be synced, while the webapp is not.

### Get Player Head Icon Address ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMSkin.html))
This function returns the address of a player head icon,
and automatically generates the icon if it doesn't exist yet.  
Useful when you want to use a playerhead from the map.  
For example, when adding custom icons to the map that involve the player head.
```java
BMSkin.getPlayerHeadIconAddress(BlueMapAPI, UUID playerUUID, BlueMapMap)
```

### Create Marker around a Claimed Area ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/Cheese.html))
With the Cheese class, you can create a [BlueMap Shape](https://repo.bluecolored.de/javadoc/releases/de/bluecolored/bluemap-api/2.7.3/raw/de/bluecolored/bluemap/api/math/Shape.html) from a collection of chunks.  
Useful for when you want to create a marker around a claimed area.

To get started, feed a list of Chunk coordinates into `createPlatterFromChunks()`.  
This will return a collection of Cheese objects, which you can use to create your markers.
The Cheese object contains the outline shape of the claimed area, and any holes in it.
It's specifically designed to be fed directly into a 
[ShapeMarker](https://repo.bluecolored.de/javadoc/releases/de/bluecolored/bluemap-api/2.7.3/raw/de/bluecolored/bluemap/api/markers/ShapeMarker.html) or
[ExtrudeMarker](https://repo.bluecolored.de/javadoc/releases/de/bluecolored/bluemap-api/2.7.3/raw/de/bluecolored/bluemap/api/markers/ExtrudeMarker.html).

The Platter functions return multiple Cheeses; a "platter" of them, if you will.  
It does this, because the input chunk coordinates might not be all connected,
which means you have to create multiple markers for the same claimed area.

<details>
<summary>Full Platter code example</summary>

```java
@Event
void onPlayerClaimEvent(Player player, Town claimedTown) {
	//assuming a class member of Map<World, MarkerSet> markerSetMap;
	MarkerSet markerSet = markerSetMap.get(claimedTown.getWorld());

	Vector2i[] chunkCoordinates = claimedTown.getClaimedChunks().stream()
		.map(chunk -> new Vector2i(chunk.getX(), chunk.getZ()))
		.toArray(Vector2i[]::new);

	Collection<Cheese> platter = Cheese.createPlatterFromChunks(chunkCoordinates);
	int i = 0;
	for (Cheese cheese : platter) {
		ShapeMarker chunkMarker = new ShapeMarker.Builder()
			.label(claimedTown.getName())
			.shape(cheese.getShape(), 80)
			.holes(cheese.getHoles().toArray(Shape[]::new))
			//...
			.build();
		markerSet.put("town-" + claimedTown.getName() + "-segment-" + (i++), chunkMarker);
	}
}
```
**Note:** You should probably run this on a separate thread, as to not block the main server thread!
</details>

If you're absolutely 100% sure your input chunk coordinates are all connected,
due to your chunk claiming system ensuring that already,
you can use `createSingleFromChunks()` to create a single Cheese.

<details>
<summary>Full Single code example</summary>

```java
@Event
void onPlayerClaimEvent(Player player, Town claimedTown) {
	//assuming a class member of Map<World, MarkerSet> markerSetMap;
	MarkerSet markerSet = markerSetMap.get(claimedTown.getWorld());

	Vector2i[] chunkCoordinates = claimedTown.getClaimedChunks().stream()
		.map(chunk -> new Vector2i(chunk.getX(), chunk.getZ()))
		.toArray(Vector2i[]::new);

	Cheese cheese = Cheese.createSingleFromChunks(chunkCoordinates);
	ExtrudeMarker chunkMarker = new ExtrudeMarker.builder()
		.label(claimedTown.getName())
		.shape(cheese.getShape(), 50, 80)
		.holes(cheese.getHoles().toArray(Shape[]::new))
		//...
		.build();
	markerSet.put("town-" + claimedTown.getName(), chunkMarker);
}
```

</details>

If your area data isn't chunk-based, you can use the Cells functions, instead of the Chunks functions,
to create a Cheese from a collection of non-16x16 cells.

_Thanks to [@TBlueF](https://github.com/TBlueF) for contributing this function, and the funny name!_

### Expand/Shrink a Shape ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/ShapeExtensions.html))
In the ShapeExtensions class, you can find multiple functions to expand or shrink a shape.  
Useful when you have just created a shape through a Cheese,
and you want to shrink it a little bit to prevent [Z-fighting](https://en.wikipedia.org/wiki/Z-fighting).

There are two kinds of expand/shrink functions: Rect, and Accurate. Both are useful in different situations.

For shapes generated by a Cheese, I would recommend using the Rect functions,
as they work more intuitively on the kind of rectangular shapes that Cheeses generate.  
For example, expanding a 16x16 square by 1 will result in an 18x18 shape, as each side is expanded by 1.

For more complex, free-form shapes, like curves, arcs, ellipses, circles, etc.,
I would recommend using the Accurate functions,
as they work more intuitively on those kinds of shapes.  
For example, expanding a circle with a radius of 8 by 1 will result in a circle with a radius of 9.

#### Simple Scaling
There are also some functions for scaling a shape. This is different from expanding/shrinking,
as it will scale the whole shape relative to a single point,
instead of from each separate edge/point of the shape itself.  
You can see a comparison of the two in the following gifs:

| Expand/Shrink                                             | Scale                                     |
|-----------------------------------------------------------|-------------------------------------------|
| ![Expand/Shrink](.github/readme_assets/expand-shrink.gif) | ![Scale](.github/readme_assets/scale.gif) |

### Utilities for Native BlueMap Addons ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMNative.html))
BMUtils also has a collection of functions for Native BlueMap Addons.
These are usually prefixed with BMN, instead of BM, to indicate that they are only to be used for Native BlueMap Addons.

#### Config Directory ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMNative/BMNConfigDirectory.html))
BMUtils allocates a config directory for every native BlueMap addon,
where you can store your addon's configuration files and other assets.

With the functions in BMNConfigDirectory, you can easily get the path to your addon's config directory.

It also has functions similar to BMCopy, to copy files to your addon's config directory.
([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMNative/BMNConfigDirectory.BMNCopy.html))  
This is useful for when you want to unpack default configuration files from your jar to the config directory.

#### Metadata ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMNative/BMNMetadata.html))
Each BlueMap Native Addon has a metadata file: `bluemap.addon.json`.
The BMNMetadata class has functions to read from this file.
You probably won't need to use these functions directly, as the other BMUtils functions will handle this for you.

#### Logger ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMUtils/BMNative/BMNLogger.html))
Logging stuff to the console is an important part of debugging.
For this, BMUtils has a logger that you can use to log messages to the console,
in a bit of a neater fashion than just printing to the standard output.

You can log debug, info, warning, and error messages with this logger.

There are also the noFlood versions of these functions, which will only actually show the log once in a while,
instead of flooding the console with the same message over and over again.

## Contributing
If you have any suggestions for more useful functions to add, please let me know by creating an issue on GitHub.

## Support
To get support with this library, join the [BlueMap Discord server](https://bluecolo.red/map-discord)
and ask your questions in [#3rd-party-support](https://discord.com/channels/665868367416131594/863844716047106068). You're welcome to ping me, @TechnicJelle.

## Performance Reports
I've set up automatic performance reports for this library, to keep track of how it performs over time.  
You can view a graph of the performance reports over time [here!](https://technicjelle.com/BMUtils-PerformanceReports/)

If you'd like to test the performance on your own computer, in IntelliJ IDEA, you can use [this plugin](https://github.com/artyushov/idea-jmh-plugin).
