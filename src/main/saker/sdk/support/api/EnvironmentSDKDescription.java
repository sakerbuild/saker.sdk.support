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

import saker.build.runtime.environment.EnvironmentProperty;
import saker.build.runtime.environment.SakerEnvironment;
import saker.sdk.support.impl.PropertyEnvironmentSDKDescription;

/**
 * {@link SDKDescription} that represents an SDK that is resolved from a given build environment.
 * <p>
 * This SDK description interface defines a strategy using which the {@link SDKReference} can be acquired from a given
 * {@linkplain SakerEnvironment build environment}.
 * <p>
 * Clients may implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 * <p>
 * You can use {@link #create(EnvironmentProperty)} to create a new instance without implementing this interface.
 */
public interface EnvironmentSDKDescription extends SDKDescription {
	/**
	 * Gets the SDK reference based on the argument build environment.
	 * <p>
	 * The implementation may retrieve an {@link SDKReference} in any way it sees fit.
	 * 
	 * @param environment
	 *            The build environment.
	 * @return The SDK reference. Never <code>null</code>.
	 * @throws Exception
	 *             If the operation failed. E.g. the SDK was not found in the argument environment.
	 */
	public SDKReference getSDK(SakerEnvironment environment) throws Exception;

	@Override
	public default void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	/**
	 * Creates a new {@link EnvironmentSDKDescription} that retrieves the {@link SDKReference} using the argument
	 * {@link EnvironmentProperty}.
	 * <p>
	 * If the property doesn't return and instance of {@link SDKReference}, then a {@link ClassCastException} is thrown
	 * when it is being retrieved.
	 * 
	 * @param property
	 *            The property that is used to retrieve the {@link SDKReference}.
	 * @return The created SDK description.
	 * @throws NullPointerException
	 *             If the argument is <code>null</code>.
	 */
	public static EnvironmentSDKDescription create(EnvironmentProperty<? extends SDKReference> property)
			throws NullPointerException {
		return new PropertyEnvironmentSDKDescription(property);
	}
}
