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
        message.getContext().put(SOME_CONTEXT_KEY,someValue);
        var something = message.hasContext(MY_CONTEXT_KEY) ? message.getContext().get(MY_CONTEXT_KEY) : null;
        // or message.getContext().has(MY_CONTEXT_KEY)
    }
```

`ContextKey` 的类型参数（也就是泛型）和你存入的数据类型应该保持一致。dash 并不会对其做运行时类型检查，如果存入了类型不匹配的数据在取出时可能会产生未定义的行为。

# ContextKey

`ContextKey` 和现有的 `IMessageContext` 底层都是基于数组和自增索引实现的，因此从消息中查询/写入索引都非常快。  
然而，每当新的 `ContextKey` 被创建时，这个索引都会被递增，从而可能会让 `IMessageContext` 实现数组初始容量增大，最终造成内存浪费。  

因此，请不要大量创建 `ContextKey` 用在一些没有意义的事情上（比如 `key-12345` 这样的程序自动生成的序列被自动的注册为 `ContextKey` ）。
开发时也并不需要太担心内存浪费问题，因为 `Object[]` 只是一团指针，而正常的，良好设计的程序一般也不会有 32768 个 `ContextKey`。（虽然默认值是32）

此外，现有的 `IMessageContext` 实现会在创建时候按照 `ContextKey.getCurrentIndex()` 的返回值来决定数组大小，如果出现了意外的情况（例如新增了 key ）会进行扩增，
因此，请总是尽可能早的初始化你的所有 ContextKey.（例如作为常量使用）。

