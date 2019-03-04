<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>8 Марта: Квест</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Ruslan+Display' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style type="text/css">
        @font-face{
            font-family: 'Ruslan Display';
            src: url('http://fonts.googleapis.com/css?family=Ruslan+Display');
        }
        body {
            font-family: 'Ruslan Display', sans-serif;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
        <tr>
            <td align="center" bgcolor="#2b79d8" style="padding: 40px 0 30px 0;">
                <img src="cid:logo.png" alt="С праздником 8 Марта" style="display: block; width:200px; height: 200px;" />
            </td>
        </tr>
        <tr>
            <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
                <p><h2>Здравствуйте, ${user.name}!</h2></p>
                <#if host??>
                    <p>Для сайта: ${host}.<br />
                    Используйте следующие учетные данные: <br />
                </#if>
                Логин: <font bold> ${user.username} </font> <br />
                Пароль: ${password}</p>

                <hr />
                <div align="center">
                    <h3>С праздником 8 Марта!</h3>
                    Мужская половина коллектива <br />
                    Вас поздравляет с праздником весны.<br />
                    Коллеги, вы прелестны и красивы,<br />
                    Как нежные, прекрасные цветы!<br />
                    <br />
                    Такими же всегда и оставайтесь.<br />
                    Круглогодично — летом и зимой.<br />
                    Живите ярко, чаше улыбайтесь<br />
                    И украшайте этот мир собой!<br />
                </div>
            </td>

        </tr>
        <tr>
            <td align="center" bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
                <p>${signature}</p>
            </td>
        </tr>
    </table>

</body>
</html>