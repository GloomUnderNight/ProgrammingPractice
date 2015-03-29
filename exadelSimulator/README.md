Помимо основных полей для класса "Message" добавил еще одно - date.
Оно хранит дату о том, когда сообщение было написано.
Как следствие, запросы из Fiddler'а должны иметь следующий вид:

POST /chat HTTP/1.1
Host: localhost:999
User-Agent: Fiddler

{"id":"2", "user":"User 2", "message":"What’s up ?", "date":"Today"}
