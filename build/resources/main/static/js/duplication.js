$(function () {
    //idck 버튼을 클릭했을 때
    $("#idck").click(function () {

        //userid 를 param.
        let user_id = $("#user_id").val();

        $.ajax({
            async: true,
            type: 'POST',
            data: user_id,
            url: "/idcheck",
            dataType: "json",
            contentType: "application/json; charset=UTF-8",
            success: function (data) {
                if (data.cnt > 0) {
                    alert("아이디가 존재합니다. 다른 아이디를 입력해주세요.");
                    $("#user_id").focus();
                } else {
                    alert("사용가능한 아이디입니다.");
                    $("#user_pw").focus();
                    $("#commit").css("display", "inline-block");
                }
            },
            error: function () {
                alert("아이디를 입력 후 중복확인 버튼을 눌러주세요.");
            }
        });
    });

});

$(function () {
    $("#user_id").on("keyup", function() {
        $("#commit").css("display", "none");
    })
})