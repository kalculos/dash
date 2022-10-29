# dash 架构

Dash 是一个跨平台的信息处理框架, 同时提供对于 IM 平台的互操作抽象.

dash 的核心由 `EventChannel` 构成, 几乎所有与信息相关的动作总是在此处进行.

通用的信息抽象格式储存在 `dash-events-api` 模块.