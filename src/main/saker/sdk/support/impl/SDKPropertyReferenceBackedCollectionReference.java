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
package saker.sdk.support.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;

import saker.build.thirdparty.saker.util.ImmutableUtils;
import saker.build.thirdparty.saker.util.io.SerialUtils;
import saker.sdk.support.api.SDKPropertyCollectionReference;
import saker.sdk.support.api.SDKPropertyReference;
import saker.sdk.support.api.SDKReference;

public final class SDKPropertyReferenceBackedCollectionReference
		implements SDKPropertyCollectionReference, Externalizable {
	private static final long serialVersionUID = 1L;

	private SDKPropertyReference propertyReference;

	/**
	 * For {@link Externalizable}.
	 */
	public SDKPropertyReferenceBackedCollectionReference() {
	}

	public SDKPropertyReferenceBackedCollectionReference(SDKPropertyReference pathref) {
		this.propertyReference = pathref;
	}

	@Override
	public Collection<String> getValue(Map<String, ? extends SDKReference> sdks)
			throws NullPointerException, Exception {
		String value = propertyReference.getValue(sdks);
		if (value == null) {
			return null;
		}
		return ImmutableUtils.singletonNavigableSet(value);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(propertyReference);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		propertyReference = SerialUtils.readExternalObject(in);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyReference == null) ? 0 : propertyReference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SDKPropertyReferenceBackedCollectionReference other = (SDKPropertyReferenceBackedCollectionReference) obj;
		if (propertyReference == null) {
			if (other.propertyReference != null)
				return false;
		} else if (!propertyReference.equals(other.propertyReference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + propertyReference + "]";
	}

}