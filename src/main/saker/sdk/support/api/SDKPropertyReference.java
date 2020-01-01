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
import java.util.Objects;

import saker.sdk.support.impl.SimpleSDKPropertyReference;

/**
 * Interface that encloses a reference to an {@linkplain SDKReference#getProperty(String) SDK property}.
 * <p>
 * Instances of this interface are used to configure tasks with input values (properties) that are resolved from an
 * {@link SDKReference}.
 * <p>
 * Clients may implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 * <p>
 * Use {@link #create} to create a new instance.
 */
public interface SDKPropertyReference {
	/**
	 * Gets the SDK name for which this property reference is associated with.
	 * 
	 * @return The SDK name.
	 * @see SDKSupportUtils#getSDKNameComparator()
	 */
	public String getSDKName();

	/**
	 * Gets the property value from the argument {@link SDKReference}.
	 * <p>
	 * The implementation will retrieve the property in an implementation dependent manner.
	 * <p>
	 * The argument SDK is the one that is associated with the {@linkplain #getSDKName() SDK name} in the context of the
	 * operation.
	 * <p>
	 * If the property cannot be retrieved from the SDK, then the method may return <code>null</code> or throw an
	 * exception.
	 * 
	 * @param sdk
	 *            The SDK.
	 * @return The resolved property or <code>null</code> if the resolution failed.
	 * @throws Exception
	 *             If the operation failed.
	 */
	public String getProperty(SDKReference sdk) throws Exception;

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	/**
	 * Creates a new {@link SDKPropertyReference} that resolves the property for a given identifier.
	 * <p>
	 * The created property references simply uses {@link SDKReference#getProperty(String)} to resolve the property.
	 * 
	 * @param sdkname
	 *            The SDK name.
	 * @param propertyidentifier
	 *            The property identifier.
	 * @return The created property reference.
	 * @throws NullPointerException
	 *             If any of the arguments are <code>null</code>.
	 */
	public static SDKPropertyReference create(String sdkname, String propertyidentifier) throws NullPointerException {
		Objects.requireNonNull(sdkname, "sdk name");
		Objects.requireNonNull(propertyidentifier, "sdk property identifier");
		return new SimpleSDKPropertyReference(sdkname, propertyidentifier);
	}
}
