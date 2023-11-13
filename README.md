# gif-exchange-go

## Cервис, который обращается к сервису курсов валют, и отображает gif:
• если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную отсюда https://giphy.com/search/rich
• если ниже - отсюда https://giphy.com/search/broke



## Запуск IDEA Gradle
1) Зайти меню Gradle
2) Перейти Tasks-build запустить clean, затем build
3) В папке build/lib лежит JAR file с названием приложения
4) Запустить консоль, перейти в директорию приложения в консоли
5) Запустить приложение командой java -jar exchanges-00.00.01.jar

## Запуск в Docker

1) docker build -t exchanges .
2) docker -run -p 80:80 exchanges:latest

## HTTP 

* GET http://localhost:80/root - главная страница 

* GET http://localhost:80/resolve?symbol={КОД ВАЛЮТЫ} - страница с изображением всех ответов с API

* GET http://localhost:80/resolve/v1/random-gif-rich - ответ json с url rich gif

* GET http://localhost:80/resolve/v1/random-gif-broke - ответ json с url broke gif

* GET http://localhost:80/resolve/v1/today/{КОД ВАЛЮТЫ} - ответ json с валютой текущие значения.

* GET http://localhost:80/resolve/v1/yesterday/{КОД ВАЛЮТЫ} - ответ json с валютой значения день назад.
