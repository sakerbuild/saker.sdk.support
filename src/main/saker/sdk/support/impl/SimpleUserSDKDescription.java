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
import java.util.Collections;
import java.util.Map;

import saker.build.file.path.SakerPath;
import saker.build.thirdparty.saker.util.ImmutableUtils;
import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.build.thirdparty.saker.util.io.SerialUtils;
import saker.sdk.support.api.SDKDescriptionVisitor;
import saker.sdk.support.api.UserSDKDescription;
import saker.std.api.environment.qualifier.EnvironmentQualifier;

public class SimpleUserSDKDescription implements UserSDKDescription, Externalizable {
	private static final long serialVersionUID = 1L;

	private EnvironmentQualifier qualifier;
	private Map<String, SakerPath> paths;
	private Map<String, String> properties;

	/**
	 * For {@link Externalizable}.
	 */
	public SimpleUserSDKDescription() {
	}

	public SimpleUserSDKDescription(EnvironmentQualifier qualifier, Map<String, SakerPath> paths,
			Map<String, String> properties) {
		this.qualifier = qualifier;
		this.paths = ObjectUtils.isNullOrEmpty(paths) ? Collections.emptyMap()
				: ImmutableUtils.makeImmutableNavigableMap(paths);
		this.properties = ObjectUtils.isNullOrEmpty(properties) ? Collections.emptyMap()
				: ImmutableUtils.makeImmutableNavigableMap(properties);
	}

	@Override
	public void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public EnvironmentQualifier getQualifier() {
		return qualifier;
	}

	@Override
	public Map<String, SakerPath> getPaths() {
		return paths;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(qualifier);
		SerialUtils.writeExternalMap(out, paths);
		SerialUtils.writeExternalMap(out, properties);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		qualifier = (EnvironmentQualifier) in.readObject();
		paths = SerialUtils.readExternalImmutableNavigableMap(in);
		properties = SerialUtils.readExternalImmutableNavigableMap(in);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paths == null) ? 0 : paths.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((qualifier == null) ? 0 : qualifier.hashCode());
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
		SimpleUserSDKDescription other = (SimpleUserSDKDescription) obj;
		if (paths == null) {
			if (other.paths != null)
				return false;
		} else if (!paths.equals(other.paths))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (qualifier == null) {
			if (other.qualifier != null)
				return false;
		} else if (!qualifier.equals(other.qualifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (qualifier != null ? "qualifier=" + qualifier + ", " : "")
				+ (paths != null ? "paths=" + paths + ", " : "")
				+ (properties != null ? "properties=" + properties : "") + "]";
	}

}
