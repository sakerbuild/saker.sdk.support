package saker.sdk.support.main;

import saker.build.scripting.model.info.TypeInformationKind;
import saker.nest.scriptinfo.reflection.annot.NestFieldInformation;
import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeUsage;

public class TaskDocs {
	@NestTypeInformation(kind = TypeInformationKind.LITERAL, qualifiedName = "SDKName")
	@NestInformation("Name of an SDK description.\n"
			+ "The SDK names are handled in a case-insensitive way and they should be unique in the context in which "
			+ "they are used.")
	public static class DocSdkNameOption {
	}

	@NestTypeInformation(kind = TypeInformationKind.LITERAL, qualifiedName = "SDKIdentifier")
	@NestInformation("Identifier for an SDK path or property.\n"
			+ "SDK identifiers are known identifier that can be used with a given SDK definition. The identifiers "
			+ "are to be used with a specific SDK definition. The identifiers are usually defined by the SDK "
			+ "schema.\n" + "The identifiers are handled case-sensitive way.")
	public static class DocSdkIdentifierOption {
	}

	@NestInformation("Reference to path in a given SDK.\n"
			+ "Represents a path in a SDK that is dynamically resolved based on the SDK context of where "
			+ "the object is applied. Using this only makes sense in a context where there are SDK definitions, "
			+ "against which this path reference can be resolved.")
	@NestFieldInformation(value = "SDKName",
			type = @NestTypeUsage(DocSdkNameOption.class),
			info = @NestInformation("The name of the SDK to which this path reference is applied to."))
	@NestTypeInformation(qualifiedName = "saker.sdk.support.api.SDKPathReference")
	public static class DocSDKPathReference {
	}

	@NestInformation("Reference to property in a given SDK.\n"
			+ "Represents a property in a SDK that is dynamically resolved based on the SDK context of where "
			+ "the object is applied. Using this only makes sense in a context where there are SDK definitions, "
			+ "against which this property reference can be resolved.")
	@NestFieldInformation(value = "SDKName",
			type = @NestTypeUsage(DocSdkNameOption.class),
			info = @NestInformation("The name of the SDK to which this property reference is applied to."))
	@NestTypeInformation(qualifiedName = "saker.sdk.support.api.SDKPropertyReference")
	public static class DocSDKPropertyReference {
	}

	@NestInformation("An SDK description.\n"
			+ "SDK descriptions provide information about an SDK, and how they can be accessed by their consumers.")
	@NestTypeInformation(qualifiedName = "saker.sdk.support.api.SDKDescription")
	public static class DocSDKDescription {
	}
}
