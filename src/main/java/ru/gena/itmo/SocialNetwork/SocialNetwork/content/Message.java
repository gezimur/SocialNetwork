package ru.gena.itmo.SocialNetwork.SocialNetwork.content;

public class Message {
    private int id;
    private int conversation;
    private int sender;
    public String text;

    public Message(int newId, int newConversation, int newSender, String newText){
        id = newId;
        conversation = newConversation;
        sender = newSender;
        text = newText;
    }

    public int getId() {
        return id;
    }

    public int getConversation() {
        return conversation;
    }

    public int getSender() {
        return sender;
    }
}
