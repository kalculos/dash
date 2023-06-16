module dash.core.api {
    uses io.ib67.dash.Dash;
    exports io.ib67.dash;
    exports io.ib67.dash.contact;
    exports io.ib67.dash.contact.group;
    exports io.ib67.dash.contact.group.channel;
    exports io.ib67.dash.exception;
    exports io.ib67.dash.message;
    exports io.ib67.dash.message.group;
    exports io.ib67.dash.message.feature;
    exports io.ib67.dash.message.feature.component;
    exports io.ib67.dash.adapter;
    exports io.ib67.dash.util;
    exports io.ib67.dash.exception.user;

    exports io.ib67.dash.event.handler;
    exports io.ib67.dash.event;
    exports io.ib67.dash.event.bus;

    exports io.ib67.dash.scheduler;
    exports io.ib67.dash.scheduler.future;
    exports io.ib67.dash.user;
    exports io.ib67.dash.user.permission;
    exports io.ib67.dash.context;

    requires kiwi.core;
    requires lombok;
    requires org.jetbrains.annotations;
    requires com.github.spotbugs.annotations;
}