package shared;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private String messageType;
    private String content;
    private String sender;
    private String recipient;
    private List<FileSearchResult> searchResults;
    private String downloadKey;
    private int port;
    private String recipientAddress;

    public Message(String messageType, String content, String sender, String recipient) {
        this.messageType = messageType;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.port = -1;
        this.recipientAddress = null;
    }

    public Message(String messageType, String content, String sender, String recipient, List<FileSearchResult> searchResults) {
        this.messageType = messageType;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.searchResults = searchResults;
        this.port = -1;
        this.recipientAddress = null;
    }

    
    public Message(String messageType, String content, String sender, String recipient, String downloadKey) {
        this.messageType = messageType;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.downloadKey = downloadKey;
        this.port = -1;
        this.recipientAddress = null;
    }

    public Message(String messageType, String content, String sender, String recipient, int port, String recipientAddress) {
        this.messageType = messageType;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.port = port;
        this.recipientAddress = recipientAddress;
    }

    // Add the getter and setter methods for the downloadKey field
    public String getDownloadKey() {
        return downloadKey;
    }

    public void setDownloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public List<FileSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<FileSearchResult> searchResults) {
        this.searchResults = searchResults;
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}
