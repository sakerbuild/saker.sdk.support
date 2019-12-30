package saker.sdk.support.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import saker.sdk.support.api.ResolvedSDKDescription;
import saker.sdk.support.api.SDKDescriptionVisitor;
import saker.sdk.support.api.SDKReference;

public class SimpleResolvedSDKDescription implements ResolvedSDKDescription, Externalizable {
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
