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

import de.bluecolored.bluemap.core.logger.AbstractLogger;
import de.bluecolored.bluemap.core.logger.Logger;

import java.io.IOException;

/**
 * A logger for BlueMap Native Addons
 */
public class BMNLogger extends AbstractLogger {
	private final Logger bmGlobalLogger = Logger.global;
	private final String prefix;

	/**
	 * Creates a new logger for a BlueMap Native Addon
	 * <p>
	 * If you're not using BlueMap itself as the addon loader, please use {@link BMNLogger(String)} instead
	 *
	 * @param classLoader The class loader of the addon
	 * @throws IOException If the addon ID could not be retrieved from the <code>bluemap.addon.json</code> file
	 */
	public BMNLogger(ClassLoader classLoader) throws IOException {
		String addonID = BMNMetadata.getAddonID(classLoader);
		prefix = "[" + addonID + "] ";
	}


	/**
	 * Creates a new logger for a BlueMap Addon
	 * <p>
	 * If you're using BlueMap itself as the addon loader, please use {@link BMNLogger(ClassLoader)} instead
	 *
	 * @param addonID The ID of this addon, for use as the prefix of the log messages
	 */
	public BMNLogger(String addonID) {
		prefix = "[" + addonID + "] ";
	}

	/**
	 * Logs an error message
	 *
	 * @param message The message to log
	 */
	public void logError(String message) {
		bmGlobalLogger.logError(prefix + message, null);
	}

	/**
	 * Logs an error message with an exception
	 *
	 * @param message   The message to log
	 * @param throwable The exception to log
	 */
	@Override
	public void logError(String message, Throwable throwable) {
		bmGlobalLogger.logError(prefix + message, throwable);
	}

	/**
	 * Logs a warning message
	 *
	 * @param message The message to log
	 */
	@Override
	public void logWarning(String message) {
		bmGlobalLogger.logWarning(prefix + message);
	}

	/**
	 * Logs an info message
	 *
	 * @param message The message to log
	 */
	@Override
	public void logInfo(String message) {
		bmGlobalLogger.logInfo(prefix + message);
	}

	/**
	 * Logs a debug message
	 *
	 * @param message The message to log
	 */
	@Override
	public void logDebug(String message) {
		bmGlobalLogger.logDebug(prefix + message);
	}
}
