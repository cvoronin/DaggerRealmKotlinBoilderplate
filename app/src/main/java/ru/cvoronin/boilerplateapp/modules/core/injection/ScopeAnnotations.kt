package ru.cvoronin.boilerplateapp.modules.core.injection

import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AuthUserScope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope