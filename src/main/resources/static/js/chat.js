(() => {
  const messagesBox = document.getElementById('messages');
  const form = document.getElementById('chatForm');
  const input = document.getElementById('messageInput');

  if (!messagesBox || !form || !input) return;

  const chatId = messagesBox.dataset.chatId;
  const currentLogin = messagesBox.dataset.currentLogin;
  let stompClient = null;

  function appendMessage(m) {
    const div = document.createElement('div');
    div.classList.add('msg');
    div.classList.add(m.senderLogin === currentLogin ? 'me' : 'other');
    div.innerHTML = `
      <div class="meta">
        <span>${m.senderUsername}</span>
        · <span>${new Date(m.createdAt).toLocaleString()}</span>
      </div>
      <div class="body"></div>
    `;
    div.querySelector('.body').innerText = m.content;
    messagesBox.appendChild(div);
    messagesBox.scrollTop = messagesBox.scrollHeight;
  }

  function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
      stompClient.subscribe(`/topic/chats/${chatId}`, (message) => {
        appendMessage(JSON.parse(message.body));
      });
    });
  }

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    const text = input.value.trim();
    if (!text || !stompClient) return;
    stompClient.send(`/app/chats/${chatId}`, {}, JSON.stringify({ content: text }));
    input.value = '';
  });

  connect();
})();
