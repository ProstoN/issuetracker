package com.axmor;
/**
 * Для начала работы необходимо ввести 4 аргумента.
 * Первый аргумент передает информацию о том, необходимо ли создание базы данных,
 * в случае надобности/ненадобсти указать верный аргумент (-yes/-no).
 * Второй аргумент передает имя базы данных (-dbUser) пример (-root).
 * Третий аргумен передает пароль от базы данных (-dbPassword) пример (-admin).
 * Четвертый аргумент передает название базы данных (-database) пример (-axmor)
 * Готовый пример аргументов будет выглядить как -yes -root -admin -axmor
 * */

public class Main {
    public static void main(String[] args) {
        Initializer initializer = new Initializer();
        initializer.initialize(args);
    }
}
