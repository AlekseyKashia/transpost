require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /



    state: start
        q!: $regex</start>
        if: !$session.wasGreeted
            script:
                $session.wasGreeted = true;
            a: Здравствуйте! Чем я вам могу помочь? || html = "Здравствуйте! Чем я вам могу помочь?"
        else: 
            a: Чем могу помочь? || html = "Чем могу помочь?"
        a: 1. Получение информации по номеру заявления || htmlEnabled = false, html = "1. Получение информации по номеру заявления"
        intent: /Информация по заявлению || onlyThisState = false, toState = "/Nomer_Z"
        intent: /Нет || onlyThisState = false, toState = "/Exit"
        event: noMatch || onlyThisState = false, toState = "/Error"

    state: Error
        random:
            a: Извините, я не расслышала.
            a: Не могли бы вы повторить?
            a: Повторите, пожалуйста.
            a: Извините, я не смогла расслышать ваш ответ.
        go!: /start

    state: Info
        HttpRequest: 
            url = http://host1848748.hostland.pro/zapros.php/?oper=2&info="{{$request.query}}"
            method = GET
            dataType = json
            timeout = 0
            vars = [{"name":"quoteText","value":"$httpResponse.quoteText"}]
            okState = /Go_Info
            errorState = /Break

    state: Go_Info
        a: Мое число {{$session.quoteText}}. || htmlEnabled = false, html = "Мое число {{$session.quoteText}}."
        a: У вас есть ещё вопросы? || htmlEnabled = false, html = "У вас есть ещё вопросы?"
        intent: /Да || onlyThisState = false, toState = "/start"
        event: noMatch || onlyThisState = false, toState = "/Exit"

    state: Exit
        intent!: /пока
        a: Всего доброго, до свидания!
        EndSession:

    state: Break
        a: Простите произошла ошибка. Проверьте данные и повторите запрос. || htmlEnabled = false, html = "Простите произошла ошибка. Проверьте данные и повторите запрос."
        go!: /start

    state: Nomer_Z
        a: Укажите пожалуйста номер заявления || htmlEnabled = false, html = "Укажите пожалуйста номер заявления"
        event: noMatch || onlyThisState = false, toState = "/Info"