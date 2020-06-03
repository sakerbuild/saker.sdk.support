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
	 * Converts the input SDK description task options to SDK descriptions.
	 * <p>
	 * <code>null</code> input values are preserved. Exception is thrown in case of conflict.
	 * 
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
		Map<String, SDKDescriptionTaskOption> sdktaskoptions = new TreeMap<>(SDKSupportUtils.getSDKNameComparator());
		for (Entry<String, SDKDescriptionTaskOption> entry : sdksoption.entrySet()) {
			SDKDescriptionTaskOption sdktaskopt = entry.getValue();
			SDKDescriptionTaskOption cloned = sdktaskopt == null ? null : sdktaskopt.clone();
			SDKDescriptionTaskOption prev = sdktaskoptions.putIfAbsent(entry.getKey(), cloned);
			if (prev != null) {
				throw new SDKNameConflictException("SDK with name " + entry.getKey() + " defined multiple times. (With "
						+ prev + " and " + cloned + ")");
			}
		}

		SDKDescription[] desc = { null };
		SDKDescriptionTaskOption.Visitor visitor = new SDKDescriptionTaskOption.Visitor() {
			@Override
			public void visit(SDKDescription description) {
				desc[0] = description;
			}
		};

		for (Entry<String, SDKDescriptionTaskOption> entry : sdktaskoptions.entrySet()) {
			SDKDescriptionTaskOption val = entry.getValue();
			desc[0] = null;
			if (val != null) {
				val.accept(visitor);
			}
			sdkdescriptions.putIfAbsent(entry.getKey(), desc[0]);
		}
		return sdkdescriptions;
	}
}
