package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.ChatSummaryDTO;
import com.TextHub.TextHub.Entity.MessageDTO;
import com.TextHub.TextHub.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public String listChats(Model model) {
        List<ChatSummaryDTO> chats = chatService.getUserChats();
        model.addAttribute("chats", chats);
        return "chats";
    }

    @PostMapping("/start")
    public String startChat(@RequestParam String login, RedirectAttributes ra) {
        try {
            Chat chat = chatService.getOrCreatePrivateChatWith(login);
            return "redirect:/chats/" + chat.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/chats";
        }
    }

    @GetMapping("/{chatId}")
    public String openChat(@PathVariable Long chatId, Model model, RedirectAttributes ra) {
        try {
            Chat chat = chatService.getChatForCurrentUser(chatId);
            List<MessageDTO> messages = chatService.getChatMessages(chatId);
            model.addAttribute("chatId", chatId);
            model.addAttribute("chat", chat);
            model.addAttribute("messages", messages);
            return "chat-room";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/chats";
        }
    }

    @PostMapping("/{chatId}/rename")
    public String renameChat(@PathVariable Long chatId,
                             @RequestParam("title") String title,
                             RedirectAttributes ra) {
        try {
            chatService.renameChat(chatId, title);
            ra.addFlashAttribute("success", "Название чата обновлено");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/chats/" + chatId;
    }
}
