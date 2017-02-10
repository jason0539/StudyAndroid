package com.jason.demo.gradle.param

import org.gradle.api.Plugin
import org.gradle.api.Project

public class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('pluginExt', PluginExtension)
        project.pluginExt.extensions.create('nestExt', PluginNestExtension)
        project.task('customTask', type: CustomTask)
    }
}