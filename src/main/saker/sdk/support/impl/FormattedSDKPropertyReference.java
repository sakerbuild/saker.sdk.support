package saker.sdk.support.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;

import saker.build.thirdparty.saker.util.io.SerialUtils;
import saker.sdk.support.api.SDKPropertyReference;
import saker.sdk.support.api.SDKReference;
import saker.sdk.support.api.SDKValueReference;
import saker.sdk.support.api.exc.SDKValueNotFoundException;

public class FormattedSDKPropertyReference implements SDKPropertyReference, Externalizable {
	private static final long serialVersionUID = 1L;

	private String format;
	private List<? extends SDKValueReference<?>> arguments;

	/**
	 * For {@link Externalizable}.
	 */
	public FormattedSDKPropertyReference() {
	}

	public FormattedSDKPropertyReference(String format, List<? extends SDKValueReference<?>> arguments) {
		this.format = format;
		this.arguments = arguments;
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getSDKName() {
		throw new UnsupportedOperationException("Deprecated. Use getValue() instead.");
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getProperty(SDKReference sdk) throws Exception {
		throw new UnsupportedOperationException("Deprecated. Use getValue() instead.");
	}

	@Override
	public String getValue(Map<String, ? extends SDKReference> sdks) throws NullPointerException, Exception {
		Object[] args = new Object[arguments.size()];
		int i = 0;
		for (SDKValueReference<?> valref : arguments) {
			Object val = valref.getValue(sdks);
			if (val == null) {
				throw new SDKValueNotFoundException("SDK value not found for: " + valref);
			}
			//don't need to be stringized, formats like %s converts it to string automatically
			args[i++] = val;
		}
		return String.format(null, format, args);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(format);
		SerialUtils.writeExternalCollection(out, arguments);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		format = SerialUtils.readExternalObject(in);
		arguments = SerialUtils.readExternalImmutableList(in);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
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
		FormattedSDKPropertyReference other = (FormattedSDKPropertyReference) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (format != null ? "format=" + format + ", " : "")
				+ (arguments != null ? "arguments=" + arguments : "") + "]";
	}

}
