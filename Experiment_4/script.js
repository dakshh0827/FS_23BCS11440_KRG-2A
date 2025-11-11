const result = document.getElementById("result");

function update(input) {
    if(result.value == '0') {
        result.value = "";
    }
    result.value += input;
}

function cleared() {
    result.value = "0";
}

function equals() {
    try {
        result.value = eval(result.value);
    }
    catch {
        result.value = "Error";
    }
}