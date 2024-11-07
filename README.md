# Требования для запуска
- JDK 17+
- Maven 3.9.9
- Apache Tomcat

# Запуск
1. Клонируйте репозиторий:
git clone https://github.com/ScaRRy-7/HabitApp.git
2. соберите проект с помощью maven
3. в директории target вы получите war архив habitapp.war
4. перенесите habitapp.war в директорию webapps веб-сервера tomcat
5. перейдите в папку bin веб-сервера tomcat и перезапустите его с помощью команд 1) ./shutdown.sh 2) ./startup.sh
6. для теста приложения можете использовать postman, HabitApp - RESTful приложение, принимающее JSONы и отвечающее JSONами

# Автор
Репозиторий создан и поддерживается:
[ScaRRy-7](https://github.com/ScaRRy-7)
