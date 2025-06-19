#!/bin/bash

# 1. Запуск docker-контейнера
echo "Запускаем docker-контейнер..."
docker-compose up -d

# 2. Ожидание 7 секунд
echo "Ожидаем 7 секунд для инициализации контейнера..."
sleep 7

# 3. Запуск SUT (System Under Test)
echo "Запускаем SUT..."
java -jar ./artifacts/aqa-shop.jar &
SUT_PID=$!
echo "SUT запущен с PID: $SUT_PID"
sleep 7
cd -

# 4. Запуск симулятора банковских сервисов
echo "Запускаем симулятор банковских сервисов..."
ls
cd gate-simulator && npm start &
SIMULATOR_PID=$!
echo "Симулятор запущен с PID: $SIMULATOR_PID"
cd -

# 5. Ожидание 5 секунд для инициализации сервисов
sleep 5

# 6. Запуск тестов
echo "Запускаем тесты..."
if [[ "$1" == "--headless" ]]; then
  ./gradlew clean test -Dselenide.headless=true
else
  ./gradlew clean test
fi
TEST_EXIT_CODE=$?

# 7. Остановка сервисов
echo "Останавливаем SUT и симулятор..."
kill $SUT_PID
kill $SIMULATOR_PID
docker-compose down

# 8. Выход с кодом статуса тестов
exit $TEST_EXIT_CODE