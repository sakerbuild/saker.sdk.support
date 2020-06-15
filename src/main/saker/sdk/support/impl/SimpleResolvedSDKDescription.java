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

import saker.sdk.support.api.SDKDescriptionVisitor;
import saker.sdk.support.api.SDKReference;

@SuppressWarnings("deprecation")
public class SimpleResolvedSDKDescription implements saker.sdk.support.api.ResolvedSDKDescription, Externalizable {
	private static final long serialVersionUID = 1L;

	private SDKReference sdkReference;

	/**
	 * For {@link Externalizable}.
	 */
	public SimpleResolvedSDKDescription() {
	}

	public SimpleResolvedSDKDescription(SDKReference sdkReference) {
		this.sdkReference = sdkReference;
	}

	@Override
	public void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public SDKReference getSDKReference() {
		return sdkReference;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(sdkReference);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		sdkReference = (SDKReference) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sdkReference == null) ? 0 : sdkReference.hashCode());
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
		SimpleResolvedSDKDescription other = (SimpleResolvedSDKDescription) obj;
		if (sdkReference == null) {
			if (other.sdkReference != null)
				return false;
		} else if (!sdkReference.equals(other.sdkReference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (sdkReference != null ? "sdkReference=" + sdkReference : "") + "]";
	}

}
