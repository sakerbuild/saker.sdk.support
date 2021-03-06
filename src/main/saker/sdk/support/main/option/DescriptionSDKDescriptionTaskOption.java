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
package saker.sdk.support.main.option;

import java.util.Objects;

import saker.sdk.support.api.SDKDescription;

class DescriptionSDKDescriptionTaskOption implements SDKDescriptionTaskOption {
	private SDKDescription sdkDescription;

	public DescriptionSDKDescriptionTaskOption(SDKDescription sdkDescription) {
		Objects.requireNonNull(sdkDescription, "sdk description");
		this.sdkDescription = sdkDescription;
	}

	@Override
	public SDKDescriptionTaskOption clone() {
		return this;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(sdkDescription);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + (sdkDescription != null ? "sdkDescription=" + sdkDescription : "")
				+ "]";
	}

}
