# 消息上下文

一个完整的 dash 程序由多个事件的处理器组合而成，这些组件之间经常需要共享一些信息，例如用户的 `角色`(管理员？用户？) 或者是来源平台是否支持某些特性。  

对此， dash 提供了 `IMessageContext` 将处理器之间解耦合。每个处理器都可以在那里放入这条消息的附加数据，以此进一步拓展信息对象本身携带的元信息。  

# IMessageContext

要使用上下文系统，首先要得到一个 `ContextKey`。

```java
var contextKey = ContextKey.<T>of("the_identifier_of_your_key");
```
 
静态工厂 `of` 接受一个 `ContextKey` 的标识符，并且尝试将其持续缓存起来。  
标识符是大小写不区分的。虽然你可以在任何地方重新拿出缓存中的 `ContextKey`，但你总是应该把得到的 `contextKey` 对象储存起来，例如作为一个静态常量。  

接着，你可以访问消息中的内容。
```java
    @Subscribe
    public void onMessage(GroupChannelMessage message){
        message.getContext().put(SOME_CONTEXT_KEY,someValue); // 存放
        var something = message.hasContext(MY_CONTEXT_KEY) ? message.getContext().get(MY_CONTEXT_KEY) : null; // 获取
        // or message.getContext().has(MY_CONTEXT_KEY)
    }
```

`ContextKey` 的类型参数（也就是泛型）和你存入的数据类型应该保持一致。dash 并不会对其做运行时类型检查，如果存入了类型不匹配的数据在取出时可能会产生未定义的行为。

然后，在此处放入 context 的对象就能被下游的处理器得到了，你可以在库中共享你的 `ContextKey` 以此让他们接入得到你的附加信息。
