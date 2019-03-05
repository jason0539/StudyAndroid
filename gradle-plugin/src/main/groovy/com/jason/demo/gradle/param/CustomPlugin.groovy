package com.jason.demo.gradle.param

import org.gradle.api.Plugin
import org.gradle.api.Project

public class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //读取build.gradle中的Extension字段
        project.extensions.create('pluginExtension', PluginExtension)
        project.extensions.create('nestExtension', PluginNestExtension)

        //注册一个任务，任务中输出上面读取到的字段
        project.task('customTask', type: CustomTask)

        //直接在当前输出读取到的字段,此时还没有赋值
        def pluginExtensionParam1 = project['pluginExtension'].param1
        def nestExtensionParam1 = project['nestExtension'].nestParam1
        println("==============================================")
        println("pluginExtensionParam1 is " + pluginExtensionParam1)
        println("nestExtensionParam1 is " + nestExtensionParam1)
        println("==============================================")
    }
}