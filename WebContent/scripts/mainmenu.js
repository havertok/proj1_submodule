let currentUser;

window.onload = function () {
    fillProfile();
    getUnderlings();

    // reimbutton.addEventListener("click", function () {
    //     getMyReims(session.getAttribute("username"));
    // });
}

function fillProfile() {
    fetch("http://localhost:8089/proj1/session")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            if (data.session === null) {
                window.location = "http://localhost:8089/proj1/login";
            } else {
                user = data;
                currentUser = user.username;
                document.getElementById("username").innerText = "Username: " + user.username;
                document.getElementById("fullname").innerText = user.lname + ", " + user.fname;
                document.getElementById("email").innerText = "Email: " + user.email;
                document.getElementById("managername").innerText = "Suzerain: " + user.managername;
                //we are getting this logged in user's name
                getMyReims("reimlist", user.username);
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
                if(data.length == 0){
                    //Toggle display of manager submenu
                }
                for (let i = 0; i < data.length; i++) {
                    let underman = document.createElement("li");
                    let underbutton = document.createElement("input");
                    //Adding buttons to users
                    underbutton.setAttribute("type", `submit`);
                    //the button's id will be based on the username
                    //hitting this will change the reims displayed in
                    //id = employeeview
                    underbutton.setAttribute("id", `${data[i]}-showReims`);
                    underbutton.setAttribute("class", `btn-primary`);
                    underbutton.innerText = data[i].username
                    underbutton.addEventListener("click", function () {
                        getUserReims(data[i])
                    });
                    //adding button to list item and list item to list
                    underman.innerText = `${data[i].fname} ${data[i].lname}`;
                    underman.appendChild(underbutton);
                    underlist.appendChild(underman);
                }
            }
        })
}

//We get the user, so that we can append the reims and a button form
//based on that user when binding an event listener
function getUserReims(user) {
    let empView = document.createElement("ul");
    //Clears the employee view so only one user's reims are displayed
    let empViewCont = document.getElementById("employeeViewContainer");
    while (empViewCont.firstChild) {
        empViewCont.removeChild(empViewCont.firstChild);
    }
    let reims = user.myReimbursements
    for (let i = 0; i < reims.length; i++) {
        let reim = document.createElement("li");
        reim.innerHTML = reimTemplateBuilder(reims[i]);
        empView.appendChild(reim);
        empViewCont.appendChild(empView);
        //Do not add a button if the reim is not pending
        if (reims[i].status == 0) {
            let reimForm = buttonTemplateBuilder(reims[i]);
            empView.appendChild(reimForm);
            document.getElementById(`btn-${reims[i].id}`).addEventListener('click', function(){
                let formData = new FormData(document.forms.namedItem('submitReimStatus'));
                console.log(formData);
                fetch('http://localhost:8089/proj1/underlingreims', {
                    method: 'POST',
                    body: formData
                }).then(
                    getMyReims("employeeViewContainer", reims[i].empUsername)
                )
            })
        }
    }
}

//Ids of elements are subordinateReim#
//Though I also use a hidden input to store the Reim ID
function buttonTemplateBuilder(reimbursement) {
    let reimButtonForm = document.createElement("form");
    reimButtonForm.innerHTML =
        `<select name="toStatus" id="">
        <option value="accept">Accept</option>
        <option value="reject">Reject</option>
        <input type="hidden" name="reimId" value="${reimbursement.id}">
        <input type="hidden" name="manName" value="${currentUser}">
    </select>
    <input id="btn-${reimbursement.id}" class="btn-primary" type="button" placeholder="SUBMIT">`;
    reimButtonForm.setAttribute("name", 'submitReimStatus');

    return reimButtonForm;
}

//element is the id of the element we are adding into
//so this can be used with multiple divs
function getMyReims(element, username) {
    //console.log("getmyreims username=" + username);
    let clearMe = document.getElementById(element);
    while (clearMe.firstChild) {
        clearMe.removeChild(clearMe.firstChild);
    }
    fetch("http://localhost:8089/proj1/reimbursements", {
        method: 'POST',
        body: username,
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            if (data === null) {
                alert("Data response for reimbursements was null");
            } else {
                let reimlist = document.getElementById(element);
                for (let i = 0; i < data.length; i++) {
                    let reim = document.createElement("li");
                    let attribName = element == "reimlist" ? "reimitem" : "subordinateReim";
                    reim.setAttribute("id", `${attribName}${i}`);
                    reim.innerHTML = reimTemplateBuilder(data[i]);
                    //console.log(reim);
                    reimlist.appendChild(reim);
                }
            }
        })
}

function reimTemplateBuilder(reimObj) {
    let dateString;
    let day = reimObj.dateMade.dayOfMonth;
    let month = reimObj.dateMade.monthValue - 1;
    let year = reimObj.dateMade.year;
    dateString = `${day}-${month}-${year}`;
    let lux =
        `<span> Status: ${reimObj.status} for ${reimObj.amount} <br>
            by: ${reimObj.empUsername} on: ${dateString} <br>
            Approving Manager: ${reimObj.status === 1 ? reimObj.approvingManager : 'N/A'}<br>
            Reason: ${reimObj.notes}
        </span> <br>
        <img src="" alt="Reciept image goes here">`
    return lux;
}

