import { dataChats, dataMessages } from '../chat.js';
import { showMessages, handleNewMessage, cleanSession } from './chatsService.js';

'use strict';
let stompClient;

const SockJS = window.SockJS;
const Stomp = window.Stomp;
const token = localStorage.getItem('liveConnectToken');
const username = localStorage.getItem('username');
const userId = localStorage.getItem('liveConnectUserId');

export function connect(connectionId) {
    debugger;

    if (connectionId) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({ 'Authorization': 'Bearer ' + token }, () => {
            onConnected(connectionId);
        }, onError);
    }
    return;
}



export function onConnected(connectionId) {

    setTimeout(function () {
        stompClient.subscribe(`/topic/public/${connectionId}`, onMessageReceived, {
            'Authorization': 'Bearer ' + token
        })
    }, 1000);

}


export function onError(error) {
    console.error("ERRO: " + error);
    stompClient.disconnect();
    cleanSession();
    return;
}


export function sendMessage(event, message) {
    debugger;
    if (message && stompClient) {
        if (!stompClient.connected) {
            console.error("WebSocket not connected");
            event.preventDefault();
            return false;
        }
        if (message.senderUserId === userId && dataChats.map(chat => String(chat.id)).includes(message.chatGroupId)) {
            stompClient.send(`/app/chat.sendMessage/${message.chatGroupId}`,
                { Authorization: `Bearer ${token}` },
                JSON.stringify(message)
            );
            return true;
        }
    }
    event.preventDefault();
    return false;
}



export function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    if (message.chatGroupId != null && message.chatGroupId != 0) {
        const openedChat = document.querySelector('[data-openedchatid]');

        if (openedChat.dataset.openedchatid == message.chatGroupId) {
            showMessages([message], 0);
        } else {
            const notificationChatId = document.querySelector(`[data-notificationchatid="${message.chatGroupId}"]`);
            notificationChatId.textContent++;
            notificationChatId.classList.replace("notification", "opened-notification");
            updatePageTitleOnNewMessages()
        }

    }
}

function updatePageTitleOnNewMessages() {
    if (document.visibilityState != 'visible') {
        const elements = document.querySelectorAll('[data-notificationchatid]');
        const count = Array.from(elements).filter(el => el.textContent.trim() !== '0').length;
        if (count != 0) {
            handleNewMessage('There are new unread messages!', count);
        }
    }

}