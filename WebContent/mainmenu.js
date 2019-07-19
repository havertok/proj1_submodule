let Employee = {};

window.onload = function (){
    fillProfile();
    getUnderlings();
    getMyReims();
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
            let reimlist = document.getElementById("reimlist");
            for(let i = 0; i < data.length; i++){
                let reim = document.createElement("li");
                reim.setAttribute("id", `reimitem${i}`);
                reim.innerHTML = reimTemplateBuilder(data[i]);
                reimlist.appendChild(reim);
            }
        }
    })
}

function reimTemplateBuilder(reimObj){
    let lux = 
        `<span> Status: ${reimObj.status} for ${reimOjb.amount} <br>
            by: ${reimObj.empUsername} on: ${reimObj.dateMade} <br>
            Reason: ${reimObj.notes}
        </span>
        <img src="" alt="Reciept image goes here">`
    return lux;
}