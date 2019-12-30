package saker.sdk.support.main.option;

import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.sdk.support.api.ResolvedSDKDescription;
import saker.sdk.support.api.SDKDescription;
import saker.sdk.support.api.SDKReference;

@NestInformation("Represents an SDK description that can be passed to tasks which support it.")
public interface SDKDescriptionTaskOption {
	public SDKDescriptionTaskOption clone();

	public void accept(Visitor visitor);

	public static SDKDescriptionTaskOption valueOf(SDKDescription description) {
		return new DescriptionSDKDescriptionTaskOption(description);
	}

	public static SDKDescriptionTaskOption valueOf(SDKReference sdkref) {
		return new DescriptionSDKDescriptionTaskOption(ResolvedSDKDescription.create(sdkref));
	}

	public interface Visitor {
		public void visit(SDKDescription description);
	}
}
