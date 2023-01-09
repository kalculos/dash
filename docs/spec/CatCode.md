# 猫码 / CatCode

和 [CatCode2](https://github.com/ForteScarlet/CatCode2) 类似,
~~但其实是我们[先](https://github.com/saltedfishclub/PolarCore/wiki/CatCode)拿的这个名字哦~~

定义: 一段猫码是多个聊天平台之间通用的符合猫码格式规范的信息的纯文本表示, 并且通过猫码必然 可以或接近完整的
还原出原本的信息.

# 格式

类似 mirai code, dash 中的猫码实际上是 CatCode2 的一个变种.

`[dash:TYPE,property=value,property2=value2,...]`

1. `TYPE` 声明了这段猫码原先的数据类型.
2. `value` **必须**以 URL 编码格式编码.
3. 一般情况下, 所有项均不能为空

## 关于读取数据

许多猫码实际上都是指向外部资源的链接.  
在使用 `dash` 开发时, 尽可能不要直接操作猫码对象. 一般对应的信息类型都会提供打开 `InputStream` 或者类似的方法用于读取.

# 支持情况

目前猫码支持的类型包括:

- 纯文本 `TEXT` as-is | 对应的消息组件为 `io.ib67.dash.message.feature.component.Text`
- 提及 `AT` | 对应的消息组件为 `io.ib67.dash.message.feature.component.At`  
  支持的属性:
  - `target` 目标的通用 userid.
  - `display` AT 在聊天平台上体现的样子, 发送时可空.
  - `platform` 平台 id. 此项仅当 `target` 为非通用 userid 时启用
- 图片 `IMAGE` | 对应的消息组件为 `io.ib67.dash.message.feature.component.Image`  
  支持的属性:
  - `path` 图片文件的路径, 同时这可能是一个 `URL` 或者是来自 dash 的回环地址. ( 比如: `dash://image/111`)
- 文件 `FILE` | 对应的消息组件为 `io.ib67.dash.message.feature.component.File`  
  支持的属性:
  - `path` 文件的路径, 同时这可能是一个 `URL` 或者是来自 dash 的回环地址. ( 比如: `dash://file/nc.avi`)
- 动作 `ACTION` | 对应的消息组件为 `io.ib67.dash.message.feature.component.Action`
  通常是 IM 平台下发的特殊消息. 反序列化时会舍弃此类消息, 因为此类消息可能与发起者强相关.
  支持的属性:
  - `type` 平台的工作, 如将你踢出群聊, 将某人提升为管理员
  - 其他 property 看具体而定.
- 语音 `AUDIO` | 对应的消息组件为 `io.ib67.dash.message.feature.component.Audio`    
  支持的属性:
  - `path` 语音文件的路径, 同时这可能是一个 `URL` 或者是来自 dash 的回环地址. ( 比如: `dash://audio/sexy_nc.mp3` )
- 贴纸 `STICKER` | 对应的消息组件为 `io.ib67.dash.message.feature.component.Sticker`  
  聊天平台上特有的 Emoji 或 "表情包".
  支持的属性:
  - `id` 平台 Emoji 对应的 ID. 通常不会和 `path` 同时出现.
  - `path` 指向表情包图片的路径, 同时这可能是一个 `URL` 或者是来自 dash 的回环地址. ( 比如: `dash://sticker/nc_sb` )
  - `platform` 平台的 dash 名称, 此项是由 Dash Adapter 定义的.
  
# 构造猫码

dash 提供了 [CatCodes](https://github.com/kalculos/dash/blob/main/dash-core-api/src/main/java/io/ib67/dash/util/CatCodes.java) 工具类帮助你迅速构造猫码.

```java
List<CatCodes.CatCode> code = CatCodes.fromString("haha[dash:AT,target=114514]");
// 或者
code.add(CatCodes.ofProps("content","haha").type("TEXT"))
code.add(CatCodes.ofProps(
        "target", "114514"
).type("AT"));

MessageChain.fromCatCode(code); // transforms to MessageChain.
```

但是一般来说不用直接操作 `CatCodes`, 直接操作[消息链](https://github.com/kalculos/dash/blob/main/docs/dev/concept/Event_And_Message.md#%E6%B6%88%E6%81%AF%E9%93%BE)即可.


