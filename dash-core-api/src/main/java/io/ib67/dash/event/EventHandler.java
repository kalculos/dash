package io.ib67.dash.event;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

@ApiStatus.AvailableSince("0.1.0")
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface EventHandler {
    int priority() default EventPriorities.NORMAL;

    ScheduleType scheduleType() default ScheduleType.MAIN;

    String name() default "anonymous listener";

    boolean ignoreCancelled() default true;
}
