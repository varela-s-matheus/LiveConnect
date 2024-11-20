class Message {
    constructor({
        id = null,
        content = "",
        username = "",
        senderUserId = null,
        chatGroupId = null,
        timestamp = new Date().toISOString()
    } = {}) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.senderUserId = senderUserId;
        this.chatGroupId = chatGroupId;
        this.timestamp = timestamp;
    }

    static fromJSON(json) {
        return new Message({
            id: json.id,
            content: json.content,
            username: json.username,
            senderUserId: json.senderUserId,
            chatGroupId: json.chatGroupId,
            timestamp: json.timestamp,
        });
    }

    toJSON() {
        return {
            id: this.id,
            content: this.content,
            username: this.username,
            senderUserId: this.senderUserId,
            chatGroupId: this.chatGroupId,
            timestamp: this.timestamp,
        };
    }
}

export default Message;
