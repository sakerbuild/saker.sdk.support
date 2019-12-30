package saker.sdk.support.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;

import saker.build.file.path.SakerPath;
import saker.build.file.provider.SakerPathFiles;
import saker.sdk.support.api.SDKPathReference;
import saker.sdk.support.api.SDKReference;
import saker.sdk.support.api.SDKSupportUtils;

public class SimpleSDKPathReference implements SDKPathReference, Externalizable {
	private static final long serialVersionUID = 1L;

	private String sdkName;
	private String directoryIdentifier;
	private SakerPath relative;

	/**
	 * For {@link Externalizable}.
	 */
	public SimpleSDKPathReference() {
	}

	public SimpleSDKPathReference(String sdkName, String directoryIdentifier, SakerPath relative) {
		this.sdkName = sdkName.toLowerCase(Locale.ENGLISH);
		this.directoryIdentifier = directoryIdentifier;
		if (relative != null) {
			SakerPathFiles.requireRelativePath(relative);
			this.relative = SakerPath.EMPTY.equals(relative) ? null : relative;
		}
	}

	@Override
	public String getSDKName() {
		return sdkName;
	}

	@Override
	public SakerPath getPath(SDKReference sdk) throws Exception {
		SakerPath result = sdk.getPath(directoryIdentifier);
		if (result != null && relative != null) {
			return result.resolve(relative);
		}
		return result;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(sdkName);
		out.writeObject(directoryIdentifier);
		out.writeObject(relative);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		sdkName = (String) in.readObject();
		directoryIdentifier = (String) in.readObject();
		relative = (SakerPath) in.readObject();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//do not include the sdk name in the hash code, as it is compared in an ignore case manner
		result = prime * result + ((directoryIdentifier == null) ? 0 : directoryIdentifier.hashCode());
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
		if (directoryIdentifier == null) {
			if (other.directoryIdentifier != null)
				return false;
		} else if (!directoryIdentifier.equals(other.directoryIdentifier))
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
		return getClass().getSimpleName() + "[" + sdkName + ":" + directoryIdentifier
				+ (relative == null ? "" : " / " + relative) + "]";
	}

}
