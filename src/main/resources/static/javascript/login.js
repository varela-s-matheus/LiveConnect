import { login, loaderOff, loaderOn, cleanSession } from '/javascript/service/chatsService.js';

document.getElementById('loginForm').addEventListener('submit', async function (event) {
  event.preventDefault();
  const button = document.getElementById('loginForm')

  const erroElement = document.getElementById('alert-login');
  erroElement.style.display = 'none';

  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  if (username && password) {
    button.disabled = true;
    loaderOn();
    window.localStorage.setItem('username', username);

    for (let attempt = 1; attempt <= 2; attempt++) {
      const isLoggedIn = await login(username, password);

      if (isLoggedIn) {
        window.location.href = 'pages/chat.html';
        return;
      }
    }
    button.disabled = false;
    cleanSession();
    loaderOff();
    erroElement.style.display = 'flex';
  } else {
    loaderOff();
    alert('Please. Try again later!');
  }
});

window.onload = function () {
  const username = localStorage.getItem('username');
  if (username) {
    document.querySelector('#username').textContent = username;
  }
  loaderOff();
};
cleanSession();


