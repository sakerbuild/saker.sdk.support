package saker.sdk.support.api;

import java.util.Objects;

import saker.build.task.TaskFactory;
import saker.sdk.support.impl.SimpleResolvedSDKDescription;

/**
 * {@link SDKDescription} that provides access to an already resolved {@link SDKReference}.
 * <p>
 * The interface encloses an {@link SDKReference} that was already resolved.
 * <p>
 * Using {@link ResolvedSDKDescription} may cause tasks that are {@linkplain TaskFactory#CAPABILITY_REMOTE_DISPATCHABLE
 * remote dispatchable} to not use build clusters.
 * <p>
 * Clients shouldn't implement this interface.
 * <p>
 * Use {@link #create(SDKReference)} to create a new instance.
 */
public interface ResolvedSDKDescription extends SDKDescription {
	/**
	 * Gets the enclosed SDK reference.
	 * 
	 * @return The SDK reference.
	 */
	public SDKReference getSDKReference();

	@Override
	public default void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Creates a new {@link ResolvedSDKDescription} for the specified {@link SDKReference}.
	 * 
	 * @param reference
	 *            The SDK reference.
	 * @return The created SDK description.
	 * @throws NullPointerException
	 *             If the argument is <code>null</code>.
	 */
	public static ResolvedSDKDescription create(SDKReference reference) throws NullPointerException {
		Objects.requireNonNull(reference, "sdk reference");
		return new SimpleResolvedSDKDescription(reference);
	}
}
