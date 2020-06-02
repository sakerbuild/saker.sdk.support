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
package saker.sdk.support.main.property;

import java.util.ArrayList;
import java.util.List;

import saker.build.runtime.execution.ExecutionContext;
import saker.build.task.ParameterizableTask;
import saker.build.task.TaskContext;
import saker.build.task.exception.MissingRequiredParameterException;
import saker.build.task.utils.annot.SakerInput;
import saker.build.task.utils.dependencies.EqualityTaskOutputChangeDetector;
import saker.build.thirdparty.saker.util.ImmutableUtils;
import saker.build.trace.BuildTrace;
import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.nest.scriptinfo.reflection.annot.NestParameterInformation;
import saker.nest.scriptinfo.reflection.annot.NestTaskInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeUsage;
import saker.nest.utils.FrontendTaskFactory;
import saker.sdk.support.api.SDKPathReference;
import saker.sdk.support.api.SDKPropertyReference;
import saker.sdk.support.api.SDKValueReference;
import saker.sdk.support.impl.FormattedSDKPropertyReference;
import saker.sdk.support.main.TaskDocs.DocFormatString;
import saker.sdk.support.main.TaskDocs.DocSDKPropertyReference;
import saker.sdk.support.main.TaskDocs.DocSdkIdentifierOption;
import saker.sdk.support.main.TaskDocs.DocSdkNameOption;

@NestTaskInformation(returnType = @NestTypeUsage(DocSDKPropertyReference.class))
@NestInformation("Creates a reference to a property in a given SDK.\n"
		+ "An SDK property is a String value derived from the SDKs that are used in a given operation. "
		+ "The task can be used in two ways. By directly referencing an SDK via Name and its property Identifier, "
		+ "or using as a compound value by specifying a Format string and its Arguments.\n"
		+ "A property Identifier that specifies the property that is contained in the SDK.\n"
		+ "SDK property references can be passed to tasks which support it. They are resolved against the SDKs which are "
		+ "also passed to the associated task as input.\n"
		+ "SDK property references are not useful on their own and they should always be used with other SDK inputs. "
		+ "They can be used to specify inputs which are not directly part of the build execution but are installed in "
		+ "the supported build environment.")
@NestParameterInformation(value = "Name",
		aliases = { "" },
		type = @NestTypeUsage(DocSdkNameOption.class),
		info = @NestInformation("Specifies the SDK name that the property should be resolved against.\n"
				+ "The SDK names are compared in an ignore case manner. The actual SDK the property "
				+ "is resolved against is based on the usage context of the returned reference."))
@NestParameterInformation(value = "Identifier",
		type = @NestTypeUsage(DocSdkIdentifierOption.class),
		info = @NestInformation("Specifies the property Identifier in the SDK.\n"
				+ "The specified Identifier will be used to retrieve the property from the associated SDK. The available "
				+ "supported identifiers are based on the associated SDK."))

@NestParameterInformation(value = "Format",
		type = @NestTypeUsage(DocFormatString.class),
		info = @NestInformation("The format of how the property value should be computed.\n"
				+ "The input for the formatter is the value of the Arguments parameter. If it is not specified, "
				+ "then the single argument will be passed to it based on Name and Identifier."))
@NestParameterInformation(value = "Arguments",
		type = @NestTypeUsage(value = List.class,
				elementTypes = SDKPropertyTaskFactory.SDKPropertyFormatArgumentTaskOption.class),
		info = @NestInformation("The input arguments for the Format parameter.\n"
				+ "If this parameter is specified, Format must be as well. Each argument can be other "
				+ "SDK property and path references. They will be evaluated and passed to the formatter "
				+ "as the input."))
public class SDKPropertyTaskFactory extends FrontendTaskFactory<Object> {
	private static final long serialVersionUID = 1L;

	public static final String TASK_NAME = "sdk.property";

	@Override
	public ParameterizableTask<? extends Object> createTask(ExecutionContext executioncontext) {
		return new ParameterizableTask<Object>() {
			@SakerInput(value = { "", "Name" })
			public String sdkNameOption;

			@SakerInput(value = { "Identifier" })
			public String identifierOption;

			@SakerInput(value = { "Format" })
			public String formatOption;

			@SakerInput(value = { "Arguments" })
			public List<SDKPropertyFormatArgumentTaskOption> argumentsOption;

			@Override
			public Object run(TaskContext taskcontext) throws Exception {
				if (saker.build.meta.Versions.VERSION_FULL_COMPOUND >= 8_006) {
					BuildTrace.classifyTask(BuildTrace.CLASSIFICATION_CONFIGURATION);
				}

				if (sdkNameOption != null) {
					if (identifierOption == null) {
						taskcontext.abortExecution(new MissingRequiredParameterException(
								"Missing Identifier parameter.", taskcontext.getTaskId()));
						return null;
					}
					if (argumentsOption != null) {
						taskcontext
								.abortExecution(new IllegalArgumentException("Conflicting parameters with Arguments."));
						return null;
					}
					SDKPropertyReference result = SDKPropertyReference.create(sdkNameOption, identifierOption);
					if (formatOption != null) {
						result = new FormattedSDKPropertyReference(formatOption, ImmutableUtils.singletonList(result));
					}
					taskcontext.reportSelfTaskOutputChangeDetector(new EqualityTaskOutputChangeDetector(result));
					return result;
				}
				if (argumentsOption != null) {
					if (formatOption == null) {
						taskcontext.abortExecution(new MissingRequiredParameterException("Missing Format parameter.",
								taskcontext.getTaskId()));
						return null;
					}
					if (sdkNameOption != null || identifierOption != null) {
						taskcontext.abortExecution(
								new IllegalArgumentException("Conflicting parameters with Name and Identifier."));
						return null;
					}
					List<SDKValueReference<?>> args = new ArrayList<>();
					for (SDKPropertyFormatArgumentTaskOption argopt : argumentsOption) {
						args.add(argopt.getRef());
					}
					SDKPropertyReference result = new FormattedSDKPropertyReference(formatOption,
							ImmutableUtils.makeImmutableList(args));
					taskcontext.reportSelfTaskOutputChangeDetector(new EqualityTaskOutputChangeDetector(result));
					return result;
				}

				taskcontext.abortExecution(new MissingRequiredParameterException(
						"Missing parameters. Either Name and Identifier or Format and Arguments need to be specified.",
						taskcontext.getTaskId()));
				return null;
			}
		};
	}

	@NestTypeInformation(qualifiedName = "SDKPropertyFormatArgument")
	@NestInformation("Input SDK path or property reference for the associated format string.")
	public static class SDKPropertyFormatArgumentTaskOption {
		private SDKValueReference<?> ref;

		public SDKPropertyFormatArgumentTaskOption(SDKValueReference<?> ref) {
			this.ref = ref;
		}

		public SDKValueReference<?> getRef() {
			return ref;
		}

		public static SDKPropertyFormatArgumentTaskOption valueOf(SDKPathReference input) {
			return new SDKPropertyFormatArgumentTaskOption(input);
		}

		public static SDKPropertyFormatArgumentTaskOption valueOf(SDKPropertyReference input) {
			return new SDKPropertyFormatArgumentTaskOption(input);
		}

	}
}
