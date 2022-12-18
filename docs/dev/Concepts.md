# 基本概念

在使用 dash 开发之前, 你需要了解一些基本的概念.

# "平台" 与 "通用"

在 dash 中你会经常见到这两个字眼. 正如 [Getting Started](../Getting_Started.md) 所述, dash 是一个跨*平台*的 IM Bot 开发框架,
因此此处的平台指代的是 `聊天软件` (比如 Telegram, QQ).  
而通用是一个修饰语, 他指代的是和平台没有强烈关联, 也就是某种对各个平台都适用的东西.

> 在开发中, 尤其要注意以下两个关键词:
> `platformUid` -- 平台上的用户 id, 比如你的 QQ 号
> `platformId` -- 平台的 `id`.
> 平台的 `id` 是由 *适配器* 决定的, 你可以在文档找到这类 id 的记录和对应的适配器. todo

通常来说, 除非某个东西被特地标注平台相关, 它就是通用的. 比如: `GroupMessage`

# dash-core 和 dash-console

dash 分为若干部分.

`dash-core-api` 提供了开发 dash app 需要的最基础的功能, 例如: 信息处理, 事件系统, 适配器机制等等.  
`dash-core-impl` 是 `dash-core-api` 的具体实现, 如果要 [开发独立应用] 则需要借助 `dash-core-impl`  
`dash-console` 提供了插件控制和管理, 权限控制和用户交互界面.

独立的 dash app 也可以直接作为 `dash-console` 的插件加载, 但是一般不推荐这么做.  
推荐的做法是: 针对 `dash-console` 编写插件. 有如下好处:

1. 可以接入 dash 的生态
2. 分发时, 可以使用 dash-console 的特殊打包工具.
3. 不需要自己手动初始化 `dash-core`, 不需要额外配置.
4. .....

配置环境:

- [基于 dash-console 开发](./Project_Setup_Console.md)
- [基于 dash-core 开发](./Project_Setup_Core.md)

---

dash 的架构相当简单, 只需要稍微学习就可以上手.  
接下来, 是一些开发中常见的概念.

- [消息, 事件与用户交互](./concept/Event_And_Message.md) -- 此章包含 dash 中最重要的两个概念, 以及开发中如何实际运用的技巧.
- [命令框架 (Console)](./concept/Using_Commands.md) -- dash-console 中命令框架的基本使用和技巧.
- [服务与依赖注入](./concept/Service_And_DI.md) -- 依赖注入机制的使用.
- [用户, 联系人, 组与权限](./concept/Permissions.md) -- 如何区分用户以及鉴权.
- [与适配器交互](./concept/With_Adapter.md) -- 处理平台强相关信息的做法, 以及一些适配器细节.

除此之外:

- [会话系统](./concept/advanced/Session.md) -- 强表达力的会话 API 的使用, 可以用于构造交互式的 bot
- [聊天组件与自动降级](./concept/advanced/Components.md) -- 高层组件的使用以及他们的自动降级机制.
- [回环地址与挂载](./concept/advanced/Ringed_And_Mount.md) -- dash 回环的使用
- [编写一个适配器](./concept/advanced/Writing_An_Adapter.md) -- 编写一个 dash 适配器的具体方法, 注意事项以及最佳实践.

以及:

- [分布式 dash 应用](./concept/advanced/Distributed_Dash_App.md) -- 通过 `Wsify` 连接各个 dash 实例的教程.
- [对应用进行提前编译](./concept/advanced/AOT.md) -- 使用 `native-image` 对 dash 程序打包.
- [其他语言接入 (Console)](./concept/Multi_Language.md) -- 使用其他语言开发 Console 插件
- [Best Practices](./concept/Best_Practices.md) -- 最佳实现