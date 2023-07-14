
function makeToast(isSuccessful, message, downloadFunction){
    var toastHeading = document.getElementById('toast-heading');
    var toastMessage = document.getElementById('toast-message');
    var toastBody = document.getElementById("toast-body");

    $("#download-errors").show();
    if(downloadFunction===null){
        $("#download-errors").hide();
    }

    toastBody.style.display = '';
    if(message==null || message.trim()===""){
        toastBody.style.display = 'none';
    }

    if(isSuccessful){
        toastHeading.innerHTML = "Success";
        toastHeading.style.color = 'green';
        toastMessage.innerHTML = null;

    }else{
        toastHeading.innerHTML = "Error";
        toastHeading.style.color = 'red';
        toastMessage.innerHTML = message;
    }

    var options = {
        animation : true,
        delay : 3000
    };

    var toastHTMLElement = document.getElementById("toast");

    var toastElement = new bootstrap.Toast(toastHTMLElement, options)

    toastElement.show();
}
function convertDate(date){
const utcTimestamp = new Date(date);
const options = {
  timeZone: "Asia/Kolkata",
  day: "2-digit",
  month: "2-digit",
  year: "numeric",
  hour: "2-digit",
  minute: "2-digit"
};

const istDateString = utcTimestamp.toLocaleString("en-IN", options);
console.log(istDateString);
return istDateString;
}










