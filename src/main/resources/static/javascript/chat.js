import * as webSocketService from './service/webSocketService.js';
import Message from './model/message.js';
import {
  sucess, findAllChatsByUserId, showAlert, createChatGroup,
  loaderOff, searchChatGroup, createSubscribe, validateTokenSession, findAllMessagesByUserIdAndChatId,
  showMessages, handleNewMessage,
  loaderOn,
  cleanSession
} from './service/chatsService.js';

import ChatGroup from './model/chatGroup.js';

const chatBox = document.getElementById('chat-messages');
export var dataChats = [];
export var dataMessages = [];

if (window.location.pathname.includes('chat.html')) {
  if (!(await validateTokenSession())) {
    window.location.href = '../index.html';
  } else {
    await initializer();
  }
  setTimeout(loaderOff(), 2500);
}


if (document.querySelector('.toggle-theme')) {
  document.querySelector('.toggle-theme').addEventListener('click', toggleTheme);
}

if (document.getElementById('sendButton')) {
  document.getElementById('sendButton').addEventListener('click', function (event) {
    sendMessage(event);
  });
}

if (document.getElementById('messageInput')) {
  document.getElementById('messageInput').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
      sendMessage(event);
    }
  });
}

function sendMessage(event) {
  const now = new Date();
  if (document.getElementById('messageInput').value.trim()) {
    let message = new Message({
      content: document.getElementById('messageInput').value.trim(),
      username: localStorage.getItem('username'),
      senderUserId: localStorage.getItem('liveConnectUserId'),
      chatGroupId: document.querySelector('[data-openedchatid]').dataset.openedchatid,
      timestamp: now.toISOString()
    });

    if (!webSocketService.sendMessage(event, message)) {
      showAlert("Oops... something went wrong. Please reload the page to try again", "Attention");
    }
    document.getElementById('messageInput').value = '';
  }
}

async function initializer() {

  const userId = localStorage.getItem('liveConnectUserId');
  const username = localStorage.getItem('username');

  if (!userId || !username) {
    showAlert('User not authenticated. Redirecting to login...', 'Warning');
    setTimeout(window.location.href = '/index.html', 1500);
    return;
  }

  try {
    const response = await findAllChatsByUserId(userId);
    if (response.length) {
      response.forEach(async chat => {
        webSocketService.connect(chat.id);
        if (!dataChats.map(chat => chat.id).includes(chat.id)) {
          dataChats.push(chat);
        }
      });

      await listContacts(response, 2);
    } else {
      console.log("No chats found.");
    }
    return;
  } catch (error) {
    console.log("Error during initialization: ", error);
    loaderOff();
  }
}


function listContacts(datachats, type) {
  debugger;
  const contactList = document.querySelector("#contact-list");
  contactList.innerHTML = '';

  datachats.sort((a, b) => new Date(b.creationTime) - new Date(a.creationTime));

  datachats.forEach(element => {
    const chat = document.createElement('li');

    chat.className = 'contact';
    chat.setAttribute('data-id', element.id);
    chat.setAttribute('data-offset', 0);
    chat.innerHTML = `
      <div data-nameChatId="${element.id}">${element.name}</div>
      <div class='notification' data-notificationchatid="${element.id}">0</div>
    `;

    contactList.appendChild(chat);

    chat.addEventListener("click", async function () {

      if (chat.getAttribute('data-offset') > 0) return;
      
      loaderOn();
      const notification = document.querySelector(`[data-notificationchatid="${chat.dataset.id}"]`);
      chatBox.innerHTML = '';

      const elementChat = document.createElement('h2');
      elementChat.textContent = document.querySelector(`[data-namechatid="${chat.dataset.id}"]`).textContent;
      elementChat.setAttribute('data-openedchatid', chat.dataset.id);

      document.querySelectorAll(".contact").forEach(contact => {

        if (contact.getAttribute('data-id') != chat.getAttribute('data-id')) {
          contact.classList.remove("active");
          contact.setAttribute('data-offset', 0);
        }
      });

      const messages = await findAllMessagesByUserIdAndChatId(chat.dataset.id, chat.getAttribute('data-offset'));

      showMessages(messages, type);

      chat.classList.add("active");
      chat.setAttribute('data-offset', (messages[0] != undefined && messages[0].offset != undefined ? messages[0].offset : 0));

      document.getElementById("chat").style.display = 'flex';
      document.getElementById('chat-header').replaceChildren(elementChat);

      notification.textContent = 0;
      notification.classList.replace("opened-notification", "notification");
      loaderOff();
      document.getElementById('menu-toggle').checked = false;
    });
  });
}



