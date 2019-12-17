const Root = Marionette.View.extend({
    template: _.template('<p>Загрузите wav для конвертации в aiff</p>\n' +
        '    <p><input id="sendWavChoose" type="file" accept="audio/wav">\n' +
        '        <input id="sendWav" type="submit" value="Отправить"></p>\n' +
        '<p>Загрузите aiff для конвертации в wav</p>\n' +
            '    <p><input id="sendAiffChoose" type="file" accept="audio/aiff">\n' +
        '        <input id="sendAiff" type="submit" value="Отправить"></p>\n'),

    events: {
        'click #sendWav': 'sendWavClick',
        'click #sendAiff': 'sendAiffClick'
    },

    sendAiffClick(){
        let controller = new RootController();
        controller.sendAiffFile();
    },

    sendWavClick(){
        let controller = new RootController();
        controller.sendWavFile();
    }
});