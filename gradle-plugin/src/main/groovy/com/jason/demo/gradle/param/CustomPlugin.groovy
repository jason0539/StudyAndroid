package com.jason.demo.gradle.param

import org.gradle.api.Plugin
import org.gradle.api.Project

public class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //读取build.gradle中的Extension字段
        project.extensions.create('appExt', PluginExtension)
        project.extensions.create('innerExt', PluginNestExtension)

        //注册一个任务，任务中输出上面读取到的字段
        project.task('customTask', type: CustomTask)

        //直接在当前输出读取到的字段,此时还没有赋值
        def appExtParam1 = project['appExt'].param1
        def innerExtParam1 = project['innerExt'].innerParam1
        println("==============================================")
        println("appExtParam1 is " + appExtParam1)
        println("innerExtParam1 is " + innerExtParam1)
        println("==============================================")
    }
}