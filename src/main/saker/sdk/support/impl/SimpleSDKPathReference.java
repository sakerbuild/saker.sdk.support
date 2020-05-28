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

import saker.build.file.path.SakerPath;
import saker.build.file.provider.SakerPathFiles;
import saker.sdk.support.api.SDKPathReference;
import saker.sdk.support.api.SDKReference;
import saker.sdk.support.api.SDKSupportUtils;

public class SimpleSDKPathReference implements SDKPathReference, Externalizable {
	private static final long serialVersionUID = 1L;

	private String sdkName;
	private String pathIdentifier;
	private SakerPath relative;

	/**
	 * For {@link Externalizable}.
	 */
	public SimpleSDKPathReference() {
	}

	public SimpleSDKPathReference(String sdkName, String directoryIdentifier, SakerPath relative) {
		this.sdkName = sdkName;
		this.pathIdentifier = directoryIdentifier;
		if (relative != null) {
			SakerPathFiles.requireRelativePath(relative);
			this.relative = SakerPath.EMPTY.equals(relative) ? null : relative;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getSDKName() {
		return sdkName;
	}

	@Override
	@SuppressWarnings("deprecation")
	public SakerPath getPath(SDKReference sdk) throws Exception {
		SakerPath result = sdk.getPath(pathIdentifier);
		if (result != null && relative != null) {
			return result.resolve(relative);
		}
		return result;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(sdkName);
		out.writeObject(pathIdentifier);
		out.writeObject(relative);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		sdkName = (String) in.readObject();
		pathIdentifier = (String) in.readObject();
		relative = (SakerPath) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//do not include the sdk name in the hash code, as it is compared in an ignore case manner
		result = prime * result + ((pathIdentifier == null) ? 0 : pathIdentifier.hashCode());
		result = prime * result + ((relative == null) ? 0 : relative.hashCode());
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
		SimpleSDKPathReference other = (SimpleSDKPathReference) obj;
		if (SDKSupportUtils.getSDKNameComparator().compare(this.sdkName, other.sdkName) != 0) {
			return false;
		}
		if (pathIdentifier == null) {
			if (other.pathIdentifier != null)
				return false;
		} else if (!pathIdentifier.equals(other.pathIdentifier))
			return false;
		if (relative == null) {
			if (other.relative != null)
				return false;
		} else if (!relative.equals(other.relative))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + sdkName + ":" + pathIdentifier
				+ (relative == null ? "" : " / " + relative) + "]";
	}

}
