package com.jason.demo.gradle.param

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class CustomTask extends DefaultTask {
    @TaskAction
    void output() {
        println "param1 is ${project.pluginExtension.param1}"
        def param2 = project['pluginExtension'].param2
        println "param2 is " + param2
        println "param3 is ${project.pluginExtension.param3}"

        println "nestparam1 is ${project.nestExtension.nestParam1}"
        println "nestparam2 is ${project.nestExtension.nestParam2}"
        println "nestparam3 is ${project.nestExtension.nestParam3}"
    }
}