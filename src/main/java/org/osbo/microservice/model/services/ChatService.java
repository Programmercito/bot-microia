package org.osbo.microservice.model.services;

import org.osbo.microservice.model.entities.Chats;
import org.osbo.microservice.model.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    ChatRepository chatRepository;
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
    public void saveChat(Chats chat) {
        chatRepository.save(chat);
    }
    public Chats getChat(Long id) {
        return chatRepository.findById(id).orElse(null);
    }
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }
    public Chats getChatByIdUserAndTipo(Long iduser, String tipo) {
        return chatRepository.findByIduserAndTipo(iduser, tipo);
    }
}
