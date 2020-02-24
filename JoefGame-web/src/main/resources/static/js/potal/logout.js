$(function () {
    $("#logout").click(function () {
        var url = "/logout";

        axios.post(url).then(function (result) {
            var jsonResult = result.data;
            if(jsonResult.success){
                window.location.href = "/";
            } else {
                alert(jsonResult.message);
            }
        })
    });
});