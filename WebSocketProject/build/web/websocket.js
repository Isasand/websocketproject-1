/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;
var socket = new WebSocket("ws://localhost:8080/WebSocketProject/actions");

socket.onmessage = onMessage;

function onMessage(event) {
    var sensor = JSON.parse(event.data);
    if(sensor.action=== "data"){
        var temp = document.getElementById("tempdata");
        temp.innerHTML = sensor.data + sensor.id + sensor.type;
    }
    if(sensor.action==="updatedata"){
        var temp = document.getElementById("tempdata");
        temp.innerHTML = sensor.data;
    }
}

// Toggle between showing and hiding the sidebar when clicking the menu icon
var mySidebar = document.getElementById("mySidebar");

var hideTable = true; 

function w3_open() {
    if (mySidebar.style.display === 'block') {
        mySidebar.style.display = 'none';
    } else {
        mySidebar.style.display = 'block';
    }
}

// Close the sidebar with the close button
function w3_close() {
    mySidebar.style.display = "none";
}

function toggleTable(){
    if (document.getElementById('table').classList.contains('hidden')){

      document.getElementById("table").classList.remove('hidden'); 
    }
    else{

    document.getElementById("table").classList.add('hidden'); 
    }

}

function generateTable(){

    if(!hideTable){
      document.getElementById("addTable").innerHTML = ""; 
      
    document.getElementById("btn_one").innerHTML = "Historical values"; 
      hideTable = true; 
    }
    else{

    document.getElementById("btn_one").innerHTML = "Hide"; 
    var myTableDiv = document.getElementById("addTable");

    var table = document.createElement('TABLE');
    table.classList.add("w3-striped");
    table.classList.add("w3-white"); 
    table.classList.add("w3-table"); 
    var tableBody = document.createElement('TBODY');
    table.appendChild(tableBody);


    var icons = new Array("fa fa-thermometer-3 w3-text-red w3-large", 
      "fa fa-thermometer w3-text-blue w3-large", 
      "fa fa-thermometer-2 w3-text-yellow w3-large", 
      "fa fa-thermometer-1 w3-text-blue w3-large", 
      "fa fa-thermometer-empty w3-text-green w3-large");

    var stock = new Array();
    stock[0] = new Array("Temp: 24.3", "2018-03-30 12:30:45.4");
    stock[1] = new Array("Temp: 24.3", "2018-03-30 12:30:45.4");
    stock[2] = new Array("Temp: 24.3", "2018-03-30 12:30:45.4");
    stock[3] = new Array("Temp: 24.3", "2018-03-30 12:30:45.4");
    stock[4] = new Array("Temp: 24.3", "2018-03-30 12:30:45.4");

for (i = 0; i < stock.length; i++) {
    var tr = document.createElement('TR');
    var td1 = document.createElement('TD');
    var italic = document.createElement("I");
    italic.innerHTML = '<i class="' + icons[i] +'"></i>';
    td1.appendChild(italic);
    tr.appendChild(td1);
    for (j = 0; j < stock[i].length; j++) {
        var td = document.createElement('TD')
        td.appendChild(document.createTextNode(stock[i][j]));
        tr.appendChild(td);
    }
    tableBody.appendChild(tr);
}
myTableDiv.appendChild(table)
hideTable = false; 
}
}