/*
 * This file is part of BMUtils, licensed under the MPL2 License (MPL).
 * Please keep tabs on https://github.com/TechnicJelle/BMUtils for updates.
 *
 * Copyright (c) TechnicJelle <https://technicjelle.com>
 * Copyright (c) contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.technicjelle.BMUtils.BMNative;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.common.api.BlueMapAPIImpl;
import de.bluecolored.bluemap.common.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * BMUtils allocates a config directory for every native BlueMap addon.<br>
 * Here are functions to retrieve that directory.<br>
 * <br>
 * The directory name is the addon's ID.
 */
public class BMNConfigDirectory {
	private BMNConfigDirectory() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * This function gives you the path to your addon's allocated config directory.
	 *
	 * @param api         The BlueMapAPI instance
	 * @param classLoader The class loader of the addon
	 * @return The allocated config directory
	 * @throws IOException If the directory could not be created
	 */
	public static Path getAllocatedDirectory(BlueMapAPI api, ClassLoader classLoader) throws IOException {
		return getAddonsDirectory(api).resolve(BMNMetadata.getAddonID(classLoader));
	}

	/**
	 * Gets the directory where BlueMap Native Addons are stored.<br>
	 * Probably shouldn't be used directly, use {@link #getAllocatedDirectory(BlueMapAPI, ClassLoader)} instead.
	 *
	 * @param api The BlueMapAPI instance
	 * @return BlueMap's own addons directory
	 */
	public static Path getAddonsDirectory(BlueMapAPI api) {
		BlueMapAPIImpl apiImpl = (BlueMapAPIImpl) api;
		@Nullable Plugin plugin = apiImpl.plugin();
		if (plugin == null) {
			//cli
			return Path.of(".", "config", "addons");
		} else {
			//installed on a server (spigot/paper/fabric/forge/sponge/etc)
			return plugin.getServerInterface().getConfigFolder().resolve("addons");
		}
	}

	/**
	 * Utility functions for copying stuff to the config directory that BMUtils allocated.
	 */
	public static class BMNCopy {
		private BMNCopy() {
			throw new IllegalStateException("Utility class");
		}

		/**
		 * Every native BlueMap addon has its own config directory, which is allocated by BlueMap.<br>
		 * This function copies a stream to that directory.
		 *
		 * @param blueMapAPI  The BlueMapAPI instance
		 * @param classLoader The class loader to get the addon ID from
		 * @param in          The input stream to copy from
		 * @param toFile      The asset to copy to, relative to the allocated config directory
		 * @param overwrite   Whether to overwrite the asset if it already exists
		 * @throws IOException If the stream could not be copied
		 */
		public static void fromStream(
				final @NotNull BlueMapAPI blueMapAPI,
				final @NotNull ClassLoader classLoader,
				final @NotNull InputStream in,
				final @NotNull String toFile,
				final boolean overwrite
		) throws IOException {
			final Path configDirectory = BMNConfigDirectory.getAllocatedDirectory(blueMapAPI, classLoader);
			final Path toPath = configDirectory.resolve(toFile);

			//Copy resource
			if (Files.exists(toPath) && !overwrite) return;
			Files.createDirectories(toPath.getParent());
			try (
					final OutputStream out = Files.newOutputStream(toPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
			) {
				in.transferTo(out);
			}
		}

		/**
		 * Every native BlueMap addon has its own config directory, which is allocated by BlueMap.<br>
		 * This function copies a file to that directory.
		 *
		 * @param blueMapAPI  The BlueMapAPI instance
		 * @param classLoader The class loader to get the addon ID from
		 * @param from        The file to copy
		 * @param toFile      The file to copy to, relative to the allocated config directory
		 * @param overwrite   Whether to overwrite the asset if it already exists
		 * @throws IOException If the file could not be found or copied
		 */
		public static void fromFile(
				final @NotNull BlueMapAPI blueMapAPI,
				final @NotNull ClassLoader classLoader,
				final @NotNull Path from,
				final @NotNull String toFile,
				final boolean overwrite
		) throws IOException {
			try (
					final InputStream in = Files.newInputStream(from)
			) {
				fromStream(blueMapAPI, classLoader, in, toFile, overwrite);
			}
		}

		/**
		 * Every native BlueMap addon has its own config directory, which is allocated by BlueMap.<br>
		 * This function copies a resource from the jar to that directory.
		 *
		 * @param blueMapAPI   The BlueMapAPI instance
		 * @param classLoader  The class loader to get the addon ID from and to get the resource out of the jar from
		 * @param fromResource The resource to copy from the jar
		 * @param toFile       The file to copy to, relative to the allocated config directory
		 * @param overwrite    Whether to overwrite the asset if it already exists
		 * @throws IOException If the resource could not be found or copied
		 */
		public static void fromJarResource(
				final @NotNull BlueMapAPI blueMapAPI,
				final @NotNull ClassLoader classLoader,
				final @NotNull String fromResource,
				final @NotNull String toFile,
				final boolean overwrite
		) throws IOException {
			try (
					final InputStream in = classLoader.getResourceAsStream(fromResource)
			) {
				if (in == null) throw new IOException("Resource not found: " + fromResource);
				fromStream(blueMapAPI, classLoader, in, toFile, overwrite);
			}
		}
	}
}
