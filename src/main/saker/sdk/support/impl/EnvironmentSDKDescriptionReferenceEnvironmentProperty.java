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

import saker.build.runtime.environment.EnvironmentProperty;
import saker.build.runtime.environment.SakerEnvironment;
import saker.sdk.support.api.EnvironmentSDKDescription;
import saker.sdk.support.api.SDKReference;

public class EnvironmentSDKDescriptionReferenceEnvironmentProperty
		implements EnvironmentProperty<SDKReference>, Externalizable {
	private static final long serialVersionUID = 1L;

	private EnvironmentSDKDescription description;

	/**
	 * For {@link Externalizable}.
	 */
	public EnvironmentSDKDescriptionReferenceEnvironmentProperty() {
	}

	public EnvironmentSDKDescriptionReferenceEnvironmentProperty(EnvironmentSDKDescription description) {
		this.description = description;
	}

	@Override
	public SDKReference getCurrentValue(SakerEnvironment environment) throws Exception {
		return description.getSDK(environment);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(description);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		description = (EnvironmentSDKDescription) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		EnvironmentSDKDescriptionReferenceEnvironmentProperty other = (EnvironmentSDKDescriptionReferenceEnvironmentProperty) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (description != null ? "description=" + description : "") + "]";
	}

}
