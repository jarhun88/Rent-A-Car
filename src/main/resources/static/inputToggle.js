$( document ).ready(function()
{
    const localURL = "http://localhost:8080";

    $("#make-reservation-fields").hide(); 
    $("#make-return-fields").hide();

    $("#rs").hide();
    $("#r-rent").hide();
    $("#r-return").hide();
    $("#vehicle").hide(); 
    $("#vehicle-type").hide(); 
    $("#customers").hide(); 
    $("#branch-box").hide();
    $("#primary-key").hide();
    $("#update-header").hide();


    // hides "reservation" inputs when you click "view-vehicles"
    $("#view-vehicles").click(function() {
        $("#make-reservation-fields").hide(); 
        $("#view-vehicles-fields").show(); 
    })

    // hides "view-vehicle" inputs when you click "reservation"
    $("#make-reservation").click(function() {
        $("#view-vehicles-fields").hide(); 
        $("#make-reservation-fields").show(); 
    })

    // hides "return" inputs when you click "rent"
    $("#rent").click(function() {
        $("make-return-fields").hide(); 
        $("#make-rental-fields").show(); 
    })

    // hides "rent" inputs when you click "return"
    $("#return").click(function() {
        $("#make-rental-fields").hide(); 
        $("#make-return-fields").show(); 
    })

    let tableFn = 0;

    $("#add").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        $("#rentals").prop("disabled", true);
        $("#reservation").prop("disabled", true);
        $("#returns").prop("disabled", true);
        $("#rentals").prop("checked", false);
        $("#reservation").prop("checked", false);
        $("#returns").prop("checked", false);
        tableFn = 0;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#delete").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        tableFn = 1;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#update").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        $("#branch-input").prop("disabled", true);
        tableFn = 2;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#view").click(function() {
        enableInputs()
        $("#all").prop("disabled", false)
        tableFn = 3;
        switchCustomerInputs(tableFn, tableType);
    })

    let tableType = 0;

    $("#reservation").click(function() {
       tableType = 0;
       switchCustomerInputs(tableFn, tableType);
    })

    $("#rentals").click(function() {
        tableType = 1;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#returns").click(function() {
        tableType = 2;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#vehicles").click(function() {
        tableType = 3;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#vehicleTypes").click(function() {
        tableType = 4;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#customer").click(function() {
        tableType = 5;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#branch-input").click(function() {
        tableType = 6;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#all").click(function() {
        tableType = 7;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#m-button").click(function() {
        let config = {
            headers: {'Access-Control-Allow-Origin': '*'}
        };
        let body;
        switch(tableType) {
            case 0:
                body = reservationHelper(tableFn);
                console.log("reso body: " + body);
                axios.post(localURL + "/res", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 1: 
                body = rentalHelper(tableFn);
                console.log("rent body: " + body);
                axios.post(localURL + "/ren", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 2: 
                body = returnHelper(tableFn);
                console.log("return body: " + body);
                axios.post(localURL + "/ret", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 3: 
                body = vehiclesHelper(tableFn);
                console.log("vehicle body: " + body);
                axios.post(localURL + "/vehicle", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 4:
                body = vehicleTypeHelper(tableFn);
                console.log("vehicleType body: " + body);
                axios.post(localURL + "/vehicle-type", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 5: 
                body = customerHelper(tableFn);
                console.log("customer body: " + body);
                axios.post(localURL + "/customer", body, config).then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
            case 6: 
            body = branchHelper(tableFn);
            console.log("branch body: " + body);
            axios.post(localURL + "/branch", body, config).then((response) => {
                responseText = JSON.stringify(response.data);
                console.log(responseText)
                document.getElementById("table-output").innerHTML = responseText;
            }).catch((error) => {
                responseText = JSON.stringify(error.data);
                console.log(responseText)
                document.getElementById("table-output").innerHTML = responseText;
            })  
            break;
            case 7:
                console.log("viewall");
                axios.get(localURL + "/view-all").then((response) => {
                    responseText = JSON.stringify(response.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = JSON.stringify(error.data);
                    console.log(responseText)
                    document.getElementById("table-output").innerHTML = responseText;
                })  
                break;
        }
    })
})

function reservationHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mReservationBody("add");
            break;
        case 1:
            body = mReservationBody("remove");
            break;   
        case 2:
            body = mReservationBody("update");
            break; 
        case 3:
            body = mReservationBody("view");
            break;
    }
    return body;
}

function rentalHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mRentBody("add");
            break;
        case 1:
            body = mRentBody("remove");
            break;   
        case 2:
            body = mRentBody("update");
            break; 
        case 3:
            body = mRentBody("view");
            break;
    }
    return body;
}

function returnHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mReturnBody("add");
            break;
        case 1:
            body = mReturnBody("remove");
            break;   
        case 2:
            body = mReturnBody("update");
            break; 
        case 3:
            body = mReturnBody("view");
            break;
    }
    return body;
}

function vehiclesHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mVehicleBody("add");
            break;
        case 1:
            body = mVehicleBody("remove");
            break;   
        case 2:
            body = mVehicleBody("update");
            break; 
        case 3:
            body = mVehicleBody("view");
            break;
    }
    return body;
}

function vehicleTypeHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mVehicleTypeBody("add");
            break;
        case 1:
            body = mVehicleTypeBody("remove");
            break;   
        case 2:
            body = mVehicleTypeBody("update");
            break; 
        case 3:
            body = mVehicleTypeBody("view");
            break;
    }
    return body;
}

function customerHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mCustomerBody("add");
            break;
        case 1:
            body = mCustomerBody("remove");
            break;   
        case 2:
            body = mCustomerBody("update");
            break; 
        case 3:
            body = mCustomerBody("view");
            break;
    }
    return body;
}

function branchHelper(tableFn) {
    let body;
    switch(tableFn) {
        case 0:
            body = mBranchBody("add");
            break;
        case 1:
            body = mBranchBody("remove");
            break;   
        case 2:
            body = mBranchBody("update");
            break; 
        case 3:
            body = mBranchBody("view");
            break;
    }
    return body;
}

function enableInputs() {
    $("#all").prop("checked", false);
    $("#rentals").prop("disabled", false);
    $("#reservation").prop("disabled", false);
    $("#vehicles").prop("disabled", false);
    $("#vehicleTypes").prop("disabled", false);
    $("#customer").prop("disabled", false);
    $("#returns").prop("disabled", false);
    $("#branch-input").prop("disabled", false);

}

function disableInputAll() {
    $("#all").prop("disabled", true);
}

