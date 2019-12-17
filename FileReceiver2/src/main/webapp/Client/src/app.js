var App = Marionette.Application.extend({
    region: 'body',

    //Создание корневого представления
    onStart(){
        const root = new Root({});

        root.render();
        this.showView(root);
    }
});

//Запуск приложения
$(function () {
    new App().start();
});