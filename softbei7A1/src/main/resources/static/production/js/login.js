/**
 * Created by 郭欣光 on 2018/6/11.
 */

function login() {
    var id = $("#id").val();
    var password = $("#password").val();
    if (id == null || id == "") {
        alert("账户不能为空！");
    } else {
        if (password == null || password == "") {
            alert("密码不能为空！");
        } else {
            var obj = new Object();
            obj.id = id;
            obj.password = password;
            $.ajax({
                type: "POST",
                url: "/admin_user/login",
                cache: false,//设置不缓存
                data: obj,
                dataType: "json",
                success: loginSuccess,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
                        alert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                    } else {
                        if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
                            alert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                        } else {
                            if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
                                alert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                            } else {
                                alert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                            }
                        }
                    }
                }
            });
        }
    }
}

function loginSuccess(data) {
    if (data["status"] == "success") {
        window.location.href = "/production/city_manage.html";
    } else {
        alert(data["message"]);
    }
}