function switchCustomerInputs(tableFn, tableType) {
    switch(tableFn) {
        // add
        case 0:
            console.log(tableType);
            $("#update-header").hide();
            $("#primary-key").show();
            if (tableType == 3) {
                $("#pk-title").text("vid");
                $("#primary-key").hide();
                $("#rs").hide();
                $("#r-rent").hide();
                $("#r-return").hide();
                $("#vehicle").show(); 
                $("#vehicle-type").hide(); 
                $("#customers").hide(); 
                $("#branch-box").hide(); 
            }
            else if (tableType == 4) {
                $("#pk-title").text("vtname");
                $("#rs").hide();
                $("#r-rent").hide();
                $("#r-return").hide();
                $("#vehicle").hide(); 
                $("#vehicle-type").show(); 
                $("#customers").hide(); 
                $("#branch-box").hide();
            }
            else if (tableType == 5) {
                $("#pk-title").text("phone number");
                $("#rs").hide();
                $("#r-rent").hide();
                $("#r-return").hide();
                $("#vehicle").hide(); 
                $("#vehicle-type").hide(); 
                $("#customers").show(); 
                $("#branch-box").hide(); 
            }
            else if (tableType == 6) {
                $("#rs").hide();
                $("#r-rent").hide();
                $("#r-return").hide();
                $("#vehicle").hide(); 
                $("#vehicle-type").hide(); 
                $("#customers").hide(); 
                $("#primary-key").hide();
                $("#branch-box").show(); 
            }
            break;
        // delete
        case 1:
            $("#rs").hide();
            $("#r-rent").hide();
            $("#r-return").hide();
            $("#vehicle").hide(); 
            $("#vehicle-type").hide(); 
            $("#customers").hide(); 
            $("#update-header").hide();
            $("#branch-box").hide(); 
            $("#primary-key").show();
            switch (tableType) {
                case 0:
                    $("#pk-title").text("confNo");
                    break;
                case 1:
                    $("#pk-title").text("rid");
                    break;
                case 2:
                    $("#pk-title").text("rid");
                    break;
                case 3:
                    $("#pk-title").text("vid");
                    break;
                case 4:
                    $("#pk-title").text("vtname");
                    break;
                case 5:
                    $("#pk-title").text("phone number");
                    break;
                case 6: 
                    $("#primary-key").hide();  
                    $("#branch-box").show(); 
            }
            console.log(tableType);
            break;
        // update
        case 2:
            $("#update-header").show();
            $("#rs").hide();
            $("#r-rent").hide();
            $("#r-return").hide();
            $("#vehicle").hide(); 
            $("#vehicle-type").hide(); 
            $("#customers").hide(); 
            $("#primary-key").show();
            console.log("update");
            switch (tableType) {
                case 0:
                    $("#pk-title").text("confNo");
                    $("#rs").show();
                    break;
                case 1:
                    $("#pk-title").text("rid");
                    $("#r-rent").show();
                    break;
                case 2:
                    $("#pk-title").text("rid");
                    $("#r-return").show();
                    break;
                case 3:
                    $("#pk-title").text("vid");
                    $("#vehicle").show(); 
                    break;
                case 4:
                    $("#pk-title").text("vtname");
                    $("#vehicle-type").show(); 
                    break;
                case 5:
                    $("#pk-title").text("phone number");
                    $("#customers").show(); 
                    break;                
                default:
                    $("#update-header").show();
            }
            break;
        // view
        case 3:
            $("#primary-key").hide();
            $("#update-header").hide();
            $("#rs").hide();
            $("#r-rent").hide();
            $("#r-return").hide();
            $("#vehicle").hide(); 
            $("#vehicle-type").hide(); 
            $("#customers").hide(); 
            $("#branch-box").hide(); 
            console.log(tableType);
            break;
    }
}

function mReservationBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "remove") {
        let confNo = $("#pk-input").val();
        body.set("confNo", confNo);
    }
    else if (mType === "update") {
        let confNo = $("#pk-input").val();
        let vtName = $("#rs-vtname").val();
        let phoneNum = $("#rs-phone-num").val();
        let from = $("#rs-from").val();
        let to = $("#rs-to").val();
        body.set("confNo", confNo);
        body.set("vtName", vtName);
        body.set("phoneNum", phoneNum);
        body.set("from", from);
        body.set("to", to);
    }
    // only other option is view
    return body;
}

function mRentBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "remove") {
        let rid = $("#pk-input").val();
        body.set("rid", rid);
    }
    else if (mType === "update") {
        let rid = $("#pk-input").val();
        let vid = $("#r-vid").val();
        let phoneNum = $("#r-phone-num").val();
        let from = $("#r-from").val();
        let to = $("#r-to").val();
        let odometer = $("#r-odometer").val();
        let cardName = $("#r-cardName").val();
        let cardNo = $("#r-cardNo").val();
        let expDate = $("#r-expDate").val();
        let confNo = $("#r-confNo").val();
        body.set("rid", rid);
        body.set("vid", vid);
        body.set("phoneNum", phoneNum);
        body.set("from", from);
        body.set("to", to);
        body.set("odometer", odometer);
        body.set("cardName", cardName);
        body.set("cardNo", cardNo);
        body.set("expDate", expDate);
        body.set("confNo", confNo);
    }
    // only other option is view
    return body;
}

function mReturnBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "remove") {
        let rid = $("#pk-input").val();
        body.set("rid", rid);
    }
    else if (mType === "update") {
        let rid = $("#pk-input").val();
        let date = $("#return-date").val();
        let odometer = $("#return-odometer").val();
        let fulltank = $("#return-fulltank").val();
        let value = $("#return-value").val();
        body.set("rid", rid);
        body.set("date", date);
        body.set("odometer", odometer);
        body.set("fulltank", fulltank);
        body.set("value", value);
    }
    // only other option is view
    return body;
}

function mVehicleBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "add") {
        let vid = $("#pk-input").val();
        let vlicense = $("#v-vlicense").val();
        let make = $("#v-make").val();
        let model = $("#v-model").val();
        let year = $("#v-year").val();
        let color = $("#v-color").val();
        let odometer = $("#v-odometer").val();
        let status = $("#v-status").val();
        let vtname = $("#v-vtname").val();
        let location = $("#v-location").val();
        let city = $("#v-city").val();
        body.set("vid", vid);
        body.set("vlicense", vlicense);
        body.set("make", make);
        body.set("model", model);
        body.set("year", year);
        body.set("color", color);
        body.set("odometer", odometer);
        body.set("status", status);
        body.set("vtname", vtname);
        body.set("location", location);
        body.set("city", city);
    }
    if (mType === "remove") {
        let vid = $("#pk-input").val();
        body.set("vid", vid);
    }
    else if (mType === "update") {
        let vid = $("#pk-input").val();
        let vlicense = $("#v-vlicense").val();
        let make = $("#v-make").val();
        let model = $("#v-model").val();
        let year = $("#v-year").val();
        let color = $("#v-color").val();
        let odometer = $("#v-odometer").val();
        let status = $("#v-status").val();
        let vtname = $("#v-vtname").val();
        let location = $("#v-location").val();
        let city = $("#v-city").val();
        body.set("vid", vid);
        body.set("vlicense", vlicense);
        body.set("make", make);
        body.set("model", model);
        body.set("year", year);
        body.set("color", color);
        body.set("odometer", odometer);
        body.set("status", status);
        body.set("vtname", vtname);
        body.set("location", location);
        body.set("city", city);
    }
    // only other option is view
    return body;
}

function mVehicleTypeBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "add") {
        let vtname = $("#pk-input").val();
        let features = $("#vt-features").val();
        let wrate = $("#vt-wrate").val();
        let drate = $("#vt-drate").val();
        let hrate = $("#vt-hrate").val();
        let wirate = $("#vt-wirate").val();
        let dirate = $("#vt-dirate").val();
        let hirate = $("#vt-hirate").val();
        let krate = $("#vt-krate").val();
        body.set("vtname", vtname);
        body.set("features", features);
        body.set("wrate", wrate);
        body.set("drate", drate);
        body.set("hrate", hrate);
        body.set("wirate", wirate);
        body.set("dirate", dirate);
        body.set("hirate", hirate);
        body.set("krate", krate);
    }
    if (mType === "remove") {
        let vtname = $("#pk-input").val();
        body.set("vtname", vtname);
    }
    else if (mType === "update") {
        let vtname = $("#pk-input").val();
        let features = $("#vt-features").val();
        let wrate = $("#vt-wrate").val();
        let drate = $("#vt-drate").val();
        let hrate = $("#vt-hrate").val();
        let wirate = $("#vt-wirate").val();
        let dirate = $("#vt-dirate").val();
        let hirate = $("#vt-hirate").val();
        let krate = $("#vt-krate").val();
        body.set("vtname", vtname);
        body.set("features", features);
        body.set("wrate", wrate);
        body.set("drate", drate);
        body.set("hrate", hrate);
        body.set("wirate", wirate);
        body.set("dirate", dirate);
        body.set("hirate", hirate);
        body.set("krate", krate);
    }
    // only other option is view
    return body;
}

function mCustomerBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "add") {
        let phone = $("#pk-input").val();
        let name = $("#c-name").val();
        let address = $("#c-address").val();
        let driverLicense = $("#c-driver-license").val();
        body.set("phone", phone);
        body.set("name", name);
        body.set("address", address);
        body.set("driverLicense", driverLicense);
    }
    if (mType === "remove") {
        let phone = $("#pk-input").val();
        body.set("phone", phone);
    }
    else if (mType === "update") {
        let phone = $("#pk-input").val();
        let name = $("#c-name").val();
        let address = $("#c-address").val();
        let driverLicense = $("#c-driver-license").val();
        body.set("phone", phone);
        body.set("name", name);
        body.set("address", address);
        body.set("driverLicense", driverLicense);
    }
    // only other option is view
    return body;
}

function mBranchBody(mType) {
    let body = new FormData();
    body.set("mType", mType);
    if (mType === "add" || mType === "remove") {
        let city = $("#b-city").val();
        let location = $("#b-location").val();
        body.set("city", city);
        body.set("location", location);
    }
    // only other option is view
    return body;
}