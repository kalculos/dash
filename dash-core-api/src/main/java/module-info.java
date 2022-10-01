module dash.core.api {
    exports io.ib67.dash.event.handler;
    exports io.ib67.dash.event;
    exports io.ib67.dash.event.bus;

    exports io.ib67.dash.scheduler;
    exports io.ib67.dash.scheduler.exception;
    exports io.ib67.dash.scheduler.strategies;

    requires kiwi.core;
    requires lombok;
    requires org.jetbrains.annotations;
}