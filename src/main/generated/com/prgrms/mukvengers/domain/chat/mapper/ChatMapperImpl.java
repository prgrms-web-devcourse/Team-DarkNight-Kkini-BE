package com.prgrms.mukvengers.domain.chat.mapper;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public Chat toChat(ChatRequest chatRequest, Long crewId) {
        if ( chatRequest == null && crewId == null ) {
            return null;
        }

        Chat.ChatBuilder chat = Chat.builder();

        if ( chatRequest != null ) {
            chat.content( chatRequest.content() );
            chat.userId( chatRequest.userId() );
            chat.type( chatRequest.type() );
        }
        chat.crewId( crewId );

        return chat.build();
    }

    @Override
    public ChatResponse toChatResponse(Chat chat, String username, String profileImgUrl) {
        if ( chat == null && username == null && profileImgUrl == null ) {
            return null;
        }

        ChatResponse.ChatResponseBuilder chatResponse = ChatResponse.builder();

        if ( chat != null ) {
            chatResponse.type( chat.getType() );
            chatResponse.userId( chat.getUserId() );
            chatResponse.createdAt( chat.getCreatedAt() );
            chatResponse.content( chat.getContent() );
            chatResponse.id( chat.getId() );
        }
        chatResponse.username( username );
        chatResponse.profileImgUrl( profileImgUrl );

        return chatResponse.build();
    }
}
