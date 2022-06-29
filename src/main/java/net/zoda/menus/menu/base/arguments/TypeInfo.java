package net.zoda.menus.menu.base.arguments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeInfo {

    @Retention(RetentionPolicy.RUNTIME)
    @interface ArgumentInfo {
        ArgumentType type();
        String name();
        boolean required() default true;

        /**
         * Whether to check for argument presence during registration or not
         */
        boolean ignoreCheck() default false;

        boolean inConstructor() default true;
    }

    String name();
    String displayName();
    ArgumentInfo[] arguments();

}