const addchat = document?.getElementById("add-chat")?.addEventListener("click", function () {
  debugger;
  Swal.fire({
    title: "Create a Group",
    html: `
      <input id="group-name" class="swal2-input" placeholder="Group Name">
      <textarea id="group-description" class="swal2-textarea" placeholder="Group Description"></textarea>
    `,
    showCancelButton: true,
    confirmButtonText: "Submit",
    preConfirm: async () => {
      const groupName = document.getElementById("group-name").value.trim();
      const groupDescription = document.getElementById("group-description").value.trim();

      if (!groupName || !groupDescription) {
        Swal.showValidationMessage("Both fields are required.");
        return false;
      }

      try {
        const newGroup = new ChatGroup({
          name: groupName,
          description: groupDescription
        });

        if (await createChatGroup(newGroup)) {
          listContacts(dataChats);
        }
        return newGroup;
      } catch (error) {
        Swal.showValidationMessage(`Error creating group: ${error.message}`);
        return false;
      }
    }
  }).then((result) => {
    if (result.isConfirmed) {
      const newChatGroup = result.value;
      sucess(`Group "${newChatGroup.name}" has been created successfully!`, "Group Created");
    }
  });
});


document?.getElementById("search-chat")?.addEventListener("click", function () {
  debugger;
  Swal.fire({
    title: "Find a Group",
    html: `
      <input id="group-name" class="swal2-input" placeholder="Group Name">
    `,
    showCancelButton: true,
    confirmButtonText: "Search",
    showLoaderOnConfirm: true,
    preConfirm: async () => {
      const groupName = document.getElementById("group-name").value.trim();

      if (!groupName) {
        Swal.showValidationMessage("Group name is required.");
        return false;
      }

      try {
        const chatGroups = await searchChatGroup(groupName);

        if (chatGroups.length === 0) {
          Swal.showValidationMessage("No chat groups found.");
          return false;
        }

        return chatGroups;
      } catch (error) {
        Swal.showValidationMessage(`Error fetching groups: ${error.message}`);
        return false;
      }
    },
    allowOutsideClick: () => !Swal.isLoading()
  }).then((result) => {
    if (result.isConfirmed) {
      const chatGroups = result.value;

      Swal.fire({
        title: "Available Groups",
        html: chatGroups.map(chat => `
          <div class="search-alert">
            <p>${chat.name}</p>
            <button type="button" class="swal2-confirm swal2-styled subscribe-btn" data-group-id="${chat.id}">
              Subscribe
            </button>
          </div>
        `).join(''),
        showCancelButton: true,
        showConfirmButton: false
      });

      document.querySelectorAll('.subscribe-btn').forEach(button => {
        button.addEventListener('click', async (e) => {
          debugger;
          const groupId = e.target.getAttribute('data-group-id');
          const chatName = e.target.closest('.search-alert').querySelector('p').textContent;

          try {
            await subscribeToChatGroup(groupId);

            listContacts(dataChats);
            sucess(`You have subscribed to "${chatName}"!`, 'Success');
          } catch (error) {
            showAlert(`Failed to subscribe: ${error.message}`, 'Error');
          }
        });
      });
    }
  });
});

document.getElementById("logout")?.addEventListener("click", function () {
  cleanSession();
  localStorage.clear();
  window.location.href = '../index.html';
})

async function subscribeToChatGroup(groupId) {
  await createSubscribe({ id: groupId });
  webSocketService.connect(groupId);
  return true;
}

document.addEventListener('visibilitychange', () => {
  if (document.visibilityState === 'visible') {
    document.title = 'LiveConnect - Messaging App';
  }
});

document.addEventListener('visibilitychange', () => {
  if (document.visibilityState != 'visible') {
    const elements = document.querySelectorAll('[data-notificationchatid]');
    const count = Array.from(elements).filter(el => el.textContent.trim() !== '0').length;
    if (count != 0) {
      handleNewMessage('There are new unread messages!', count);
    }
  }
});

function toggleTheme() {
  document.body.classList.toggle('dark-mode');
}

const darkModeStyles = document.createElement('style');
darkModeStyles.textContent = `
  body.dark-mode {
    background-color: #181818;
    color: #e0e0e0;
  }

  body.dark-mode .app-container {
    background-color: #282828;
  }

  body.dark-mode .message-input {
    border-top: 1px solid #444;
  }

  body.dark-mode .chat-window {
    background-color: #202020;
  }

  body.dark-mode .received {
    background-color: #555;
  }

  body.dark-mode .sent {
    background-color: #1e90ff;
  }

  body.dark-mode #messageField {
    background-color: #333;
    color: #e0e0e0;
  }
  
  body.dark-mode .chat-messages {
    background-color: #1e1f22;
  }

  body.dark-mode .received .message span {
    color: #e0e0dc;
  }
 
  body.dark-mode .message span {
    color: #e0e0dc;
  }
  
  body.dark-mode #messageInput {
    background-color: #a1a1a1;
    border-color: #a1a1a1;
  }  

  body.dark-mode .chat-header, body.dark-mode .chat-footer {
    background-color: #333;
    color: #e0e0e0;
  }

`;

if (window.location.pathname.includes('chat.html')) {
  document.head.appendChild(darkModeStyles);
}