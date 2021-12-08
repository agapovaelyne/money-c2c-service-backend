# "Сервис перевода денег"

## Особенности реализации:


- **Сервис реализует методы, описанные в [Спецификации](https://github.com/netology-code/jd-homeworks/blob/master/diploma/MoneyTransferServiceSpecification.yaml)**


- **Приложение разработано с использованием Spring Boot:**

  Сервис предоставляет REST интерфейс для интеграции с [front'ом приложения](frontend/card-transfer-front).


- **Используется сборщик пакетов Maven:**

  См.  [pom.xml](pom.xml).


- **Для запуска используются docker, docker-compose.**

  См.
     - [Dockerfile для front'a](frontend/Dockerfile),
     - [Dockerfile для backend'a](Dockerfile),
     - [Docker-compose скрипт](docker-compose.yml).
     

- **В приложении реализовано логирование посредством [log4j](src/main/resources/log4j.properties):**

  Логируются операции перевода и подтверждения (уровень INFO) и ошибки (уровень ERROR),

  Запись лога производится в файл [money-backend.log](log/money-backend.log).


- **Код покрыт unit-тестами с использованием mockito:**

  Классы тестов реализованы в [src/test/java/com/transfer/MoneyC2C](src/test/java/com/transfer/MoneyC2C).


- **Реализованы интеграционные тесты с использованием testcontainers:**

  Класс интеграционных тестов - [src/test/java/com/transfer/MoneyC2C/container](src/test/java/com/transfer/MoneyC2C/container).

## Запуск:

1. Убедитесь, что на машине установлен Docker,
2. Осуществите сборку проекта, выполнив команду в терминале:
```
mvn install
```
3. Подготовьте (соберите) образы для запуска backend'a и frontend'a, выполнив в терминале последовательно команды:
```
docker build --file=frontend/Dockerfile  -t moneyc2c-frontend .

docker build --file=Dockerfile  -t moneyc2c_money-app .
```
3. После успешной сборки образов запустите docker-compose скрипт, выполнив команду:

```
docker-compose up
```
4. После успешного запуска будут активны и готовы к использованию:
- backend по адресу http://localhost:5500,
- frontend по адресу http://localhost:3000/card-transfer.

## Использование и примеры запросов:
- **С описанием и правилами использования front'а можно ознакомиться [здесь](frontend/card-transfer-front/README.md),**


- **Примеры запросов представлены в директории [requests](src/test/java/com/transfer/MoneyC2C/requests):**

   - Первым выполняется запрос, инициирующий операцию перевода денег с карты на карту - [transfer.http](src/test/java/com/transfer/MoneyC2C/requests/transfer.http),
     В ответ на данный запрос сервер направляет номер (id) операции, 
   - Затем осуществляется подтверждение операции посредством запроса [confirmation.http](src/test/java/com/transfer/MoneyC2C/requests/confirmation.http).
   
     При успешном подтверждении операция выполняется и осуществляется перевод средств в соответствии с инициирующим запросом.
  

- **Обратите внимание:** Тестирование функционала **рекомендуется начать со списания средств с карты-заглушки** (Card №: "1111 2222 3333 4444", CVV: "555", expires: "05/25"),
   
   т.к. текущая реализация предполагает проверку карты, с которой производится списание средств, на ее наличие в базе. Вместо базы с картами в приложении используется заглушка, содержащая только одну карту - заглушку для тестирования с балансом 1000000 RUR (см. данные карты выше, а также в [репозитории](src/main/java/com/transfer/MoneyC2C/repository/TransferRepository.java) или в [transfer.http](src/test/java/com/transfer/MoneyC2C/requests/transfer.http)).

   При этом карта, на которую осуществляется перевод, автоматически заносится в базу данных, поэтому в последующих операциях карты, на которые производились зачисления, можно также указывать как источники списания.
