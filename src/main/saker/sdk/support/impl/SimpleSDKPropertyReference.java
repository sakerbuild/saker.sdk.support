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

import saker.sdk.support.api.SDKPropertyReference;
import saker.sdk.support.api.SDKReference;
import saker.sdk.support.api.SDKSupportUtils;

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
		this.sdkName = sdkName;
		this.propertyIdentifier = directoryIdentifier;
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getSDKName() {
		return sdkName;
	}

	@Override
	@SuppressWarnings("deprecation")
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
		//do not include the sdk name in the hash code, as it is compared in an ignore case manner
		result = prime * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
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
		if (SDKSupportUtils.getSDKNameComparator().compare(this.sdkName, other.sdkName) != 0) {
			return false;
		}
		if (propertyIdentifier == null) {
			if (other.propertyIdentifier != null)
				return false;
		} else if (!propertyIdentifier.equals(other.propertyIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + sdkName + ":" + propertyIdentifier + "]";
	}

}
