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
package saker.sdk.support.main.resolve;

import java.util.Map;
import java.util.NavigableMap;

import saker.build.runtime.execution.ExecutionContext;
import saker.build.runtime.execution.ExecutionProperty;
import saker.build.task.EnvironmentSelectionResult;
import saker.build.task.ParameterizableTask;
import saker.build.task.TaskContext;
import saker.build.task.TaskExecutionEnvironmentSelector;
import saker.build.task.utils.annot.SakerInput;
import saker.build.task.utils.dependencies.EqualityTaskOutputChangeDetector;
import saker.build.trace.BuildTrace;
import saker.nest.scriptinfo.reflection.annot.NestInformation;
import saker.nest.scriptinfo.reflection.annot.NestParameterInformation;
import saker.nest.scriptinfo.reflection.annot.NestTaskInformation;
import saker.nest.scriptinfo.reflection.annot.NestTypeUsage;
import saker.nest.utils.FrontendTaskFactory;
import saker.sdk.support.api.SDKDescription;
import saker.sdk.support.api.SDKReference;
import saker.sdk.support.api.SDKSupportUtils;
import saker.sdk.support.api.exc.SDKManagementException;
import saker.sdk.support.main.SDKSupportFrontendUtils;
import saker.sdk.support.main.TaskDocs.DocSDKDescription;
import saker.sdk.support.main.TaskDocs.DocSdkNameOption;
import saker.sdk.support.main.option.SDKDescriptionTaskOption;
import saker.std.api.util.SakerStandardUtils;

@NestTaskInformation(returnType = @NestTypeUsage(value = Map.class,
		elementTypes = { DocSdkNameOption.class, DocSDKDescription.class }))
@NestInformation("Resolves the specified SDKs.\n"
		+ "The task takes the SDK name and description pairs as input and performs SDK resolution based on them.\n"
		+ "The returned map contains the concrete SDKs that are the result of the operation. Any indeterminate SDKs will be "
		+ "pinned to a specific version or configuration.\n"
		+ "The task will take build clusters into account (if any) during resolution.")
@NestParameterInformation(value = "SDKs",
		aliases = { "" },
		required = true,
		type = @NestTypeUsage(value = Map.class,
				elementTypes = { DocSdkNameOption.class, SDKDescriptionTaskOption.class }),
		info = @NestInformation("The map of SDKs to resolve.\n"
				+ "The keys are the name of a given SDK and they are treated in a case-insensitive manner. The values "
				+ "are the SDK configurations that should be resolved."))
public class ResolveSDKTaskFactory extends FrontendTaskFactory<Object> {
	private static final long serialVersionUID = 1L;

	public static final String TASK_NAME = "sdk.resolve";

	@Override
	public ParameterizableTask<? extends Object> createTask(ExecutionContext executioncontext) {
		return new ParameterizableTask<Object>() {

			@SakerInput(value = { "", "SDKs" }, required = true)
			public Map<String, SDKDescriptionTaskOption> sdksOption;

			@Override
			public Object run(TaskContext taskcontext) throws Exception {
				if (saker.build.meta.Versions.VERSION_FULL_COMPOUND >= 8_006) {
					BuildTrace.classifyTask(BuildTrace.CLASSIFICATION_CONFIGURATION);
				}

				NavigableMap<String, SDKDescription> sdks = SDKSupportFrontendUtils.toSDKDescriptionMap(sdksOption);
				TaskExecutionEnvironmentSelector envselector = SDKSupportUtils
						.getSDKBasedClusterExecutionEnvironmentSelector(sdks.values());
				NavigableMap<String, SDKDescription> pinned;
				if (envselector != null) {
					try {
						ExecutionProperty<? extends EnvironmentSelectionResult> execprop = SakerStandardUtils
								.createEnvironmentSelectionTestExecutionProperty(envselector);
						EnvironmentSelectionResult selectionresult = taskcontext.getTaskUtilities()
								.getReportExecutionDependency(execprop);
						pinned = SDKSupportUtils.pinSDKSelection(selectionresult, sdks);
					} catch (Exception e) {
						throw new SDKManagementException("SDK resolution failed.", e);
					}
				} else {
					try {
						NavigableMap<String, SDKReference> resolved = SDKSupportUtils.resolveSDKReferences(taskcontext,
								sdks);
						pinned = SDKSupportUtils.pinSDKSelection(sdks, resolved);
					} catch (Exception e) {
						throw new SDKManagementException("SDK resolution failed.", e);
					}
				}

				taskcontext.reportSelfTaskOutputChangeDetector(new EqualityTaskOutputChangeDetector(pinned));
				return pinned;
			}
		};
	}

}
