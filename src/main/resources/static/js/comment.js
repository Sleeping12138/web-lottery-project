$(document).ajaxSend(function (event, xhr, options) {
    xhr.setRequestHeader("user_token", localStorage.getItem("user_token"));
});

$(document).ajaxError(function (event, xhr, options, exc) {
    if (xhr.status == 401) {
        localStorage.removeItem('user_identity');
        localStorage.removeItem('user_token');
        location.href = "login.html";
    }
})