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
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import saker.build.file.path.SakerPath;
import saker.build.runtime.environment.EnvironmentProperty;
import saker.build.runtime.environment.SakerEnvironment;
import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.sdk.support.impl.EnvironmentSDKDescriptionReferenceEnvironmentProperty;

/**
 * Utility class providing functions for SDK management.
 */
public class SDKSupportUtils {
	private SDKSupportUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets a {@link Comparator} that should be used when comparing SDK names.
	 * <p>
	 * The SDK API defines that the SDK names should be compared in an ignore-case manner. This method returns a
	 * comparator that compares the SDK name strings appropriately.
	 * <p>
	 * The returned {@link Comparator} is {@link Externalizable}.
	 * 
	 * @return The comparator.
	 */
	public static Comparator<String> getSDKNameComparator() {
		return SDKNameComparator.INSTANCE;
	}

	/**
	 * Gets an {@link EnvironmentProperty} that computes its value using the argument {@link EnvironmentSDKDescription}.
	 * <p>
	 * The created environment property will use {@link EnvironmentSDKDescription#getSDK(SakerEnvironment)} to compute
	 * its value. The created property is basically a wrapper.
	 * 
	 * @param sdkdescription
	 *            The SDK description.
	 * @return The created environment property.
	 * @throws NullPointerException
	 *             If the argument is <code>null</code>.
	 */
	public static EnvironmentProperty<? extends SDKReference> getEnvironmentSDKDescriptionReferenceEnvironmentProperty(
			EnvironmentSDKDescription sdkdescription) throws NullPointerException {
		Objects.requireNonNull(sdkdescription, "sdk description");
		return new EnvironmentSDKDescriptionReferenceEnvironmentProperty(sdkdescription);
	}

	/**
	 * Gets the path for the given {@linkplain SDKPathReference path reference} from the specified SDKs.
	 * <p>
	 * The function will query the path that the {@link SDKPathReference} references from the SDK with the associated
	 * name.
	 * <p>
	 * The argument SDK map should be ordered using {@link #getSDKNameComparator()}. This function doesn't handle if the
	 * map uses case-sensitive keys.
	 * 
	 * @param sdkpathref
	 *            The path reference to query.
	 * @param sdks
	 *            The SDKs that is associated with the enclosing operation.
	 * @return The path that was found for the given path reference.
	 * @throws NullPointerException
	 *             If the {@link SDKPathReference} is <code>null</code>, or the
	 *             {@linkplain SDKPathReference#getSDKName() SDK name} that it contains is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the SDK with the name, the SDK path wasn't found or querying the SDK path failed with an
	 *             exception.
	 */
	public static SakerPath getSDKPathReferencePath(SDKPathReference sdkpathref,
			Map<String, ? extends SDKReference> sdks) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(sdkpathref, "sdk path reference");
		String sdkname = sdkpathref.getSDKName();
		Objects.requireNonNull(sdkname, "sdk name");
		SDKReference sdkref = ObjectUtils.getMapValue(sdks, sdkname);
		if (sdkref == null) {
			throw new IllegalArgumentException("SDK not found for name: " + sdkname);
		}
		try {
			SakerPath path = sdkpathref.getPath(sdkref);
			if (path == null) {
				throw new IllegalArgumentException("No SDK path found for: " + sdkpathref + " in " + sdkref);
			}
			return path;
		} catch (Exception e) {
			throw new IllegalArgumentException("No SDK path found for: " + sdkpathref + " in " + sdkref, e);
		}
	}

	/**
	 * Gets the property value for the given {@linkplain SDKPropertyReference property reference} from the specified
	 * SDKs.
	 * <p>
	 * The function will query the property that the {@link SDKPropertyReference} references from the SDK with the
	 * associated name.
	 * <p>
	 * The argument SDK map should be ordered using {@link #getSDKNameComparator()}. This function doesn't handle if the
	 * map uses case-sensitive keys.
	 * 
	 * @param sdkpropertyref
	 *            The property reference to query.
	 * @param sdks
	 *            The SDKs that is associated with the enclosing operation.
	 * @return The property value that was found for the given property reference.
	 * @throws NullPointerException
	 *             If the {@link SDKPropertyReference} is <code>null</code>, or the
	 *             {@linkplain SDKPropertyReference#getSDKName() SDK name} that it contains is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the SDK with the name, the SDK property wasn't found or querying the SDK property failed with an
	 *             exception.
	 */
	public static String getSDKPropertyReferenceProperty(SDKPropertyReference sdkpropertyref,
			Map<String, ? extends SDKReference> sdks) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(sdkpropertyref, "sdk property reference");
		String sdkname = sdkpropertyref.getSDKName();
		Objects.requireNonNull(sdkname, "sdk name");
		SDKReference sdkref = ObjectUtils.getMapValue(sdks, sdkname);
		if (sdkref == null) {
			throw new IllegalArgumentException("SDK not found for name: " + sdkname);
		}
		try {
			String property = sdkpropertyref.getProperty(sdkref);
			if (property == null) {
				throw new IllegalArgumentException("No SDK property found for: " + sdkpropertyref + " in " + sdkref);
			}
			return property;
		} catch (Exception e) {
			throw new IllegalArgumentException("No SDK property found for: " + sdkpropertyref + " in " + sdkref, e);
		}
	}

	private static final class SDKNameComparator implements Comparator<String>, Externalizable {
		private static final long serialVersionUID = 1L;

		public static final SDKNameComparator INSTANCE = new SDKNameComparator();

		/**
		 * For {@link Externalizable}.
		 */
		public SDKNameComparator() {
		}

		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		}

		@Override
		public int hashCode() {
			return getClass().getName().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return ObjectUtils.isSameClass(this, obj);
		}
	}
}
