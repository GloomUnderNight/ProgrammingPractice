var editFlag = false;
var oldTextPlace;
var connectionFlag = true;
var username = 0;
var messageList = [];
var tmpMsgEdit;

var theMessage = function(text, user, changeable, date){
    return {
        messageText: text,
        messageUser: user,
        messageFlag: changeable,
        messageDate: date
    };
};

function run() {
    username = restoreUsername() || 0;
    if (username != 0){
        var tmp = document.getElementById('nameField');
        tmp.value = username;
    }
    messageList = restoreMessages() || [theMessage('Hi, welcome to our chat!','System',false,getTempDate())];
    fillMessageArea(messageList);
}

function restoreUsername(){
    if(typeof(Storage) == "undefined") {
        alert('Local Storage is not accessible');
        return;
    }

    var item = localStorage.getItem("Username");

    return item && JSON.parse(item);
}

function restoreMessages(){
    if(typeof(Storage) == "undefined") {
        alert('Local Storage is not accessible');
        return;
    }

    var item = localStorage.getItem("Message List");

    return item && JSON.parse(item);
}

function store(listToSave) {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    localStorage.setItem("Message List", JSON.stringify(listToSave));
}

function fillMessageArea(messageList){
    for (var i = 0; i < messageList.length; i++){
        if (messageList[i].messageFlag == true){
            insertUserMessage(messageList[i].messageUser, messageList[i].messageText, messageList[i].messageDate)
        }
        else {
            insertMessage(messageList[i].messageUser, messageList[i].messageText, messageList[i].messageDate)
        }
    }
}

function onNameButtonClick() {
    var tmp = document.getElementById('nameField');
    if (tmp.value == username){
        return;
    }
    changeName(tmp.value);
}

function changeName(value){
    if(!value){
        return;
    }
    renameDeclaration(value);
    username = value;
    localStorage.setItem("Username", JSON.stringify(value));
}

function renameDeclaration(value){
    if (username == 0) {
        insertMessage("System",  (value + " has joined this chat."), 0);
    }
    else {
        insertMessage("System",(username + " has changed name to " + value), 0);
    }
}

function check(msg){
    for (var i = 0; i < messageList.length; i++){
        if (msg.messageUser == messageList[i].messageUser && msg.messageDate == messageList[i].messageDate &&
            msg.messageFlag == messageList[i].messageFlag && msg.messageText == messageList[i].messageText){
            return false;
        }
    }
    return true;
}

function sendMessage(){
    var userName = username;
    if (userName == ''){
        window.alert('You have to sign up at first!');
        return;
    }
    if (editFlag == true){
        window.alert('You cant send new messages while editing the old one!');
        return;
    }
    var newMessage = document.getElementById('textInput');
    if (newMessage.value == '') {
        return;
    }
    insertUserMessage(userName, newMessage.value, 0);
    newMessage.value = '';
}

function insertUserMessage(userName, newMessage, date){
    var message = document.createElement('tr');
    message.classList.add('userMessageField');
    var messageFunctions = userFunctionsForming();
    var messageName = nameMessageForming(userName);
    var messageText = messageTextForming(newMessage);
    if (date == 0) {
        date = getTempDate();
    }
    var d = dateForming(date);
    message.appendChild(messageFunctions);
    message.appendChild(messageName);
    message.appendChild(messageText);
    message.appendChild(d);
    var msg = theMessage(newMessage,userName,true,date);
    if (check(msg) == true) {
        messageList.push(msg);
    }
    var destination = document.getElementById('chat');
    destination.appendChild(message);
    store(messageList);
}

function userFunctionsForming (){
    var messageFunctions = document.createElement('td');
    var editButton = document.createElement('input');
    editButton.setAttribute('type', 'button');
    editButton.classList.add('simpleButton');
    editButton.value = 'Edit';
    editButton.addEventListener('click', function(){editButtonClicked(this);});
    var deleteButton = document.createElement('input');
    deleteButton.setAttribute('type', 'button');
    deleteButton.classList.add('simpleButton');
    deleteButton.value = 'Delete';
    deleteButton.addEventListener('click', function(){deleteMessage(this);});
    messageFunctions.appendChild(deleteButton);
    messageFunctions.appendChild(editButton);
    return messageFunctions;
}

