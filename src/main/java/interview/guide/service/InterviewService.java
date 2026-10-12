package interview.guide.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {
    private final ChatClient interviewChatClient;

    public InterviewService(@Qualifier("interviewChatClient") ChatClient interviewChatClient) {
        this.interviewChatClient = interviewChatClient;
    }

    public String startInterview() {
        return interviewChatClient.prompt()
                .system("开始面试前必须调用 load_interview_skill 工具，skillId 传 java-backend。然后按返回的 SKILL.md 规则只出一道题。")
                .user("我要进行 Java 后端面试")
                .call()
                .content();
    }
}
