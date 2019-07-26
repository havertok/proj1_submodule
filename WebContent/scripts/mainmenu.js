let currentUser;

window.onload = function () {
    document.getElementById("displayModal").style.display = "none";
    fillProfile();
    getUnderlings();
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
                let managerViewButton = document.getElementById("displayModal");
                if (data.length > 0) {
                    //Only performs this block if he is a manager
                    managerViewButton.style.display = "block";
                    populateManagerList();
                    document.getElementById("getPendingReims").addEventListener('click',
                        function () {
                            fillManagerReims("0");
                        })
                    document.getElementById("getApprovedReims").addEventListener('click',
                        function () {
                            fillManagerReims("1");
                        })
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
                        getUserReims(data[i]);
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
            document.getElementById(`btn-${reims[i].id}`).addEventListener('click', function () {
                let formData = new FormData(document.forms.namedItem('submitReimStatus'));
                //This operates on the local reimlist, so we need to change this local
                //list to reflect changes without refetching
                reims[i].status = formData.get("toStatus") == 'accept' ? 1 : 2;
                fetch('http://localhost:8089/proj1/underlingreims', {
                    method: 'POST',
                    body: formData
                }).then(
                    getUserReims(reims[i].empUsername)
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
    <input id="btn-${reimbursement.id}" class="btn-primary" type="button" value="CHANGE">`;
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
//Builds a nice(ish) looking table 
function reimTemplateBuilder(reimObj) {
    let dateString;
    let day = reimObj.dateMade.dayOfMonth;
    let month = reimObj.dateMade.monthValue - 1;
    let year = reimObj.dateMade.year;
    dateString = `${day}-${month}-${year}`;
    let lux =
        `<table border="1">
        <tr>
            <th>Status</th>
            <th>Amount</th>
            <th>Made By</th>
            <th>Approved By</th>
            <th>Date Made</th>
        </tr>
        <tr>
            <td>${reimObj.status} </td>
            <td>${reimObj.amount} </td>
            <td>${reimObj.empUsername}</td>
            <td>${reimObj.status === 1 ? reimObj.approvingManager : 'N/A'}</td>
            <td>${dateString}</td>
        </tr>
        <tr>
            ${reimObj.notes}
        </tr>
    </table>
        <img src="data:image/jpg;base64,${reimObj.reciept}" alt="[No Image]">`
    return lux;
}

function populateManagerList() {
    fetch("http://localhost:8089/proj1/getunderlings", {
        method: 'POST'
    }).then(function (response) {
        return response.json();
    }).then(function (data) {
        if (data != null) {
            let manTable = document.getElementById('managerList');
            for (let i = 0; i < data.length; i++) {
                console.log(data[i]);
                let tableRow = document.createElement("tr");
                tableRow.setAttribute("id", `manTableData${data[i].username}`)
                tableRow.innerHTML =
                `<td id="manTableName">${data[i].fname} ${data[i].lname}</td>
                <td id="manTableUsername">${data[i].username}</td>
                <td id="manTableSuperior">${data[i].managername}</td>`
                //I want it align, so it needs a td element
                let button = document.createElement("td");
                if (data[i].manager == true) {
                    button.innerHTML =
                    `<td id="manTableButton${data[i].username}">Press Me</td>`;
                    button.setAttribute("class", "btn-primary");
                    button.addEventListener('click', function () {
                        fillManagerReims(`${data[i].username}`);
                    })
                } 
                tableRow.appendChild(button);
                manTable.appendChild(tableRow);
                // document.getElementById(`manTableButton${data[i].username}`).
                //     addEventListener('click', function () {
                //         fillManagerReims(`${data[i].username}`);
                //     })
            }
        }
    })
}

function fillManagerReims(determinant) {
    fetch("http://localhost:8089/proj1/listReimsByStatus", {
        method: 'POST',
        body: determinant,
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(function (response) {
        return response.json();
    }).then(function (data) {
        let managerReimView = document.getElementById("reimbursementView");
        while (managerReimView.firstChild) {
            managerReimView.removeChild(managerReimView.firstChild);
        }
        for (let i = 0; i < data.length; i++) {
            let reim = document.createElement("span");
            reim.innerHTML = reimTemplateBuilder(data[i]);
            //console.log(reim);
            managerReimView.appendChild(reim);
        }
    })
}
