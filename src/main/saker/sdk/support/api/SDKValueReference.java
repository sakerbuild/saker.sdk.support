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

/**
 * Interface that provides access to derived values from SDKs.
 * <p>
 * The interface is a common subinterface to more concrete specializations. It defines the {@link #getValue(Map)} method
 * that is responsible for deriving the result object from the information it can access from the argument SDKs.
 * <p>
 * Clients may implement this interface, but they are recommended to not directly implement it. Please refer to the more
 * concrete subinterfaces of this.
 * <p>
 * Implementations should adhere to the {@link #hashCode()} and {@link #equals(Object)} contract. Implementers are also
 * recommended to implement {@link Externalizable}.
 * 
 * @param <T>
 *            The type of the derived value.
 * @since saker.sdk.support 0.8.3
 * @see SDKPathReference
 * @see SDKPropertyReference
 * @see SDKPathCollectionReference
 * @see SDKPropertyCollectionReference
 */
public interface SDKValueReference<T> {
	/**
	 * Gets the derived value based on the arugment SDKs.
	 * <p>
	 * The method can access the information accessible via the argument SDKs and construct the return value based on
	 * that.
	 * <p>
	 * The result can be <code>null</code>, but implementations are encouraged to throw an appropriate exception
	 * instead.
	 * 
	 * @param sdks
	 *            The SDKs to derive the value from.
	 * @return The derived value. May be <code>null</code>.
	 * @throws NullPointerException
	 *             If the argument is <code>null</code>.
	 * @throws Exception
	 *             In case of failure.
	 */
	public T getValue(Map<String, ? extends SDKReference> sdks) throws NullPointerException, Exception;

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
