# TextHub — мини‑соцсеть с реальным временем

Полноценное Spring Boot приложение с авторизацией, постами, лайками, комментариями и приватными чатами в реальном времени (STOMP/WebSocket + SockJS). Верстка на Thymeleaf, стили — классический темный UI.

## Стек
- Java 21, Spring Boot (Web, Security, Data JPA, WebSocket, Validation, DevTools)
- PostgreSQL (ddl-auto=update), Hibernate, Lombok
- Thymeleaf + Spring Security extras
- WebSocket/STOMP + SockJS, SimpleBroker `/topic`, prefix `/app`
- Font Awesome (webjars)

## Быстрый старт
1) Клонировать проект  
`git clone https://github.com/<your-username>/TextHub.git`

2) Настроить БД (PostgreSQL)  
```
spring.datasource.url=jdbc:postgresql://localhost:5432/texthub
spring.datasource.username=postgres
spring.datasource.password=admin
```
При необходимости поменять значения в `src/main/resources/application.properties`.

3) Запуск в dev  
`./mvnw spring-boot:run` (или `mvnw.cmd spring-boot:run` на Windows)

4) Открыть  
`http://localhost:8080`

## Основные возможности
- Регистрация, логин, профиль пользователя.
- Лента постов, создание поста, просмотр поста.
- Лайки и комментарии.
- Поиск пользователей и просмотр чужих постов.
- Чаты 1:1 в реальном времени:
  - список чатов (`/chats`), предпросмотр последнего сообщения;
  - страница комнаты `/chats/{id}` с автопрокруткой, фиксированным инпутом;
  - отправка/прием сообщений через STOMP (`/app/chats/{chatId}` → `/topic/chats/{chatId}`);
  - переименование чата (форма на странице комнаты).

## Как работает WebSocket
- Эндпоинт: `/ws` (SockJS подключение).
- Application destination prefix: `/app`.
- Broker destinations: `/topic/**`.
- На клиенте: подключение к `/ws`, подписка на `/topic/chats/{chatId}`, отправка в `/app/chats/{chatId}`.

## Архитектура
- Слои: Controller → Service → Repository → Entity/DTO.
- Безопасность: Spring Security + Thymeleaf extras; CSRF включен, для WebSocket разрешен `/ws/**`.
- DTO для отдачи в шаблоны (`PostDTO`, `MessageDTO`, `ChatSummaryDTO`) вместо прямых Entity.
- Чаты: `Chat`, `ChatMember`, `Message`; сервисы `ChatService`, `MessageService`.

## Структура (ключевое)
- `src/main/java/com/TextHub/TextHub/Controller` — MVC и WebSocket контроллеры.
- `src/main/java/com/TextHub/TextHub/Service` — бизнес-логика.
- `src/main/java/com/TextHub/TextHub/Repository` — JPA репозитории.
- `src/main/java/com/TextHub/TextHub/Entity` — сущности и DTO.
- `src/main/resources/templates` — Thymeleaf страницы.
- `src/main/resources/static/css` — стили.

## Что улучшу дальше
- Добавлю интеграционные тесты для WebSocket чатов.
- Вынесу CSP в конфиг и добавлю nonce для inline-скриптов.
- Подготовлю Docker Compose для app + PostgreSQL.
- Переведу логику уведомлений о доставке/прочтении сообщений.