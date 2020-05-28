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
package saker.sdk.support.api.exc;

/**
 * Failed to derive some information based on the input SDKs. 
 * 
 * @see SDKPathNotFoundException
 * @see SDKPropertyNotFoundException
 * @since saker.sdk.support 0.8.3
 */
public class SDKValueNotFoundException extends SDKManagementException {
	private static final long serialVersionUID = 1L;

	/**
	 * @see SDKManagementException#SDKManagementException()
	 */
	public SDKValueNotFoundException() {
		super();
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String, Throwable, boolean, boolean)
	 */
	protected SDKValueNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String, Throwable)
	 */
	public SDKValueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String)
	 */
	public SDKValueNotFoundException(String message) {
		super(message);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(Throwable)
	 */
	public SDKValueNotFoundException(Throwable cause) {
		super(cause);
	}
}
