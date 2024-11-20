import { dataChats } from '../chat.js';
import ChatGroup from '../model/chatGroup.js';
import * as webSocketService from './webSocketService.js';


const chatBox = document?.getElementById("chat-messages")
let newMessageCount = 0;

export async function login(username, password) {
    try {
        const response = await fetch(`/user/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'password': password
            })
        });

        if (!response.ok) {
            return false;
        }

        const data = await response.json();
        window.localStorage.setItem('liveConnectToken', data.token);
        window.localStorage.setItem('liveConnectUserId', data.id);
        return true;
    } catch (error) {
        return false;
    }
}

export async function validateTokenSession() {
    debugger;
    const token = localStorage.getItem('liveConnectToken');
    const username = localStorage.getItem('username');

    if (token != '') {
        try {
            const response = await fetch(`/user/token`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    'username': username
                })
            });

            if (!response.ok) {
                return false;
            }

            const data = await response.json();
            return data;
        } catch (error) {
            return false;
        }
    }
    return false;
}

export async function createChatGroup(chatGroup) {
    const userId = localStorage.getItem('liveConnectUserId');
    const token = localStorage.getItem('liveConnectToken');

    try {
        const response = await fetch(`/chat/createChat/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(chatGroup)
        });

        if (!response.ok) {
            return false;
        }
        const chat = await response.json();
        if (!dataChats.map(chat => chat.id).includes(chat.id)) {
            dataChats.push(chat);
        }

        webSocketService.connect(chat.id);
        return true;
    } catch (error) {
        return false;
    }
}

export async function findAllChatsByUserId(userId) {
    const token = localStorage.getItem('liveConnectToken');

    try {
        const response = await fetch(`/chat/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            return [];
        }
        const data = await response.json();
        return data;
    } catch (error) {
        return [];
    }
}


export async function findAllMessagesByUserIdAndChatId(chatGroupId, offset) {
    const token = localStorage.getItem('liveConnectToken');
    const userId = localStorage.getItem('liveConnectUserId');

    try {
        const response = await fetch(`/message/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                chatGroupId: chatGroupId,
                offset: offset
            })
        });

        if (!response.ok) {
            return [];
        }
        const data = await response.json();
        return data;
    } catch (error) {
        return [];
    }
}


export async function searchChatGroup(chatGroupName) {
    debugger;
    const token = localStorage.getItem('liveConnectToken');
    const userId = localStorage.getItem('liveConnectUserId');

    try {
        const response = await fetch(`/chat/search/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                name: chatGroupName
            })
        });

        if (!response.ok) {
            return false;
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return false;
    }
}


export function showAlert(content, title) {
    Swal.fire({
        title: title,
        text: content,
        icon: 'error',
    });
}

export function sucess(content, title) {
    Swal.fire({
        title: title,
        text: content,
        icon: 'success'
    });
}

export function loaderOff() {
    const waitDiv = document.getElementById('wait');
    waitDiv ? waitDiv.style.display = 'none' : '';
};

export function loaderOn() {
    const waitDiv = document.getElementById('wait');
    waitDiv ? waitDiv.style.display = 'flex' : '';
};

export async function createSubscribe(dataGroup) {
    const token = localStorage.getItem('liveConnectToken');
    const userId = localStorage.getItem('liveConnectUserId');

    try {
        const response = await fetch(`/chat/subscribe/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(new ChatGroup(dataGroup))
        });

        if (!response.ok) {
            return false;
        }
        const chat = await response.json();
        if (!dataChats.map(chat => chat.id).includes(chat.id)) {
            dataChats.push(chat);
        }

        return chat;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return false;
    }
}

export function showMessages(messages, type) {
    const userId = localStorage.getItem('liveConnectUserId');

    const formattedDate = new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        timeZone: 'UTC'
    });
    const moreMessage = document.createElement('div');
    moreMessage.style = 'display: flex; justify-content: center;';
    moreMessage.id = '';
    moreMessage.innerHTML = `
        <button type=button id="show-more">Show more</button>
    `;
    moreMessage.addEventListener("click", async function () {

        const openedChatId = document.querySelector(`[data-openedchatid]`);
        const offset = document.querySelector(`[data-id="${openedChatId.dataset.openedchatid}"]`);

        const messages = await findAllMessagesByUserIdAndChatId(openedChatId.dataset.openedchatid, offset.dataset.offset);
        if (messages.length > 0) {
            offset.setAttribute("data-offset", messages.offset);
            showMessages(messages.reverse(), 1);
        } else {
            document.getElementById("show-more").remove();
        }

    });

    if (messages.length > 0 && type != 0 && type != 1) {
        appendMessage(moreMessage, type);
    }
    messages.forEach(message => {
        const messageArea = document.createElement('div');
        messageArea.className = 'messages ' + (message.senderUserId != userId ? '' : 'content-end');
        messageArea.innerHTML = `
        <div class="${(message.senderUserId != userId ? 'received' : 'sent')}">
            ${(message.senderUserId != userId ? '<div class="username-message">' + message.username + '</div>' : '')}
            <div class='message'>
                <p>${message.content}</p>
                <span>${formattedDate.format(new Date(message.timestamp))}</span>
            <div>
        </div>
        `
        appendMessage(messageArea, type);
    });
    if (messages.length > 0 && type == 1) {
        document.getElementById("show-more").remove();
        appendMessage(moreMessage, type);
    }
    return;
}

export function appendMessage(messageElement, type) {
    const shouldScroll = Math.abs(chatBox.scrollHeight - chatBox.scrollTop - chatBox.clientHeight) <= 1;

    switch (type) {
        case 1:
            chatBox.insertBefore(messageElement, chatBox.firstChild);
            break;
        case 0:
        default:
            chatBox.appendChild(messageElement);
            break;
    }

    if (shouldScroll) {
        setTimeout(() => {
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 0);
    }
}

export function handleNewMessage(messageContent, MessageCount) {
    if (document.visibilityState === 'hidden') {

        document.title = `Chat(${MessageCount})`;

        if (Notification.permission === 'granted') {
            new Notification('Nova mensagem', {
                body: messageContent,
                icon: 'path/to/icon.png'
            });
        }
    }
}

if (Notification.permission !== 'granted' && Notification.permission !== 'denied') {
    Notification.requestPermission();
}


export function cleanSession() {
    document.cookie.split(";").forEach(function (c) {
        document.cookie = c.trim().split("=")[0] + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    });

    window.myAppData = {};

    history.replaceState(null, document.title, window.location.href);

    if (window.fetchAbortController) {
        window.fetchAbortController.abort();
    }
}