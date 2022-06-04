<!DOCTYPE html>
<#import "spring.ftl" as spring/>

<head>
    <meta charset="UTF-8">
    <title>Gif and Exchanges</title>

</head>
<body>
<h1>Тестовое задание</h1>

<label for="Hello"> Курс ${defaultCurrencyBase} к ${defaultCurrencyTo} в GIF</label>
<br>
<br>
<button id="all" type="button" onclick="location.href ='${uriExchangeUSD}${defaultCurrencyTo}'"
>Узнать
</button>

</body>
</html>