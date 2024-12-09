package dev.kuehni.jeecms.config.security;

import dev.kuehni.jeecms.exception.result.ForbiddenException;
import dev.kuehni.jeecms.exception.result.UnauthorizedException;
import dev.kuehni.jeecms.model.identity.IdentityRole;
import dev.kuehni.jeecms.service.auth.AuthBean;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Interceptor
@Secured
public class SecurityInterceptor {

    @Named
    @Inject
    private AuthBean authBean;

    @Nonnull
    private EnumSet<IdentityRole> getRequiresAnyOfRole(@Nonnull final InvocationContext context) {
        final Method method = Objects.requireNonNull(context.getMethod(), "context.getMethod()");

        final var methodAnnotation = method.getAnnotation(Secured.class);
        final var classAnnotation = method.getDeclaringClass().getAnnotation(Secured.class);

        if (methodAnnotation == null && classAnnotation == null) {
            throw new NoSuchElementException(String.format(
                    "Class %s and Method %s both don't have a Secured annotation.",
                    method.getDeclaringClass().getName(),
                    method.getName()
            ));
        }
        if (methodAnnotation == null) {
            return toSet(classAnnotation.requiresAnyOf());
        }
        if (classAnnotation == null) {
            return toSet(methodAnnotation.requiresAnyOf());
        }

        return intersection(classAnnotation.requiresAnyOf(), methodAnnotation.requiresAnyOf());
    }

    @Nonnull
    private EnumSet<IdentityRole> toSet(@Nonnull final IdentityRole[] roles) {
        return Arrays.stream(roles).collect(Collectors.collectingAndThen(Collectors.toSet(), EnumSet::copyOf));
    }

    @Nonnull
    private EnumSet<IdentityRole> intersection(
            @Nonnull IdentityRole[] requiresAnyOfLeft,
            @Nonnull IdentityRole[] requiresAnyOfRight
    ) {
        final EnumSet<IdentityRole> set = toSet(requiresAnyOfLeft);
        set.retainAll(List.of(requiresAnyOfRight));
        return set;
    }

    private void ensureAuthorizedOrThrowResult(@Nonnull final InvocationContext context) {
        final var method = context.getMethod();
        final var requiresAnyOfRole = getRequiresAnyOfRole(context);

        final var loggedInIdentity = authBean.getLoggedInIdentity();
        if (loggedInIdentity == null) {
            throw new UnauthorizedException(
                    "%s annotation on method %s#%s required authentication.".formatted(
                            Secured.class.getSimpleName(),
                            method.getDeclaringClass().getName(),
                            method.getName()
                    )
            );
        }

        if (requiresAnyOfRole.stream().noneMatch(loggedInIdentity::is)) {
            throw new ForbiddenException(
                    "%s annotation requires the user with id %d to have one of the following roles to invoke the method %s#%s: %s.".formatted(
                            Secured.class.getSimpleName(),
                            loggedInIdentity.getId(),
                            method.getDeclaringClass().getName(),
                            method.getName(),
                            requiresAnyOfRole
                    )
            );
        }
    }

    @AroundInvoke
    public Object intercept(@Nonnull final InvocationContext context) throws Exception {
        ensureAuthorizedOrThrowResult(context);
        return context.proceed();
    }
}
