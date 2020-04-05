/*
 * Copyright (C) 2020 Bence Sipka
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package saker.sdk.support.api;

import java.io.Externalizable;
import java.util.Map;

import saker.build.file.path.SakerPath;

/**
 * Provides access to the paths and properties of an SDK.
 * <p>
 * An {@link SDKReference} encloses the information provided by an SDK. It is the result of the resolution of an
 * {@link SDKDescription}. An SDK provides access to paths and properties for specific identifiers.
 * <p>
 * The path and property identifiers that an SDK accepts are determined by the actual SDK that is used. Different SDKs
 * usually support different identifiers.
 * <p>
 * Clients are recommended to implement this interface.
 * <p>
 * Clients should adhere to the contract specified by {@link #hashCode()} and {@link #equals(Object)}.
 * <p>
 * Implementations are recommended to implement the {@link Externalizable} interface.
 * <p>
 * <i>Design note:</i> The interface doesn't provide a method to query the supported path and property identifiers. In
 * general, when one wants to use an SDK, the identifiers must be known beforehand, and the need for querying them
 * usually signals an unconventional configuration that this API is not intended to provide support for.
 * 
 * @see UserSDKDescription#createSDKReference(Map, Map)
 */
public interface SDKReference {
	/**
	 * Gets a path provided by this SDK for the given identifier.
	 * <p>
	 * The returned path may be interpreted in an SDK dependent manner, but in most cases, it will be a local path on
	 * the local file system for the JVM that is running the caller code.
	 * <p>
	 * The identifiers may or may not be interpreted in a case-insensitive way by the SDK implementation.
	 * <p>
	 * If the identifier is not recognized by the SDK, it may either return <code>null</code>, or throw an exception.
	 * 
	 * @param identifier
	 *            The identifier for which to look up the path.
	 * @return The path or <code>null</code> if there's no path for the given identifier.
	 * @throws Exception
	 *             If the operation fails.
	 */
	public SakerPath getPath(String identifier) throws Exception;

	/**
	 * Gets a property provided by this SDK for the given identifier.
	 * <p>
	 * The returned property may be interpreted in an SDK dependent manner. Its format depends on the semantics provided
	 * by the SDK.
	 * <p>
	 * The identifiers may or may not be interpreted in a case-insensitive way by the SDK implementation.
	 * <p>
	 * If the identifier is not recognized by the SDK, it may either return <code>null</code>, or throw an exception.
	 * 
	 * @param identifier
	 *            The identifier for which to look up the property.
	 * @return The property value or <code>null</code> if there's no property for the given identifier.
	 * @throws Exception
	 *             If the operation fails.
	 */
	public String getProperty(String identifier) throws Exception;

	@Override
	public int hashCode();

	/**
	 * Checks if this SDK reference is the same as the argument.
	 * <p>
	 * Two SDK references are the same, if they provide access to the same paths and properties for the same
	 * identifiers, and with the same values.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj);
}
