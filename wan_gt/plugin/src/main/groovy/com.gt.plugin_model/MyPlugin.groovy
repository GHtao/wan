package com.gt.plugin_model

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        System.out.println("全局的plugin 可以上传到maven")
    }
}