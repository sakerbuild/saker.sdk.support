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

import java.util.Objects;

/**
 * Visitor interface for the possible types of {@link SDKDescription}.
 * <p>
 * The visitor is used with {@link SDKDescription#accept(SDKDescriptionVisitor)} where the subject SDK description will
 * call the appropriate <code>visit</code> method of this interface.
 * <p>
 * All of the declared methods in this interface are <code>default</code> and throw an
 * {@link UnsupportedOperationException} by default. Additional <code>visit</code> methods may be added to this
 * interface with similar default implementations.
 * <p>
 * Clients are recommended to implement this interface.
 */
public interface SDKDescriptionVisitor {
	/**
	 * Visits an {@linkplain EnvironmentSDKDescription environment SDK description}.
	 * 
	 * @param description
	 *            The SDK description.
	 */
	public default void visit(EnvironmentSDKDescription description) {
		throw new UnsupportedOperationException("Unsupported SDK description: " + description);
	}

	/**
	 * Visits a {@linkplain ResolvedSDKDescription resolved SDK description}.
	 * 
	 * @deprecated Resolved SDK descriptions are strongly discouraged, clients aren't expected to support these kind of
	 *                 SDK descriptions.
	 * @param description
	 *            The SDK description.
	 */
	@Deprecated
	public default void visit(ResolvedSDKDescription description) {
		throw new UnsupportedOperationException("Unsupported SDK description: " + description);
	}

	/**
	 * Visits a {@linkplain UserSDKDescription user SDK description}.
	 * 
	 * @param description
	 *            The SDK description.
	 */
	public default void visit(UserSDKDescription description) {
		throw new UnsupportedOperationException("Unsupported SDK description: " + description);
	}

	/**
	 * Visits an {@linkplain IndeterminateSDKDescription indeterminate SDK description}.
	 * <p>
	 * The default implementation visits the {@linkplain IndeterminateSDKDescription#getBaseSDKDescription() base SDK
	 * description}.
	 * 
	 * @param description
	 *            The SDK description.
	 */
	public default void visit(IndeterminateSDKDescription description) {
		SDKDescription basesdk = description.getBaseSDKDescription();
		Objects.requireNonNull(basesdk, "base sdk description");
		basesdk.accept(this);
	}
}
