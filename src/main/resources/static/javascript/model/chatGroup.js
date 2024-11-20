class ChatGroup {
    constructor({
        id = null,
        name = "",
        description = "",
        creationTime = new Date().toISOString()
    } = {}) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationTime = creationTime;
    }

    static fromJSON(json) {
        return new ChatGroup({
            id: json.id,
            name: json.name,
            description: json.description,
            creationTime: json.creationTime,
        });
    }

    toJSON() {
        return {
            id: this.id,
            name: this.name,
            description: this.description,
            creationTime: this.creationTime,
        };
    }
}

export default ChatGroup;
