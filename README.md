#Железо
1. JDK 17
2. Gradle (DSL kotlin)
3. Spring boot 3
4. PostgreSQL 16 + Cassandra db

#Установка
1. После клона проекта нужно запустить образы командой
   docker-compose up
2. После успешного запуска cassandra db нужно создать новый keyspace 
CREATE KEYSPACE cass WITH replication = {
'class': 'SimpleStrategy',
'replication_factor': 1};

#Функционал проекта
1. В application.properties настроить порты сервера и свой бд в postgresql и cassandra если у вас другие данные
2. URL сваггера http://localhost:8080/swagger-ui/index.html#/ 
в сваггере весь функционал продокументирован
3. Некоторые валюты в API не рабочие например KZT/USD, GBP и тд в респонсе будет информация насчет валют.
