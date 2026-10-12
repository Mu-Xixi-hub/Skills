package interview.guide.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {
    @Bean("interviewChatClient")
    public ChatClient interviewChatClient(ChatModel chatModel,
            @Qualifier("interviewSkillsToolCallback") ToolCallback interviewSkillsToolCallback) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultToolCallbacks(interviewSkillsToolCallback);
        return builder.build();
    }
}
