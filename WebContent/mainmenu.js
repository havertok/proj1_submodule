let Employee = {};

window.onload = function (){
    fillProfile();
    //add func to display reimbursements
    getUnderlings();
}

function fillProfile(){
    //Trying fetch
    fetch("http://localhost:8089/proj1/session")
    .then(function(response){
        return response.json();
    })
    .then(function(data){
        if(data.session === null){
            window.location = "http://localhost:8089/proj1/login";
        } else {
            user = data;
            document.getElementById("username").innerText = "Username: "+user.username;
            document.getElementById("fullname").innerText = user.lname+", "+user.fname;
            document.getElementById("email").innerText = "Email: "+user.email;
            document.getElementById("managername").innerText = "Suzerain: "+user.managername;
            document.getElementById("pageTitle").innerText = `Profile of ${user.username}`;
        }
    })
}

function getUnderlings(){
    fetch("http://localhost:8089/proj1/getunderlings")
    .then(function(response){
        return response.json();
    })
    .then(function(data){
        if(data === null){
            alert("Data response for getUnderlings was null");
        } else {
            let underlist = document.getElementById("underlinglist");
            for(let i = 0; i < data.length; i++){
               let underman = document.createElement("li")
               underman.innerText = data[i].username;
               underlist.appendChild(underman);
            }
        }
    })
}

function getMyReims(){
    fetch("http://localhost:8089/proj1/reimbursements")
    .then(function(response){
        return response.json();
    })
    .then(function(data){
        if(data === null){
            alert("Data response for reimbursements was null");
        } else {
            let reimlist = document.getElementById("underlinglist");
            for(let i = 0; i < data.length; i++){
               let reim = document.createElement("li")
               reim.innerText = reimTemplateBuilder(data[i], i);
               reimlist.appendChild(underman);
            }
        }
    })
}

function reimTemplateBuilder(reimObj, number){
    let lux = 
    `<li id="reimitem${number}">
        <span> Status: ${reimObj.status} for ${reimOjb.amount} \n
            by: ${reimObj.empUsername} on: ${reimObj.dateMade} \n
            Reason: ${reimObj.notes}
        </span>
        <img src="" alt="Reciept image goes here">
    </li>`;
    return lux;
}