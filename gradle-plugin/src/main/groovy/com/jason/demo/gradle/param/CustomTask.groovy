package com.jason.demo.gradle.param

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class CustomTask extends DefaultTask {
    @TaskAction
    void output() {
        println "param1 is ${project.appExt.param1}"
        def param2 = project['appExt'].param2
        println "param2 is " + param2
        println "param3 is ${project.appExt.param3}"

        println "innerParam1 is ${project.innerExt.innerParam1}"
        println "innerParam2 is ${project.innerExt.innerParam1}"
        println "innerParam3 is ${project.innerExt.innerParam1}"
    }
}