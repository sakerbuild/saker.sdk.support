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

import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.sdk.support.api.SDKDescription;
import saker.sdk.support.api.SDKReference;

@NestInformation("Represents an SDK description that can be passed to tasks which support it.")
public interface SDKDescriptionTaskOption {
	public SDKDescriptionTaskOption clone();

	public void accept(Visitor visitor);

	public static SDKDescriptionTaskOption valueOf(SDKDescription description) {
		return new DescriptionSDKDescriptionTaskOption(description);
	}

	@SuppressWarnings("deprecation")
	public static SDKDescriptionTaskOption valueOf(SDKReference sdkref) {
		return new DescriptionSDKDescriptionTaskOption(saker.sdk.support.api.ResolvedSDKDescription.create(sdkref));
	}

	public interface Visitor {
		public void visit(SDKDescription description);
	}
}
