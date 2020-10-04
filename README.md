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

### Установка и запуск сборки на MySQL
#### Выполнить настройки:
1. Состав docker-compose:

version: '3'
services:
  mysql:
    image: mysql:8.0.19
    ports:
      - '3306:3306'
    volumes:
      - ./data:/var/lib/mysql
    environment:
      - MYSQL_RANDOM_ROOT_PASSWORD=yes
      - MYSQL_DATABASE=app
      - MYSQL_USER=app
      - MYSQL_PASSWORD=pass

2. Состав application.properties:

spring.credit-gate.url=http://localhost:9999/credit
spring.payment-gate.url=http://localhost:9999/payment
spring.datasource.url=jdbc:mysql://localhost:3306/app
spring.datasource.username=app
spring.datasource.password=pass

#### Все команды выполняются в терминале проекта в IntelliJ IDEA

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

### Установка и запуск сборки на PostgreSQL
#### Выполнить настройки:
1. Состав docker-compose:

version: '3'
services:
  postgresdb:
    image: postgres:latest
    ports:
      - '5432:5432'
    environment:
      - POSTGRES_DB=app
      - POSTGRES_USER=app
      - POSTGRES_PASSWORD=pass

2. Состав application.properties:

spring.credit-gate.url=http://localhost:9999/credit
spring.payment-gate.url=http://localhost:9999/payment
spring.datasource.url=jdbc:postgresdb://localhost:5432/app
spring.datasource.username=app
spring.datasource.password=pass


#### Все команды выполняются в терминале проекта в IntelliJ IDEA

1. Запустить docker-контейнер командой 
`docker-compose up`

2. Подключить БД командой 
`docker-compose exec postgresdb psql -U app -d app -W`
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