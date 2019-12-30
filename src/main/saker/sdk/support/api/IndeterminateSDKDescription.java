package saker.sdk.support.api;

import java.io.Externalizable;

/**
 * {@link SDKDescription} that may possibly select different SDKs on different environments.
 * <p>
 * The {@link IndeterminateSDKDescription} interface defines an SDK description that may locate different
 * {@linkplain SDKReference SDK references} based on the environment that it is being resolved on. The interface also
 * defines the {@link #pinSDKDescription(SDKReference)} method that is able to create an {@link SDKDescription} that is
 * no longer indeterminate based on a resolved {@link SDKReference}.
 * <p>
 * An example intederminate SDK description is one that selects the latest installed version of an SDK. The use-case for
 * an indeterminate SDK description is to be able to select the same SDKs on build clusters if they have different
 * installations. E.g.
 * <ol>
 * <li>Determine the SDK using {@link #getBaseSDKDescription()}.</li>
 * <li>Pin the SDK description with the resolved {@link SDKReference} using
 * {@link #pinSDKDescription(SDKReference)}.</li>
 * <li>Use the pinned {@link SDKDescription} when distributing the task to build clusters.</li>
 * </ol>
 * <p>
 * This SDK description plays an important role to ensure that the same SDKs are used for a given operation when the SDK
 * installations on build cluster machines are heterogeneous.
 * <p>
 * Clients are recommended implement this interface. When doing so, make sure to adhere to the {@link #hashCode()} and
 * {@link #equals(Object)} contract. Implementers are also recommended to implement {@link Externalizable}.
 */
public interface IndeterminateSDKDescription extends SDKDescription {
	/**
	 * Gets the base {@link SDKDescription} that may possibly locate different {@linkplain SDKReference SDK references}.
	 * <p>
	 * The returned SDK description will be used to resolve the {@link SDKReference} that will be passed to
	 * {@link #pinSDKDescription(SDKReference)}.
	 * 
	 * @return The base SDK description.
	 */
	public SDKDescription getBaseSDKDescription();

	/**
	 * Pins the resolved SDK reference to an SDK description that may no longer locate different SDKs.
	 * <p>
	 * The argument will always be the result of the SDK resolution of {@link #getBaseSDKDescription()}.
	 * 
	 * @param sdkreference
	 *            The previously resolved SDK reference.
	 * @return The pinned SDK description.
	 */
	public SDKDescription pinSDKDescription(SDKReference sdkreference);

	@Override
	public default void accept(SDKDescriptionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
