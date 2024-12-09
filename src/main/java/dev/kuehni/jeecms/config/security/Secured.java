package dev.kuehni.jeecms.config.security;

import dev.kuehni.jeecms.model.identity.IdentityRole;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.io.Serializable;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

    @Nonbinding
    @Nonnull
    IdentityRole[] requiresAnyOf() default {};
}
