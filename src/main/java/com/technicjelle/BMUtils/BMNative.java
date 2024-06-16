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

package com.technicjelle.BMUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.common.api.BlueMapAPIImpl;
import de.bluecolored.bluemap.common.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Utility functions for BlueMap Native Addons
 */
public class BMNative {
	private final static JsonParser JSON_PARSER = new JsonParser();
	private final static String ADDON_METADATA_FILE = "bluemap.addon.json";
	private final static String KEY_ID = "id";

	private BMNative() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Every native BlueMap addon has its own config directory, which is allocated by BlueMap.<br>
	 * This function gives you the path to that directory.
	 *
	 * @param api         The BlueMapAPI instance
	 * @param classLoader The class loader of the addon
	 * @return The allocated config directory
	 * @throws IOException If the directory could not be created
	 */
	public static Path getAllocatedConfigDirectory(BlueMapAPI api, ClassLoader classLoader) throws IOException {
		return getAddonsDirectory(api).resolve(getAddonID(classLoader));
	}

	/**
	 * Gets the directory where BlueMap Native Addons are stored.
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
	 * Gets the addon's ID from the addon metadata file. (<code>bluemap.addon.json</code>)
	 *
	 * @param classLoader The class loader of the addon
	 * @return The addon's ID
	 * @throws IOException If the metadata file could not be read
	 */
	public static String getAddonID(ClassLoader classLoader) throws IOException {
		try (
				final InputStream in = classLoader.getResourceAsStream(ADDON_METADATA_FILE)
		) {
			if (in == null) throw new IOException("Resource not found: " + ADDON_METADATA_FILE);
			final String json = new String(in.readAllBytes());
			final JsonObject o = JSON_PARSER.parse(json).getAsJsonObject();
			return o.get(KEY_ID).getAsString();
		}
	}
}

