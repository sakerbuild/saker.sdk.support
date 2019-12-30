package saker.sdk.support.api;

import java.io.Externalizable;
import java.util.Objects;

import saker.build.exception.InvalidPathFormatException;
import saker.build.file.path.SakerPath;
import saker.sdk.support.impl.SimpleSDKPathReference;

/**
 * Interface that encloses a reference to an {@linkplain SDKReference#getPath(String) SDK path}.
 * <p>
 * Instances of this interface are used to configure tasks with input paths that are resolved from an
 * {@link SDKReference}.
 * <p>
 * Clients may implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 * <p>
 * Use {@link #create} to create a new instance.
 */
public interface SDKPathReference {
	/**
	 * Gets the SDK name for which this path reference is associated with.
	 * 
	 * @return The SDK name.
	 * @see SDKSupportUtils#getSDKNameComparator()
	 */
	public String getSDKName();

	/**
	 * Gets the path from the argument {@link SDKReference}.
	 * <p>
	 * The implementation will retrieve the path in an implementation dependent manner.
	 * <p>
	 * The argument SDK is the one that is associated with the {@linkplain #getSDKName() SDK name} in the context of the
	 * operation.
	 * <p>
	 * If the path cannot be retrieved from the SDK, then the method may return <code>null</code> or throw an exception.
	 * 
	 * @param sdk
	 *            The SDK.
	 * @return The resolved path or <code>null</code> if the resolution failed.
	 * @throws Exception
	 *             If the operation failed.
	 */
	public SakerPath getPath(SDKReference sdk) throws Exception;

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	/**
	 * Creates a new {@link SDKPathReference} that resolves the path for a given identifier.
	 * <p>
	 * The created path references simply uses {@link SDKReference#getPath(String)} to resolve the path.
	 * 
	 * @param sdkname
	 *            The SDK name.
	 * @param pathidentifier
	 *            The path identifier.
	 * @return The created path reference.
	 * @throws NullPointerException
	 *             If any of the arguments are <code>null</code>.
	 */
	public static SDKPathReference create(String sdkname, String pathidentifier) throws NullPointerException {
		return create(sdkname, pathidentifier, null);
	}

	/**
	 * Creates a new {@link SDKPathReference} that resolves the path for a given identifier and resolves the argument
	 * path against it.
	 * <p>
	 * The created path references simply uses {@link SDKReference#getPath(String)} to resolve the path, and if the
	 * <code>relative</code> argument is not <code>null</code>, the that will be resolved against the result.
	 * 
	 * @param sdkname
	 *            The SDK name.
	 * @param pathidentifier
	 *            The path identifier.
	 * @param relative
	 *            The relative path to resolve against the one retrieved from the SDK or <code>null</code> to don't
	 *            perform path resolution.
	 * @return The created path reference.
	 * @throws NullPointerException
	 *             If the SDK name or path identifier is <code>null</code>.
	 * @throws InvalidPathFormatException
	 *             If the relative path argument is not <code>null</code>, and not relative.
	 * @see SakerPath#resolve(SakerPath)
	 */
	public static SDKPathReference create(String sdkname, String pathidentifier, SakerPath relative)
			throws NullPointerException, InvalidPathFormatException {
		Objects.requireNonNull(sdkname, "sdk name");
		Objects.requireNonNull(pathidentifier, "sdk path identifier");
		return new SimpleSDKPathReference(sdkname, pathidentifier, relative);
	}
}
