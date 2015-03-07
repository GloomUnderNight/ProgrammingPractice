var editFlag = false;
var oldTextPlace;
var connectionFlag = true;

function onNameButtonClick() {
    var username;
    username = document.getElementById('nameField');
    changeName(username.value)
}

function changeName(value){
    if(!value){
        return;
    }
    var name = document.getElementById('userNameField');
    renameDeclaration(value);
    name.innerHTML = value;
}

function renameDeclaration(value){
    if (document.getElementById('userNameField').innerHTML == '') {
        insertMessage("System",  (value + " has joined this chat."));
    }
    else {
        insertMessage("System",(document.getElementById('userNameField').innerHTML + " has changed name to " + value));
    }
}

function sendMessage(){
    var userName = document.getElementById('userNameField').innerHTML;
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
    insertUserMessage(userName, newMessage.value);
    newMessage.value = '';
}

function insertUserMessage(userName, newMessage){
    var message = document.createElement('tr');
    message.classList.add('userMessageField');
    var messageFunctions = userFunctionsForming();
    var messageName = nameMessageForming(userName);
    var messageText = messageTextForming(newMessage);
    var date = getTempDate();
    message.appendChild(messageFunctions);
    message.appendChild(messageName);
    message.appendChild(messageText);
    message.appendChild(date);
    var destination = document.getElementById('chat');
    destination.appendChild(message);
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

function getTempDate() {
    var date = document.createElement('td');
    var currentDate = new Date();
    date.innerHTML =  currentDate.getHours() + ":"
    + currentDate.getMinutes() + ":"
    + currentDate.getSeconds() + " "
    + currentDate.getDate() + "."
    + (currentDate.getMonth() + 1) + "."
    + currentDate.getFullYear();
    return date;
}

function deleteMessage(thisButton){
    var parent = thisButton.parentNode.parentNode;
    parent.parentNode.removeChild(parent);
}

function insertMessage(userName, newMessage){
    var message = document.createElement('tr');
    message.classList.add('messageField');
    var messageFunctions = FunctionsForming();
    var messageName = nameMessageForming(userName);
    var messageText = messageTextForming(newMessage);
    var date = getTempDate();
    message.appendChild(messageFunctions);
    message.appendChild(messageName);
    message.appendChild(messageText);
    message.appendChild(date);
    var destination = document.getElementById('chat');
    destination.appendChild(message);
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
    oldTextPlace = button.parentNode.parentNode.childNodes.item(2);
    document.getElementById('textInput').value = oldTextPlace.innerHTML;
}

function editMessage(){
    if (editFlag == false){
        window.alert("You have to select message to edit firstly!");
        return;
    }
    editFlag = false;
    oldTextPlace.innerHTML = document.getElementById('textInput').value;
    document.getElementById('textInput').value = '';
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