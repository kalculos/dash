package io.ib67.dash;

import net.sf.persism.Session;

public record BotContext(
        Dash dash,
        Session databaseSession
) {
}
