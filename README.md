# Требования для запуска
- JDK 17+
- Maven 3.9.9
- Docker


# Запуск
1. Клонируйте репозиторий:
git clone https://github.com/ScaRRy-7/HabitApp.git
2. Для запуска приложения используется Spring Boot, имеющий встроенный Apache Tomcat (до этого использовался чистый Tomcat)
3. перейдите в модуль habitapp-core, найдите файл docker-compose и запустите, поднимется докер контейнер с БД PostgreSQL
4. запустите класс App.java из модуля habit-core, отработают тесты, после которых поднимется сервер на порту 7070
5. для теста приложения можете использовать postman, HabitApp - RESTful приложение, принимающее JSONы и отвечающее JSONами
6. приложение использует JWT (Json Web Token)

# Автор
Репозиторий создан и поддерживается:
[ScaRRy-7](https://github.com/ScaRRy-7)
