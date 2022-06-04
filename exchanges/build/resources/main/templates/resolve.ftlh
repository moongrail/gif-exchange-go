<!DOCTYPE html>
<#import "spring.ftl" as spring/>

<head>
    <meta charset="UTF-8">
    <title>Gif and Exchanges</title>

</head>
<body>

<p>Курс USD к ${currencyName}: </p>
<p>Актуальные значения на сегодня.</p>
<table border="1" cellspacing="0" cellpadding="1">
    <tr class="tableHeader">
        <td>USD</td>
        <td>${currencyName}</td>
    </tr>
    <tr class="tableBody">
        <td>1</td>
        <td>${currencyValue}</td>
    </tr>
</table>

<p>USD : 1 </p>
<p>Сегодня: ${currencyValue} </p>
<p>Вчера: ${yesterdayCurrencyValue} </p>

<img src="${gifRandom}" alt="gif" width="250"/>

</body>
</html>