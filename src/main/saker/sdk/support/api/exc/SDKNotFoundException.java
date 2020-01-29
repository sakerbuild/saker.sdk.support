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
 * An SDK was not found in a given build environment.
 * 
 * @since saker.sdk.support 0.8.2
 */
public class SDKNotFoundException extends SDKManagementException {
	private static final long serialVersionUID = 1L;

	/**
	 * @see SDKManagementException#SDKManagementException()
	 */
	public SDKNotFoundException() {
		super();
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String, Throwable, boolean, boolean)
	 */
	protected SDKNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String, Throwable)
	 */
	public SDKNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(String)
	 */
	public SDKNotFoundException(String message) {
		super(message);
	}

	/**
	 * @see SDKManagementException#SDKManagementException(Throwable)
	 */
	public SDKNotFoundException(Throwable cause) {
		super(cause);
	}

}
