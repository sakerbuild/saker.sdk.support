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
package saker.sdk.support.main.user;

import java.util.Map;

import saker.build.file.path.SakerPath;
import saker.build.runtime.execution.ExecutionContext;
import saker.build.task.ParameterizableTask;
import saker.build.task.TaskContext;
import saker.build.task.utils.annot.SakerInput;
import saker.build.task.utils.dependencies.EqualityTaskOutputChangeDetector;
import saker.build.thirdparty.saker.util.ImmutableUtils;
import saker.build.thirdparty.saker.util.ObjectUtils;
import saker.build.trace.BuildTrace;
import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.nest.scriptinfo.reflection.annot.NestParameterInformation;
import saker.nest.scriptinfo.reflection.annot.NestTaskInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeUsage;
import saker.nest.utils.FrontendTaskFactory;
import saker.sdk.support.api.SDKDescription;
import saker.sdk.support.api.UserSDKDescription;
import saker.sdk.support.main.TaskDocs.DocSDKDescription;
import saker.sdk.support.main.TaskDocs.DocSdkIdentifierOption;
import saker.sdk.support.main.path.SDKPathTaskFactory;
import saker.std.api.environment.qualifier.EnvironmentQualifier;
import saker.std.main.environment.qualifier.EnvironmentQualifierTaskOption;

@NestTaskInformation(returnType = @NestTypeUsage(DocSDKDescription.class))
@NestInformation("Creates an SDK description based on the specified parameters.\n"
		+ "The task can be used to create a new SDK description accepted by other tasks as their inputs. The arguments of this "
		+ "task is used to specify how the SDK behaves in the associated context.\n"
		+ "The paths, properties, and environment qualifier can be set to the created SDK description.")
@NestParameterInformation(value = "EnvironmentQualifier",
		type = @NestTypeUsage(EnvironmentQualifierTaskOption.class),
		info = @NestInformation("Specifies the environment qualifier that is associated with the SDK description.\n"
				+ "The environment qualifier is used by the consumer of this SDK desription to determine the suitable "
				+ "build environment it can be used on.\n"
				+ "In general, the environment qualifier can be used to select an executor machine that contains the "
				+ "described SDK. This is particularly useful when build clusters are used.\n"
				+ "If not specified, the SDK description will be associated only with the local execution machine.\n"
				+ "Outputs of std.env.qualifier.*() tasks can be passed to this option."))
@NestParameterInformation(value = "Paths",
		type = @NestTypeUsage(value = Map.class, elementTypes = { DocSdkIdentifierOption.class, SakerPath.class }),
		info = @NestInformation("Specifies the paths for the corresponding identifiers in the created SDK description.\n"
				+ "Each SDK can provide information about paths for the specified identifiers to be used in the consumer "
				+ "task context. The identifiers specified here is often used with the " + SDKPathTaskFactory.TASK_NAME
				+ "() task."))
@NestParameterInformation(value = "Properties",
		type = @NestTypeUsage(value = Map.class, elementTypes = { DocSdkIdentifierOption.class, String.class }),
		info = @NestInformation("Specifies the properties for the corresponding identifiers in the created SDK description.\n"
				+ "Each SDK can provide information about properties for the specified identifiers to be used in the consumer "
				+ "task context."))
public class UserSDKTaskFactory extends FrontendTaskFactory<SDKDescription> {
	private static final long serialVersionUID = 1L;

	public static final String TASK_NAME = "sdk.user";

	@Override
	public ParameterizableTask<? extends SDKDescription> createTask(ExecutionContext executioncontext) {
		return new ParameterizableTask<SDKDescription>() {
			@SakerInput("EnvironmentQualifier")
			public EnvironmentQualifierTaskOption environmentQualifierOption;

			@SakerInput("Paths")
			public Map<String, SakerPath> pathsOption;
			@SakerInput("Properties")
			public Map<String, String> propertiesOption;

			@Override
			public SDKDescription run(TaskContext taskcontext) throws Exception {
				if (saker.build.meta.Versions.VERSION_FULL_COMPOUND >= 8_006) {
					BuildTrace.classifyTask(BuildTrace.CLASSIFICATION_CONFIGURATION);
				}

				if (!ObjectUtils.hasNonNull(propertiesOption, pathsOption, environmentQualifierOption)) {
					//everything is null. useless.
					taskcontext.abortExecution(new IllegalArgumentException("No arguments specified."));
					return null;
				}
				EnvironmentQualifierTaskOption envqualifieropt = ObjectUtils.clone(environmentQualifierOption,
						EnvironmentQualifierTaskOption::clone);
				EnvironmentQualifier[] qualifier = { null };
				if (envqualifieropt != null) {
					envqualifieropt.accept(new EnvironmentQualifierTaskOption.Visitor() {
						@Override
						public void visit(EnvironmentQualifier q) {
							qualifier[0] = q;
						}
					});
				}
				Map<String, SakerPath> paths = ImmutableUtils.makeImmutableNavigableMap(pathsOption);
				Map<String, String> properties = ImmutableUtils.makeImmutableNavigableMap(propertiesOption);
				UserSDKDescription result = UserSDKDescription.create(qualifier[0], paths, properties);
				taskcontext.reportSelfTaskOutputChangeDetector(new EqualityTaskOutputChangeDetector(result));
				return result;
			}
		};
	}

}
