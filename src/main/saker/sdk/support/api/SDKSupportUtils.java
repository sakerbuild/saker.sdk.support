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
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.Map.Entry;

import saker.build.file.path.SakerPath;
import saker.build.runtime.environment.EnvironmentProperty;
import saker.build.runtime.environment.SakerEnvironment;
import saker.build.task.EnvironmentSelectionResult;
import saker.build.task.TaskExecutionEnvironmentSelector;
import saker.build.task.TaskFactory;
import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.sdk.support.impl.EnvironmentSDKDescriptionReferenceEnvironmentProperty;
import saker.sdk.support.impl.SDKBasedClusterExecutionEnvironmentSelector;
import saker.std.api.environment.qualifier.AnyEnvironmentQualifier;
import saker.std.api.environment.qualifier.EnvironmentQualifier;
import saker.std.api.environment.qualifier.EnvironmentQualifierVisitor;
import saker.std.api.environment.qualifier.PropertyEnvironmentQualifier;

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

	/**
	 * Gets a {@link TaskExecutionEnvironmentSelector} instance that selects the suitable environments for the argument
	 * SDKs with regard to cluster execution.
	 * <p>
	 * The method will check if the argument SDKs are a good candidate for remote dispatched task execution. If all SDKs
	 * are appropriate, a {@link TaskExecutionEnvironmentSelector} will be returned that can determine whether or not a
	 * given cluster build environment is suitable for execution with the given SDKs.
	 * <p>
	 * If this method returns non-<code>null</code>, then the returned environment selector may be used with the
	 * {@link TaskFactory#CAPABILITY_REMOTE_DISPATCHABLE} task capability. If the result is <code>null</code>, then at
	 * least one SDK description is directly associated with the local build environment.
	 * 
	 * @param sdkdescriptions
	 *            The SDK descriptions.
	 * @return The environment selector for cluster execution or <code>null</code> if the SDK descriptions are not
	 *             suitable for that use-case.
	 * @throws NullPointerException
	 *             If the argument or any of the SDK descriptions are <code>null</code>.
	 * @since saker.sdk.support 0.8.1
	 * @see #pinSDKSelection(EnvironmentSelectionResult, NavigableMap)
	 */
	public static TaskExecutionEnvironmentSelector getSDKBasedClusterExecutionEnvironmentSelector(
			Collection<? extends SDKDescription> sdkdescriptions) throws NullPointerException {
		Objects.requireNonNull(sdkdescriptions, "sdk descriptions");
		boolean[] clusterable = { true };
		for (SDKDescription desc : sdkdescriptions) {
			Objects.requireNonNull(desc, "sdk description");
			desc.accept(new SDKDescriptionVisitor() {
				@Override
				public void visit(EnvironmentSDKDescription description) {
					//stay clusterable as true
				}

				@Override
				public void visit(ResolvedSDKDescription description) {
					clusterable[0] = false;
				}

				@Override
				public void visit(IndeterminateSDKDescription description) {
					SDKDescription basesdk = description.getBaseSDKDescription();
					Objects.requireNonNull(basesdk, "base sdk description");
					basesdk.accept(this);
				}

				@Override
				public void visit(UserSDKDescription description) {
					EnvironmentQualifier qualifier = description.getQualifier();
					if (qualifier == null) {
						//only the local should be used
						clusterable[0] = false;
						return;
					}
					qualifier.accept(new EnvironmentQualifierVisitor() {
						@Override
						public void visit(PropertyEnvironmentQualifier qualifier) {
							//stay clusterable as true 
						}

						@Override
						public void visit(AnyEnvironmentQualifier qualifier) {
							//stay clusterable as true 
						}
					});
				}
			});
			if (!clusterable[0]) {
				return null;
			}
		}
		return new SDKBasedClusterExecutionEnvironmentSelector(sdkdescriptions);
	}

	/**
	 * Pins the SDK descriptions of the resolved {@link IndeterminateSDKDescription indeterminate SDK descriptions}.
	 * <p>
	 * The method will take the argument environment selection result and pin the SDK descriptions of the
	 * {@link IndeterminateSDKDescription IndeterminateSDKDescriptions} that are present.
	 * <p>
	 * This method should be used before batched tasks with common environmental requirements to avoid mistakenly using
	 * different SDKs where the same ones would be expected. The returned descriptions can be used to recreate the
	 * execution environment selector for the actual dispatching.
	 * <p>
	 * One example where this may matter:
	 * 
	 * <pre>
	 * Coordinator SDKs: v1.0, v1.1, v2.0
	 * Cluster1 SDKs:          v1.1, v2.0
	 * Cluster2 SDKs:    v1.0, v1.1
	 * </pre>
	 * 
	 * When different SDKs are installed on the cluster machines, it may occurr that all SDK descriptions resolve to
	 * some version of a given SDK, however, this can cause issues if different versions of it is used in the same
	 * build.
	 * <p>
	 * Without SDK pinning, all machines in the above list would be used. With pinning, only the machines will be used
	 * that have the selected SDK.
	 * <p>
	 * If the environment selection chooses <code>v2.0</code>, then only the Coordinator and Cluster1 is used to
	 * dispatch the tasks. If <code>v1.1</code> is choosen, then all machines are used, and if <code>v1.0</code> is
	 * choosen, only Coordinator and Cluster2 is used.
	 * <p>
	 * We recommend setting up homogeneous cluster environments to avoid such issues, and occasional non-deterministic
	 * builds.
	 * 
	 * @param envselectionresult
	 *            The environment test selection result of the selector returned by
	 *            {@link #getSDKBasedClusterExecutionEnvironmentSelector(Collection)}.
	 * @param sdkdescriptions
	 *            The SDK descriptions that should be pinned.
	 * @return The pinned SDK description map.
	 * @throws NullPointerException
	 *             If any of the arguments are <code>null</code>.
	 * @since saker.sdk.support 0.8.1
	 */
	public static NavigableMap<String, SDKDescription> pinSDKSelection(EnvironmentSelectionResult envselectionresult,
			NavigableMap<String, ? extends SDKDescription> sdkdescriptions) throws NullPointerException {
		Objects.requireNonNull(envselectionresult, "environment selection result");
		Objects.requireNonNull(sdkdescriptions, "sdk descriptions");
		TreeMap<String, SDKDescription> ndescriptions = new TreeMap<>(sdkdescriptions);
		for (Entry<String, SDKDescription> entry : ndescriptions.entrySet()) {
			SDKDescription desc = entry.getValue();
			if (desc instanceof IndeterminateSDKDescription) {
				IndeterminateSDKDescription indeterminate = (IndeterminateSDKDescription) desc;
				SDKReference actualreference = getResolvedSDKReference(desc, envselectionresult);
				SDKDescription pinneddescription = indeterminate.pinSDKDescription(actualreference);
				entry.setValue(pinneddescription);
			}
		}
		return ndescriptions;
	}

	private static SDKReference getResolvedSDKReference(SDKDescription description,
			EnvironmentSelectionResult selectionresult) {
		SDKReference[] result = { null };
		description.accept(new SDKDescriptionVisitor() {
			@Override
			public void visit(EnvironmentSDKDescription description) {
				EnvironmentProperty<? extends SDKReference> envproperty = getEnvironmentSDKDescriptionReferenceEnvironmentProperty(
						description);
				SDKReference resolvedsdkref = (SDKReference) selectionresult.getQualifierEnvironmentProperties()
						.get(envproperty);
				if (resolvedsdkref == null) {
					throw new IllegalArgumentException("Failed to resolve environment SDK description: " + description);
				}
				result[0] = resolvedsdkref;
			}
		});
		return result[0];
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
