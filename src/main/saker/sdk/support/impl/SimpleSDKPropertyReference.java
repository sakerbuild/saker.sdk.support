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
import java.util.Locale;

import saker.sdk.support.api.SDKPropertyReference;
import saker.sdk.support.api.SDKReference;

public class SimpleSDKPropertyReference implements SDKPropertyReference, Externalizable {
	private static final long serialVersionUID = 1L;

	private String sdkName;
	private String propertyIdentifier;

	/**
	 * For {@link Externalizable}.
	 */
	public SimpleSDKPropertyReference() {
	}

	public SimpleSDKPropertyReference(String sdkName, String directoryIdentifier) {
		this.sdkName = sdkName.toLowerCase(Locale.ENGLISH);
		this.propertyIdentifier = directoryIdentifier;
	}

	@Override
	public String getSDKName() {
		return sdkName;
	}

	@Override
	public String getProperty(SDKReference sdk) throws Exception {
		return sdk.getProperty(propertyIdentifier);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(sdkName);
		out.writeObject(propertyIdentifier);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		sdkName = (String) in.readObject();
		propertyIdentifier = (String) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
		result = prime * result + ((sdkName == null) ? 0 : sdkName.hashCode());
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
		SimpleSDKPropertyReference other = (SimpleSDKPropertyReference) obj;
		if (propertyIdentifier == null) {
			if (other.propertyIdentifier != null)
				return false;
		} else if (!propertyIdentifier.equals(other.propertyIdentifier))
			return false;
		if (sdkName == null) {
			if (other.sdkName != null)
				return false;
		} else if (!sdkName.equals(other.sdkName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + sdkName + ":" + propertyIdentifier + "]";
	}

}
