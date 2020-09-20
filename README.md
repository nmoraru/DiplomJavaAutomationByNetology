# Бронирование тура

Тестирование бронирования тура в Марракэш

## Начало работы

Скопировать через GIT на свой ПК проект командой `git clone https://github.com/nmoraru/aqa_Diplom`

### Prerequisites

На ПК должны быть установлены:

1. Git

2. IntelliJ IDEA

3. OpenJDK 11

4. Docker 

5. Google Chrome

### Установка и запуск

Все команды выполняются в терминале проекта в IntelliJ IDEA

1. Запустить docker-контейнер командой 
`docker-compose up`

2. Подключить БД командой 
`docker-compose exec mysql mysql -u app -p app -v`
Ввести пароль (указан в файле docker-compose.yml)

3. Запустить SUT
Для запуска SUT выполнить команду 
`java -jar ./artifacts/aqa-shop.jar`

4. Запустить симулятор банковских сервисов. Для этого необходимо:
 * перейти в раздел gate-simulator по команде `cd gate-simulator`
 * запустить симулятор сервисов командой `npm start`

5. Запустить тесты командой `gradlew clean test`
 
6. Для повторного использования тестов необходимо перезапустить SUT 

## Лицензия

Copyright [Альфа-Банк] 
Лицензия Альфа-Банка на разработку информационных систем:
https://alfabank.ru/f/3/about/licence_and_certificate/lic.pdf