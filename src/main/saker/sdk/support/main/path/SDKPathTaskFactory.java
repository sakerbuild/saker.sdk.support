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
package saker.sdk.support.main.path;

import saker.build.exception.InvalidPathFormatException;
import saker.build.file.path.SakerPath;
import saker.build.runtime.execution.ExecutionContext;
import saker.build.task.ParameterizableTask;
import saker.build.task.TaskContext;
import saker.build.task.utils.annot.SakerInput;
import saker.build.task.utils.dependencies.EqualityTaskOutputChangeDetector;
import saker.build.trace.BuildTrace;
import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.nest.scriptinfo.reflection.annot.NestParameterInformation;
import saker.nest.scriptinfo.reflection.annot.NestTaskInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeUsage;
import saker.nest.utils.FrontendTaskFactory;
import saker.sdk.support.api.SDKPathReference;
import saker.sdk.support.main.TaskDocs.DocSDKPathReference;
import saker.sdk.support.main.TaskDocs.DocSdkIdentifierOption;
import saker.sdk.support.main.TaskDocs.DocSdkNameOption;

@NestTaskInformation(returnType = @NestTypeUsage(DocSDKPathReference.class))
@NestInformation("Creates a reference to a path in a given SDK.\n"
		+ "An SDK path reference constists of an SDK Name from which it looks up the path. "
		+ "A path Identifier that specifies the path that is contained in the SDK.\n"
		+ "SDK path references can be passed to tasks which support it. They are resolved against the SDKs which are "
		+ "also passed to the associated task as input.\n"
		+ "SDK path references are not useful on their own and they should always be used with other SDK inputs. "
		+ "They can be used to specify inputs which are not directly part of the build execution but are installed in "
		+ "the supported build environment.")
@NestParameterInformation(value = "Name",
		aliases = { "" },
		required = true,
		type = @NestTypeUsage(DocSdkNameOption.class),
		info = @NestInformation("Specifies the SDK name that the path should be resolved against.\n"
				+ "The SDK names are compared in an ignore case manner. The actual SDK the path "
				+ "is resolved against is based on the usage context of the returned reference."))
@NestParameterInformation(value = "Identifier",
		required = true,
		type = @NestTypeUsage(DocSdkIdentifierOption.class),
		info = @NestInformation("Specifies the path Identifier in the SDK.\n"
				+ "The specified Identifier will be used to retrieve the path from the associated SDK. The available "
				+ "supported identifiers are based on the associated SDK."))
@NestParameterInformation(value = "Relative",
		type = @NestTypeUsage(SakerPath.class),
		info = @NestInformation("Specifies a relative path that should be resolved against the retrieved SDK path.\n"
				+ "The specified relative path will be resolved against the path that was directly retrieved "
				+ "from the associated SDK."))
public class SDKPathTaskFactory extends FrontendTaskFactory<Object> {
	private static final long serialVersionUID = 1L;

	public static final String TASK_NAME = "sdk.path";

	@Override
	public ParameterizableTask<? extends Object> createTask(ExecutionContext executioncontext) {
		return new ParameterizableTask<Object>() {
			@SakerInput(value = { "", "Name" }, required = true)
			public String sdkName;

			@SakerInput(value = { "Identifier" }, required = true)
			public String identifier;

			@SakerInput(value = { "Relative" })
			public SakerPath relativePath;

			@Override
			public Object run(TaskContext taskcontext) throws Exception {
				if (saker.build.meta.Versions.VERSION_FULL_COMPOUND >= 8_006) {
					BuildTrace.classifyTask(BuildTrace.CLASSIFICATION_CONFIGURATION);
				}

				if (relativePath != null && !relativePath.isRelative()) {
					taskcontext.abortExecution(
							new InvalidPathFormatException("Relative path argument is not relative: " + relativePath));
					return null;
				}
				SDKPathReference result = SDKPathReference.create(sdkName, identifier, relativePath);
				taskcontext.reportSelfTaskOutputChangeDetector(new EqualityTaskOutputChangeDetector(result));
				return result;
			}
		};
	}

}
