Для начала работы необходимо ввести 5 параметров.
 * Первый аргумент передает информацию о том, необходимо ли создание базы данных, в случае надобности/ненадобсти указать верный аргумент (-yes/-no).
 * Второй аргумент передает имя от вашего аккаунта MySQL(-dbUser) пример (-root).
 * Третий аргумен передает пароль от вашего аккаунта MySQL (-dbPassword) пример (-admin).
 * Четвертый аргумент передает название базы данных (-database) пример (-axmor)
 * Пятый аргумент передает место хранение базы данных примеры -localhost или -sql7.freemysqlhosting.net (онлайн сервер для хранения базы данных) 
 * Готовый пример аргументов будет выглядить как -yes -root -admin -axmor -localhost

Так как в своей работе я использую MySQL, то я предлагаю два варианта событий:
1) Если у вас есть доступ к своей базе данных MySQL вы можете использовать вариант примера что использован выше просто вводя параметры своего аккаунта и сами называете базу данных
2) В случае если у вас нет доступа к своей базе данных, то вы можете использовать аккаунт специально созданный для этого тестового задания 
команда для доступа (-yes -sql7283878 -PNmhtWjWRv -sql7283878 -sql7.freemysqlhosting.net). Дальше программа сделает все самостоятельно.
Различие этих двух вариантов в коде отличается тем, что в первом случае мы удаляем базу данных, во втором только таблицы (из-за того что БД находится на стороннем сервере, то доступа к нему у меня нет).

Зачем писать в первом аргументе -yes и -no?
При самом первом запуске вам нужно в любом случае нажать -yes, иначе произойдет ошибка, что программа не сможет найти базу данных с данным именем. Это что касается случая когда используется localhost (ну или ваш аккаунт).
Во всех остальных же, yes будет перезаписывать существующый таблицы и создавать все заново.
Если напишите -no, то все данные сохранятся и вы можете их использовать (опять же при самом первом запуске при вводе -no вылетит ошибка)

