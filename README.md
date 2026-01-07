# TextHub — мини‑соцсеть с реальным временем

Полноценное Spring Boot приложение с авторизацией, постами, лайками, комментариями и приватными чатами в реальном времени (STOMP/WebSocket + SockJS). Верстка на Thymeleaf, стили — классический темный UI.

## Стек
- Java 21, Spring Boot (Web, Security, Data JPA, WebSocket, Validation, DevTools)
- PostgreSQL (ddl-auto=update), Hibernate, Lombok
- Thymeleaf + Spring Security extras
- WebSocket/STOMP + SockJS, SimpleBroker `/topic`, prefix `/app`
- Font Awesome (webjars)

## Возможности
- Публичные посты: лента, просмотр поста, создание/редактирование, лайки.
- Профили пользователей и список пользователей.
- Личные чаты: список чатов, комната чата, переименование чата, обмен сообщениями в реальном времени.
- Аутентификация/авторизация через Spring Security (форма логина/регистрации).
- CSP и заголовки безопасности вынесены в отдельный конфиг.

## Технологии
- Backend: Spring Boot (Web, Security, Data JPA, WebSocket, Validation).
- Frontend: Thymeleaf, CSS (Flex/Animate.css), JS (fetch, SockJS/STOMP).
- БД: JPA (H2/PostgreSQL, в зависимости от настроек).
- MapStruct для маппинга сущностей в DTO.
- Тесты: Spring Boot Test, MockMvc.

## Структура
- `src/main/java/com/TextHub/TextHub`
  - `Controller` — HTTP-контроллеры (посты, пользователи, чаты, auth), WebSocket-контроллер чатов.
  - `config` — `WebSocketConfig`.
  - `security` — `SecurityConfig`, `SecurityHeadersConfig`.
  - `Service` — бизнес-логика постов, чатов, сообщений, лайков, комментариев, пользователей.
  - `Repository` — JPA-репозитории.
  - `Entity` — JPA-сущности и DTO.
  - `exceptions` — `ResourceNotFoundException`.
- `src/main/java/com/TextHub/app`
  - `mapper` — MapStruct мапперы (Post, User, Message, Chat).
  - `websocket` — `WsConstants`, `ChatMessagePayload`.
- `src/main/resources/templates` — Thymeleaf страницы (`index`, `posts`, `post-page`, `post-form`, `users`, `profile`, `user-posts`, `login`, `registrations`, `chats`, `chat-room`, `channel`).
- `src/main/resources/static` — `css/styles.css`, `js/layout.js`, `js/chat.js`.
- `src/test/java/com/TextHub/TextHub` — базовые тесты: контекст, `/login` view, наличие `WebSocketConfig`.

## Архитектура
- Слои: Controller → Service → Repository → Entity/DTO.
- Безопасность: Spring Security + Thymeleaf extras; CSRF включен, для WebSocket разрешен `/ws/**`.
- DTO для отдачи в шаблоны (`PostDTO`, `MessageDTO`, `ChatSummaryDTO`) вместо прямых Entity.
- Чаты: `Chat`, `ChatMember`, `Message`; сервисы `ChatService`, `MessageService`.

## Что улучшу дальше
- Добавлю интеграционные тесты для WebSocket чатов.
- Вынесу CSP в конфиг и добавлю nonce для inline-скриптов.
- Подготовлю Docker Compose для app + PostgreSQL.
- Переведу логику уведомлений о доставке/прочтении сообщений.