function nameMessageForming (userName){
    var messageName = document.createElement('td');
    messageName.innerHTML = userName;
    return messageName;
}

function messageTextForming(newMessage) {
    var messageText = document.createElement('td');
    messageText.innerHTML = newMessage;
    return messageText;
}

function dateForming(dat){
    var date = document.createElement('td');
    date.innerHTML = dat;
    return date;
}

function getTempDate() {
    var date;
    var currentDate = new Date();
    date =  currentDate.getHours() + ":"
    + currentDate.getMinutes() + ":"
    + currentDate.getSeconds() + " "
    + currentDate.getDate() + "."
    + (currentDate.getMonth() + 1) + "."
    + currentDate.getFullYear();
    return date;
}

function deleteMessage(thisButton){
    var parent = thisButton.parentNode.parentNode;
    var place = thisButton.parentNode.parentNode.childNodes.item(3);
    var tmpUser;
    for (var i = 0; i < messageList.length; i++){
        if (messageList[i].messageFlag != false){
            if (messageList[i].messageDate == place.innerHTML){
                tmpUser = messageList[i].messageUser;
                messageList[i].messageText = 'This message was deleted by ' + messageList[i].messageUser + '.';
                messageList[i].messageFlag = false;
                messageList[i].messageUser = 'System';
                messageList[i].messageDate = getTempDate();
            }
        }
    }
    store(messageList);
    var message = document.createElement('tr');
    message.classList.add('messageField');
    var messageFunctions = FunctionsForming();
    var messageName = nameMessageForming('System');
    var messageText = messageTextForming('This message was deleted by ' + tmpUser + '.');
    var d = dateForming(getTempDate());
    message.appendChild(messageFunctions);
    message.appendChild(messageName);
    message.appendChild(messageText);
    message.appendChild(d);
    parent.parentNode.replaceChild(message,parent);
}

function insertMessage(userName, newMessage, date){
    var message = document.createElement('tr');
    message.classList.add('messageField');
    var messageFunctions = FunctionsForming();
    var messageName = nameMessageForming(userName);
    var messageText = messageTextForming(newMessage);
    if (date == 0) {
        date = getTempDate();
    }
    var d = dateForming(date);
    message.appendChild(messageFunctions);
    message.appendChild(messageName);
    message.appendChild(messageText);
    message.appendChild(d);
    var msg = theMessage(newMessage,userName,false,date);
    if (check(msg) == true) {
        messageList.push(msg);
    }
    var destination = document.getElementById('chat');
    destination.appendChild(message);
    store(messageList);
}

function FunctionsForming (){
    return document.createElement('td');
}

function editButtonClicked(button){
    if (editFlag == true){
        window.alert("You are editing one message already!");
        return;
    }
    editFlag = true;
    var oldDatePlace = button.parentNode.parentNode.childNodes.item(3);
    oldTextPlace = button.parentNode.parentNode.childNodes.item(2);
    document.getElementById('textInput').value = oldTextPlace.innerHTML;
    for (var i = 0; i < messageList.length; i++){
        if (messageList[i].messageFlag != false){
            if (messageList[i].messageDate == oldDatePlace.innerHTML){
                tmpMsgEdit = messageList[i];
            }
        }
    }
}

function editMessage(){
    if (editFlag == false){
        window.alert("You have to select message to edit firstly!");
        return;
    }
    editFlag = false;
    oldTextPlace.innerHTML = document.getElementById('textInput').value + '<br><br><h6>Message was changed at</h6>' + getTempDate();
    tmpMsgEdit.messageText = document.getElementById('textInput').value + '<br><br><h6>Message was changed at</h6>' + getTempDate();
    document.getElementById('textInput').value = '';
    store(messageList);
}

function changeIcon(icon){
    if (connectionFlag == true){
        connectionFlag = false;
        icon.src="nowifi.png";
    }
    else{
        connectionFlag = true;
        icon.src="wifi.png"
    }
}