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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility functions for interfacing with the metadata of native BlueMap addons.
 * (<code>bluemap.addon.json</code>)
 */
public class BMNMetadata {
	private final static JsonParser JSON_PARSER = new JsonParser();
	private final static String ADDON_METADATA_FILE = "bluemap.addon.json";
	private final static String KEY_ID = "id";

	private BMNMetadata() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Gets the addon's ID from the addon metadata file.
	 * (<code>bluemap.addon.json</code>)
	 *
	 * @param classLoader The class loader of the addon
	 * @return The addon's ID
	 * @throws IOException If the metadata file could not be read
	 */
	public static String getAddonID(ClassLoader classLoader) throws IOException {
		return getKey(classLoader, KEY_ID);
	}

	/**
	 * Gets any arbitrary key from the addon metadata file.
	 * (<code>bluemap.addon.json</code>)
	 *
	 * @param classLoader The class loader of the addon
	 * @param key         The key to get
	 * @return The value of the key
	 * @throws IOException If the metadata file could not be read
	 */
	public static String getKey(ClassLoader classLoader, String key) throws IOException {
		try (
				final InputStream in = classLoader.getResourceAsStream(ADDON_METADATA_FILE)
		) {
			if (in == null) throw new IOException("Resource not found: " + ADDON_METADATA_FILE);
			final String json = new String(in.readAllBytes());
			final JsonObject o = JSON_PARSER.parse(json).getAsJsonObject();
			return o.get(key).getAsString();
		}
	}
}
