# BMUtils
A small library with a collection of useful functions for BlueMap addons,
that I have created while working on my own BlueMap addons.

Please check them out if you're interested:
[TechnicJelle.com BlueMap Addons](https://technicjelle.com/#%F0%9F%8C%90-bluemap-addons)

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

## Usage/Overview of Features
This section just contains a brief overview of some of the most useful features of this library.\
Please see the javadoc for the full API reference: [technicjelle.com/BMUtils](https://technicjelle.com/BMUtils)

- [Copying Assets](#copying-assets-docs)
	- [Copy Jar Resource](#copy-jar-resource)
	- [Copy Any File](#copy-any-file)
	- [Copy Any Stream](#copy-any-stream)
- [Get Player Head Icon Address](#get-player-head-icon-address-docs)
- [Create Marker around a Claimed Area](#create-marker-around-a-claimed-area-docs)
- [Expand/Shrink a Shape](#expandshrink-a-shape-docs )

### Copying Assets ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMCopy.html))
#### Copy Jar Resource
This function copies any resource file from your jar to the BlueMap assets folder.\
Useful for adding custom icons, scripts, or styles from your own addon.\
Any scripts or styles that are copied with this function will be automatically registered with BlueMap.
```java
BMCopy.jarResourceToWebApp(BlueMapAPI, ClassLoader, String fromResource, String toAsset, boolean overwrite)
```

Also available for copying to a specific BlueMap map's [asset storage](https://bluecolored.de/bluemapapi/latest/de/bluecolored/bluemap/api/AssetStorage.html).\
Do not use this method for copying scripts or styles, as those need to be installed in the webapp.
```java
BMCopy.jarResourceToMap(BlueMapMap, ClassLoader, String fromResource, String toAsset, boolean overwrite)
```

#### Copy Any File
This function copies any file to the BlueMap assets folder.\
Useful for copying user-provided assets to BlueMap, from a configuration directory for example.\
Any scripts or styles that are copied with this function will be automatically registered with BlueMap.
```java
BMCopy.fileToWebApp(BlueMapAPI, Path from, String toAsset, boolean overwrite)
```

Also available for copying to a specific BlueMap map's [asset storage](https://bluecolored.de/bluemapapi/latest/de/bluecolored/bluemap/api/AssetStorage.html).\
Do not use this method for copying scripts or styles, as those need to be installed in the webapp.
```java
BMCopy.fileToMap(BlueMapMap, Path from, String toAsset, boolean overwrite)
```

#### Copy Any Stream
This function copies any stream to the BlueMap assets folder.\
Useful for when you have a stream of data, for example from a URL.\
Any scripts or styles that are copied with this function will be automatically registered with BlueMap.
```java
BMCopy.streamToWebApp(BlueMapAPI, InputStream in, String toAsset, boolean overwrite)
```

Also available for copying to a specific BlueMap map's [asset storage](https://bluecolored.de/bluemapapi/latest/de/bluecolored/bluemap/api/AssetStorage.html).\
Do not use this method for copying scripts or styles, as those need to be installed in the webapp.
```java
BMCopy.streamToMap(BlueMapMap, InputStream in, String toAsset, boolean overwrite)
```

### Get Player Head Icon Address ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/BMSkin.html))
This function returns the address of a player head icon,
and automatically generates the icon if it doesn't exist yet.\
Useful when you want to use a playerhead from the map.\
For example, when adding custom icons to the map that involve the player head.
```java
BMSkin.getPlayerHeadIconAddress(BlueMapAPI, UUID playerUUID, BlueMapMap)
```

### Create Marker around a Claimed Area ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/Cheese.html))
With the Cheese class, you can create a [BlueMap Shape](https://bluecolored.de/bluemapapi/latest/de/bluecolored/bluemap/api/math/Shape.html) from a collection of chunks.\
Useful for when you want to create a marker around a claimed area.

To get started, feed a list of Chunk coordinates into `createPlatterFromChunks()`.\
This will return a collection of Cheese objects, which you can use to create your markers.

The Platter functions return multiple Cheeses; a "platter" of them, if you will.\
It does this, because the input chunk coordinates might not be all connected,
which means you have to create multiple markers for the same claimed area.

<details>
<summary>Full Platter code example</summary>

```java
@Event
void onPlayerClaimEvent(Player player, Town claimedTown) {
	//assuming a class member of Map<World, MarkerSet> markerSetMap;
	MarkerSet markerSet = markerSetMap.get(claimedTown.getWorld());

	Collection<Vector2i> chunkCoordinates = claimedTown.getClaimedChunks().stream()
		.map(chunk -> new Vector2i(chunk.getX(), chunk.getZ()))
		.collect(Collectors.toList());

	Collection<Cheese> platter = Cheese.createPlatterFromChunks(chunkCoordinates);
	for (int i = 0; i < platter.size(); i++) {
		Cheese cheese = platter[i];
		ShapeMarker chunkMarker = new ShapeMarker.Builder()
			.label(claimedTown.getName())
			.shape(cheese.getShape())
			.holes(cheese.getHoles())
			//...
			.build();
		markerSet.put("town-" + claimedTown.getName() + "-segment-" + i, chunkMarker);
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

	Collection<Vector2i> chunkCoordinates = claimedTown.getClaimedChunks().stream()
		.map(chunk -> new Vector2i(chunk.getX(), chunk.getZ()))
		.collect(Collectors.toList());

	Cheese cheese = Cheese.createFromChunks(chunkCoordinates);
	ExtrudeMarker chunkMarker = new ExtrudeMarker.builder()
		.label(claimedTown.getName())
		.shape(cheese.getShape())
		.holes(cheese.getHoles())
		//...
		.build();
	markerSet.put("town-" + claimedTown.getName(), chunkMarker);
}
```

</details>

If your area data isn't chunk-based, you can use the Cells functions, instead of the Chunks functions,
to create a Cheese from a collection of non-16x16 cells.

_Thanks to [@TBlueF](https://github.com/TBlueF) for contributing this function, and the funny name!_

### Expand/Shrink a Shape ([Docs](https://technicjelle.com/BMUtils/com/technicjelle/ShapeExtensions.html))
In the ShapeExtensions class, you can find multiple functions to expand or shrink a shape.\
Useful when you just created a shape through a Cheese, and you want to shrink it a little bit to prevent Z-fighting.

There are two kinds of expand/shrink functions: Rect, and Accurate. Both are useful in different situations.

For shapes generated by a Cheese, I would recommend using the Rect functions,
as they work more intuitively on the kind of rectangular shapes that Cheeses generate.\
For example, expanding a 16x16 square by 1 will result in a 18x18 shape, as each side is expanded by 1.

For more complex, free-form shapes, like curves, arcs, ellipses, circles, etc.,
I would recommend using the Accurate functions,
as they work more intuitively on those kinds of shapes.\
For example, expanding a circle with a radius of 8 by 1 will result in a circle with a radius of 9.

There are also some functions for scaling a shape. This is different from expanding/shrinking,
as it will scale the whole shape relative to a single point,
instead of from each separate edge/point of the shape itself.\
You can see a comparison of the two in the following gifs:

| Expand/Shrink                                             | Scale                                     |
|-----------------------------------------------------------|-------------------------------------------|
| ![Expand/Shrink](.github/readme_assets/expand-shrink.gif) | ![Scale](.github/readme_assets/scale.gif) |

## Contributing
If you have any suggestions for more useful functions to add, please let me know by creating an issue on GitHub.

## Support
To get support with this library, join the [BlueMap Discord server](https://bluecolo.red/map-discord)
and ask your questions in [#3rd-party-support](https://discord.com/channels/665868367416131594/863844716047106068). You're welcome to ping me, @TechnicJelle.

## Performance Reports
I've set up automatic performance reports for this library, to keep track of how it performs over time.\
You can view a graph of the performance reports over time [here!](https://technicjelle.com/BMUtils-PerformanceReports/)

If you'd like to test the performance on your own computer, in IntelliJ IDEA, you can use [this plugin](https://github.com/artyushov/idea-jmh-plugin).
