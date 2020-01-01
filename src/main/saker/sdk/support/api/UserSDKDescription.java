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

import java.util.Map;

import saker.build.file.path.SakerPath;
import saker.build.task.TaskFactory;
import saker.sdk.support.impl.SimpleUserSDKDescription;
import saker.sdk.support.impl.SimpleUserSDKReference;
import saker.std.api.environment.qualifier.EnvironmentQualifier;

/**
 * {@link SDKDescription} that encloses a predefined set of paths and properties.
 * <p>
 * An user SDK description is a fixed set of paths and properties for the defined identifiers. It also contains an
 * {@linkplain EnvironmentQualifier environment qualifier} that specifies the environment for which the SDK is defined
 * for.
 * <p>
 * Using user SDKs may cause tasks use only build environments that the {@linkplain #getQualifier() environment
 * qualifier} deems suitable. This may cause {@linkplain TaskFactory#CAPABILITY_REMOTE_DISPATCHABLE remote dispatchable}
 * tasks to not use build clusters, or tasks that are not remote dispatchable to fail.
 * <p>
 * Clients shouldn't implement this interface.
 * <p>
 * Use {@link #create(EnvironmentQualifier, Map, Map)} to create a new instance.
 */
public interface UserSDKDescription extends SDKDescription {
	/**
	 * Gets the qualifier for this user SDK.
	 * <p>
	 * The returned qualifier may be <code>null</code>, in which case the user SDK should be interpreted to be
	 * associated with the local environment.
	 * 
	 * @return The qualifier or <code>null</code> if only the local environment should be used.
	 */
	public EnvironmentQualifier getQualifier();

	/**
	 * Gets the paths mapped to their identifiers for this SDK.
	 * 
	 * @return The paths for the associated identifiers.
	 */
	public Map<String, SakerPath> getPaths();

	/**
	 * Gets the properties mapped to their identifiers for this SDK.
	 * 
	 * @return The properties for the associated identifiers.
	 */
	public Map<String, String> getProperties();

	@Override
	public default void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Creates a new SDK description for the specified properties.
	 * <p>
	 * If any of the {@link Map} arguments are <code>null</code>, an empty map is used in place of them.
	 * 
	 * @param qualifier
	 *            The {@linkplain #getQualifier() environment qualifier}.
	 * @param paths
	 *            The {@linkplain #getPaths() SDK paths}.
	 * @param properties
	 *            The {@linkplain #getProperties() SDK properties}.
	 * @return The created SDK description.
	 */
	public static UserSDKDescription create(EnvironmentQualifier qualifier, Map<String, SakerPath> paths,
			Map<String, String> properties) {
		return new SimpleUserSDKDescription(qualifier, paths, properties);
	}

	/**
	 * Creates a new {@link SDKReference} for the given paths and properties.
	 * 
	 * @param paths
	 *            The {@linkplain SDKReference#getPath(String) SDK paths}.
	 * @param properties
	 *            The {@linkplain SDKReference#getProperty(String) SDK properties}
	 * @return The created SDK reference.
	 */
	public static SDKReference createSDKReference(Map<String, SakerPath> paths, Map<String, String> properties) {
		return new SimpleUserSDKReference(paths, properties);
	}
}
