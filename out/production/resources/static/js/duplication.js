let idck = 0;
$(function() {
    //idck 버튼을 클릭했을 때
    $("#idck").click(function() {

        //userid 를 param.
        let userid =  $("#userid").val();
        console.log(userid);

        $.ajax({
            async: true,
            type : 'POST',
            data : userid,
            url : "/idcheck",
            dataType : "json",
            contentType: "application/json; charset=UTF-8",
            success : function(data) {
                if (data.cnt > 0) {

                    alert("아이디가 존재합니다. 다른 아이디를 입력해주세요.");
                    $("#userid").focus();


                } else {
                    alert("사용가능한 아이디입니다.");
                    $("#userpw").focus();
                    //아이디가 중복하지 않으면  idck = 1
                    idck = 1;

                }
            },
            error : function(error) {

                alert("error : " + error);
            }
        });
    });
});