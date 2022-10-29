module dash.core.api {
    exports io.ib67.dash.event.handler;
    exports io.ib67.dash.event;
    exports io.ib67.dash.event.bus;

    requires kiwi.core;
    requires lombok;
    requires org.jetbrains.annotations;
}