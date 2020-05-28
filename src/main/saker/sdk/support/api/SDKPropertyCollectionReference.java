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
import java.util.Collection;
import java.util.Objects;

import saker.sdk.support.impl.SDKPropertyReferenceBackedCollectionReference;

/**
 * Interface providing access to a collection of {@link String} properties derived from SDKs.
 * <p>
 * The interface is similar to {@link SDKPropertyReference}, however, it can return a variable number of properties
 * instead of one. It can be used when the number of properties that are associated with a given operation cannot be
 * determined statically, and may be different amount based on other properties of the SDKs.
 * <p>
 * Clients may implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 * <p>
 * A delegate instance from {@link SDKPropertyReference} can be created using {@link #valueOf(SDKPropertyReference)}.
 * 
 * @since saker.sdk.support 0.8.3
 */
public interface SDKPropertyCollectionReference extends SDKValueReference<Collection<String>> {
	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	/**
	 * Creates a new instance based on the argument {@link SDKPropertyReference}.
	 * <p>
	 * The returned property collection reference will return a singleton set that contains the proeprty returned by the
	 * argument.
	 * 
	 * @param properf
	 *            The property reference.
	 * @return The delegate property collection reference.
	 * @throws NullPointerException
	 *             If the argument is <code>null</code>.
	 */
	public static SDKPropertyCollectionReference valueOf(SDKPropertyReference properf) throws NullPointerException {
		Objects.requireNonNull(properf, "property reference");
		return new SDKPropertyReferenceBackedCollectionReference(properf);
	}
}
