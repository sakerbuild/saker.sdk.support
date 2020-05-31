package saker.sdk.support.main;

import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.sdk.support.api.SDKDescription;
import saker.sdk.support.api.SDKSupportUtils;
import saker.sdk.support.api.exc.SDKNameConflictException;
import saker.sdk.support.main.option.SDKDescriptionTaskOption;

public class SDKSupportFrontendUtils {
	private SDKSupportFrontendUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param sdksoption
	 *            The SDK options.
	 * @return A modifiable map.
	 * @since saker.sdk.support 0.8.3
	 */
	public static NavigableMap<String, SDKDescription> toSDKDescriptionMap(
			Map<String, SDKDescriptionTaskOption> sdksoption) {
		NavigableMap<String, SDKDescription> sdkdescriptions = new TreeMap<>(SDKSupportUtils.getSDKNameComparator());
		if (ObjectUtils.isNullOrEmpty(sdksoption)) {
			return sdkdescriptions;
		}
		Map<String, SDKDescriptionTaskOption> sdkoptions = new TreeMap<>(SDKSupportUtils.getSDKNameComparator());
		if (!ObjectUtils.isNullOrEmpty(sdksoption)) {
			for (Entry<String, SDKDescriptionTaskOption> entry : sdksoption.entrySet()) {
				SDKDescriptionTaskOption sdktaskopt = entry.getValue();
				if (sdktaskopt == null) {
					continue;
				}
				SDKDescriptionTaskOption prev = sdkoptions.putIfAbsent(entry.getKey(), sdktaskopt.clone());
				if (prev != null) {
					throw new SDKNameConflictException("SDK with name " + entry.getKey() + " defined multiple times.");
				}
			}
		}
		for (Entry<String, SDKDescriptionTaskOption> entry : sdkoptions.entrySet()) {
			SDKDescriptionTaskOption val = entry.getValue();
			SDKDescription[] desc = { null };
			if (val != null) {
				val.accept(new SDKDescriptionTaskOption.Visitor() {
					@Override
					public void visit(SDKDescription description) {
						desc[0] = description;
					}
				});
			}
			sdkdescriptions.putIfAbsent(entry.getKey(), desc[0]);
		}
		return sdkdescriptions;
	}
}
