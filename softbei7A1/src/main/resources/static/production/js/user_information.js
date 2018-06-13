/**
 * Created by 郭欣光 on 2018/6/11.
 */

function getUserInformation() {
    $.ajax({
        type: "GET",
        url: "/admin_user/get_information",
        cache: false,//设置不缓存
        dataType: "json",
        success: getUserInformationSuccess,
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

function getUserInformationSuccess(data) {
    if (data["status"] == "success") {
        var adminUser = data["admin_user"];
        $("#admin-user-name1").html(adminUser["id"]);
        $("#admin-user-name2").html(adminUser["id"]);
        $("#admin-user-reset-password").html("<a onclick=\"resetPasswordHref('" + adminUser["id"] + "');return false;\"> 修改密码</a>");
    } else {
        window.location.href = "/production/login.html";
    }
}

function resetPasswordHref(adminUserId) {
    $("#admin-user-id").val(adminUserId);
    $("#resetPasswordFormHref").click();
}

function resetPassword() {
    var adminUserId = $("#admin-user-id").val();
    var oldPassword = $("#old-password").val();
    var newPassword = $("#new-password").val();
    var repeatPassword = $("#repeat-password").val();

    if (oldPassword == null || oldPassword == "") {
        alert("原密码不能为空！");
    } else {
        if (newPassword == null || newPassword == "") {
            alert("新密码不能为空！");
        } else {
            if (newPassword != repeatPassword) {
                alert("两次密码不一致！");
            } else {
                var obj = new Object();
                obj.id = adminUserId;
                obj.password = oldPassword;
                $.ajax({
                    type: "POST",
                    url: "/admin_user/login",
                    cache: false,//设置不缓存
                    data: obj,
                    dataType: "json",
                    success: function (data) {
                        if (data["status"] == "success") {
                            var obj1 = new Object();
                            obj1.password = newPassword;
                            $.ajax({
                                type: "POST",
                                url: "/admin_user/reset_password",
                                cache: false,//设置不缓存
                                data: obj1,
                                dataType: "json",
                                success: resetPasswordSuccess,
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
                        } else {
                            alert(data["message"]);
                        }
                     },
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
}

function resetPasswordSuccess(data) {
    if (data["status"] == "success") {
        alert("修改成功！");
        window.location.href = "/production/login.html";
    } else {
        if (data["message"] == "用户未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(data["message"]);
        }
    }
}

function layOut() {
    $.ajax({
        type: "POST",
        url: "/admin_user/lay_out",
        cache: false,//设置不缓存
        dataType: "json",
        success: layOutSuccess,
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

function layOutSuccess(data) {
    window.location.href = "/production/login.html";
}

// $(document).ready(function () {
//     getUserInformation();
// });
