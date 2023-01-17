package dash.internal;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {
    public static final boolean ALLOW_ASYNC_UNSUBSCRIBE = Boolean.getBoolean("dash.asyncHandlers.mutatePipeline");
}
