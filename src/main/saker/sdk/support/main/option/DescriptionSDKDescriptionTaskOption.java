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
}
