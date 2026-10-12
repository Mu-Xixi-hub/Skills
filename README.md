# Spring AI ToolCallback + SKILL.md 面试示例

这是一个可学习的最小后端项目，保留 `builder.defaultToolCallbacks(interviewSkillsToolCallback)` 的核心调用链。

**重要区别：** 原项目的 `interviewSkillsToolCallback` 由 `spring-ai-agent-utils` 的 `SkillsTool.builder().addSkillsResource(...).build()` 创建。本示例为了提供独立、可理解、无需额外 agent-utils 依赖的项目，改为使用 Spring AI 原生 `ToolCallback` 接口实现同样的「按需读取 SKILL.md」效果。并非原项目 SkillsTool 的逐字复制。

## 环境
- JDK 21
- Gradle 8.x（需本机已安装 Gradle；未附带 wrapper）
- DeepSeek API Key（或者修改 `application.yml` 为你的 OpenAI 兼容服务）

## 启动
Linux/macOS:
```bash
export AI_API_KEY=你的密钥
gradle bootRun
```
Windows PowerShell:
```powershell
$env:AI_API_KEY="你的密钥"
gradle bootRun
```

## 测试
```bash
curl http://localhost:8080/api/interview/start
```
预期：返回一道 Java 后端面试题。模型工具调用由模型自行决定，若供应商不支持工具调用可能失败。

## 代码阅读顺序
1. `skills/java-backend/SKILL.md`：规则文件
2. `AgentUtilsConfiguration`：创建 `interviewSkillsToolCallback`，`call()` 实际读取文件
3. `ChatClientConfiguration`：通过 `defaultToolCallbacks` 注册工具
4. `InterviewService`：调用 `.prompt().call().content()`
5. `InterviewController`：暴露 HTTP 接口

## 换回原项目 SkillsTool
若你的项目已经正确引入 `spring-ai-agent-utils`，可以将 `AgentUtilsConfiguration` 中的 `@Bean` 方法替换为原项目写法：
```java
@Bean("interviewSkillsToolCallback")
public ToolCallback interviewSkillsToolCallback() {
    Resource skillsRootResource = resourceLoader.getResource("classpath:skills");
    if (!skillsRootResource.exists()) {
        throw new IllegalStateException("未找到 skills 根目录");
    }
    return SkillsTool.builder()
            .addSkillsResource(skillsRootResource)
            .build();
}
```
需要使用原项目中实际的 SkillsTool import 和相匹配的依赖版本。其余 `ChatClientConfiguration`、Service、Controller 不需要改动。

## 验证说明
当前环境未执行联网依赖下载、Gradle 编译或真实 API 调用，不能保证所选依赖版本与你本地环境完全一致。
