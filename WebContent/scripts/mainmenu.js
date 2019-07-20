let Employee = {};

window.onload = function () {
    fillProfile();
    getUnderlings();
    getMyReims();
}

function fillProfile() {
    //Trying fetch
    fetch("http://localhost:8089/proj1/session")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            if (data.session === null) {
                window.location = "http://localhost:8089/proj1/login";
            } else {
                user = data;
                document.getElementById("username").innerText = "Username: " + user.username;
                document.getElementById("fullname").innerText = user.lname + ", " + user.fname;
                document.getElementById("email").innerText = "Email: " + user.email;
                document.getElementById("managername").innerText = "Suzerain: " + user.managername;
            }
        })
}

function getUnderlings() {
    fetch("http://localhost:8089/proj1/getunderlings")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            if (data === null) {
                alert("Data response for getUnderlings was null");
            } else {
                let underlist = document.getElementById("underlinglist");
                for (let i = 0; i < data.length; i++) {
                    let underman = document.createElement("li");
                    let underbutton = document.createElement("input");
                    //Adding buttons to users
                    underbutton.setAttribute("type", `submit`);
                    underbutton.setAttribute("id", `userbutton${i}`);
                    underbutton.setAttribute("class", `btn-primary`);
                    underbutton.addEventListener("click", function () {
                        getUserPage(data[i].username)
                    });
                    //adding button to list item and list item to list
                    underman.innerText = `${data[i].fname} ${data[i].lname}`;
                    underman.appendChild(underbutton);
                    underlist.appendChild(underman);
                }
            }
        })
}

//Should make a fetch to a java servlet that will redirect to a new html page
function getUserPage(username) {
    alert(username);
}

function getMyReims() {
    fetch("http://localhost:8089/proj1/reimbursements")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            if (data === null) {
                alert("Data response for reimbursements was null");
            } else {
                let reimlist = document.getElementById("reimlist");
                for (let i = 0; i < data.length; i++) {
                    let reim = document.createElement("li");
                    reim.setAttribute("id", `reimitem${i}`);
                    reim.innerHTML = reimTemplateBuilder(data[i]);
                    console.log(reim);
                    reimlist.appendChild(reim);
                }
            }
        })
}

function reimTemplateBuilder(reimObj) {
    //let date = reimObj.dateMade.toDateString();
    let lux =
        `<span> Status: ${reimObj.status} for ${reimObj.amount} <br>
            by: ${reimObj.empUsername} on: ${reimObj.dateMade} <br>
            Reason: ${reimObj.notes}
        </span> <br>
        <img src="" alt="Reciept image goes here">`
    return lux;
}