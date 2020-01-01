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

/**
 * Represents a description for an SDK.
 * <p>
 * {@link SDKDescription} is a superinterface for all SDK description types that can be represented under this
 * interface.
 * <p>
 * An SDK description specifies how an {@link SDKReference} can be retrieved. The SDK description is basically a
 * descriptor that defines the location or other properties of an SDK. Constructing the {@link SDKReference} is the
 * responsibility of the consumer.
 * <p>
 * When using file locations, you shouldn't downcast them or use <code>instanceof</code>, but use the
 * {@link #accept(SDKDescriptionVisitor)} method with a custom {@link SDKDescriptionVisitor} implementation.
 * <p>
 * Clients shouldn't implement this interface directly.
 * <p>
 * You can create a new {@link SDKDescription} by using the <code>create</code> methods of the actual subinterfaces.
 * 
 * @see EnvironmentSDKDescription
 * @see UserSDKDescription
 * @see IndeterminateSDKDescription
 * @see ResolvedSDKDescription
 */
public interface SDKDescription {
	/**
	 * Accepts a visitor and calls an appropriate <code>visit</code> method on it.
	 * 
	 * @param visitor
	 *            The visitor.
	 * @throws NullPointerException
	 *             If the visitor is <code>null</code>.
	 */
	public void accept(SDKDescriptionVisitor visitor) throws NullPointerException;

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
