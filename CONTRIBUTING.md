# How 2 Contribute?

## Code

1. 不引入依赖，不修改版本号，无特殊情况不修改包名类名
2. 公开的 API 需要加入 `@ApiStatus.AvailableSince`，内部但必须暴露的 API 加上 `@Internal`，Javadoc 需要说明注意事项。
3. 在合理情况下，使用项目内的 lombok. 测试用代码放在 `**/local/**` 下。
4. 记得更新文档

此外， 此项目实施来自 SaltedFish Club 的[代码贡献指南](https://github.com/saltedfishclub/documents/blob/main/CONTRIBUTING.md)。shito