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
import java.util.Objects;

import saker.build.runtime.environment.EnvironmentProperty;
import saker.build.runtime.environment.SakerEnvironment;
import saker.sdk.support.api.EnvironmentSDKDescription;
import saker.sdk.support.api.SDKDescriptionVisitor;
import saker.sdk.support.api.SDKReference;

public class PropertyEnvironmentSDKDescription implements EnvironmentSDKDescription, Externalizable {
	private static final long serialVersionUID = 1L;

	private EnvironmentProperty<?> property;

	/**
	 * For {@link Externalizable}.
	 */
	public PropertyEnvironmentSDKDescription() {
	}

	public PropertyEnvironmentSDKDescription(EnvironmentProperty<? extends SDKReference> property) {
		Objects.requireNonNull(property, "property");
		this.property = property;
	}

	@Override
	public void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public SDKReference getSDK(SakerEnvironment environment) throws Exception {
		Object result = environment.getEnvironmentPropertyCurrentValue(property);
		if (!(result instanceof SDKReference)) {
			throw new ClassCastException("SDK environment property result doesn't implement "
					+ SDKReference.class.getName() + " : " + result);
		}
		return (SDKReference) result;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(property);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		property = (EnvironmentProperty<?>) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
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
		PropertyEnvironmentSDKDescription other = (PropertyEnvironmentSDKDescription) obj;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + property + "]";
	}

}
