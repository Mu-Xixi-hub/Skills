package interview.guide.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;

@Configuration
public class AgentUtilsConfiguration {
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public AgentUtilsConfiguration(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Bean("interviewSkillsToolCallback")
    public ToolCallback interviewSkillsToolCallback() {
        return new ToolCallback() {
            @Override
            public ToolDefinition getToolDefinition() {
                return ToolDefinition.builder()
                        .name("load_interview_skill")
                        .description("开始技术面试前读取指定方向的 SKILL.md 规则；Java 后端方向传入 java-backend")
                        .inputSchema("""
                                {"type":"object","properties":{"skillId":{"type":"string","description":"Skill 标识，如 java-backend"}},"required":["skillId"]}
                                """)
                        .build();
            }

            @Override
            public String call(String toolInput) {
                try {
                    JsonNode input = objectMapper.readTree(toolInput);
                    String skillId = input.path("skillId").asText("");
                    if (!"java-backend".equals(skillId)) {
                        return "不支持的 Skill: " + skillId;
                    }
                    Resource skill = resourceLoader.getResource("classpath:skills/java-backend/SKILL.md");
                    if (!skill.exists()) {
                        return "Skill 文件不存在";
                    }
                    return skill.getContentAsString(StandardCharsets.UTF_8);
                } catch (Exception exception) {
                    throw new IllegalStateException("加载 Skill 失败", exception);
                }
            }
        };
    }
}
