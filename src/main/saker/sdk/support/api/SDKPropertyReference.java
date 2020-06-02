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
package saker.sdk.support.api;

import java.io.Externalizable;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import saker.build.thirdparty.saker.util.ImmutableUtils;
import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.sdk.support.api.exc.SDKNotFoundException;
import saker.sdk.support.impl.FormattedSDKPropertyReference;
import saker.sdk.support.impl.SimpleSDKPropertyReference;

/**
 * Interface that references a {@link String} value derived based on the associated SDKs.
 * <p>
 * Instances of this interface are used to configure tasks with input values (properties) that are resolved from the
 * {@link SDKReference SDKReferences} in the caller context.
 * <p>
 * Clients may implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 * <p>
 * Use {@link #create} to create a new instance.
 */
public interface SDKPropertyReference extends SDKValueReference<String> {
	/**
	 * Gets the SDK name for which this property reference is associated with.
	 * 
	 * @deprecated Call {@link #getValue(Map)} directly instead. (Since saker.sdk.support 0.8.3)
	 * @return The SDK name.
	 * @see SDKSupportUtils#getSDKNameComparator()
	 */
	@Deprecated
	public String getSDKName();

	/**
	 * Gets the property value from the argument {@link SDKReference}.
	 * <p>
	 * The implementation will retrieve the property in an implementation dependent manner.
	 * <p>
	 * The argument SDK is the one that is associated with the {@linkplain #getSDKName() SDK name} in the context of the
	 * operation.
	 * <p>
	 * If the property cannot be retrieved from the SDK, then the method may return <code>null</code> or throw an
	 * exception.
	 * 
	 * @deprecated Call {@link #getValue(Map)} directly instead. (Since saker.sdk.support 0.8.3)
	 * @param sdk
	 *            The SDK.
	 * @return The resolved property or <code>null</code> if the resolution failed.
	 * @throws Exception
	 *             If the operation failed.
	 */
	@Deprecated
	public String getProperty(SDKReference sdk) throws Exception;

	@Override
	public default String getValue(Map<String, ? extends SDKReference> sdks) throws NullPointerException, Exception {
		String sdkname = getSDKName();
		Objects.requireNonNull(sdkname, "sdk name");
		SDKReference sdk = ObjectUtils.getMapValue(sdks, sdkname);
		if (sdk == null) {
			throw new SDKNotFoundException(sdkname);
		}
		return this.getProperty(sdk);
	}

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	/**
	 * Creates a new {@link SDKPropertyReference} that resolves the property for a given identifier.
	 * <p>
	 * The created property references simply uses {@link SDKReference#getProperty(String)} to resolve the property.
	 * 
	 * @param sdkname
	 *            The SDK name.
	 * @param propertyidentifier
	 *            The property identifier.
	 * @return The created property reference.
	 * @throws NullPointerException
	 *             If any of the arguments are <code>null</code>.
	 */
	public static SDKPropertyReference create(String sdkname, String propertyidentifier) throws NullPointerException {
		Objects.requireNonNull(sdkname, "sdk name");
		Objects.requireNonNull(propertyidentifier, "sdk property identifier");
		return new SimpleSDKPropertyReference(sdkname, propertyidentifier);
	}

	/**
	 * Creates a new {@link SDKPropertyReference} that derives the property value using the given format and associated
	 * arguments.
	 * <p>
	 * Each argument {@link SDKValueReference} will be evaluated in the caller SDK context and the results are formatted
	 * with the given {@linkplain Formatter format string}.
	 * 
	 * @param format
	 *            The format string.
	 * @param arguments
	 *            The arguments.
	 * @return The cretaed property reference.
	 * @throws NullPointerException
	 *             If any of the arguments are <code>null</code>.
	 * @since saker.sdk.support 0.8.3
	 */
	public static SDKPropertyReference createWithFormat(String format, List<? extends SDKValueReference<?>> arguments)
			throws NullPointerException {
		Objects.requireNonNull(format, "format");
		Objects.requireNonNull(arguments, "arguments");
		return new FormattedSDKPropertyReference(format, ImmutableUtils.makeImmutableList(arguments));
	}
}
