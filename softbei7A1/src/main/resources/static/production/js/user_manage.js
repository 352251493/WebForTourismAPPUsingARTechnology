/**
 * Created by 郭欣光 on 2018/6/13.
 */

function getAllUserInformation() {
    $.ajax({
        type: "GET",
        url: "/user/get_all",
        cache: false,//设置不缓存
        dataType: "json",
        success: getAllUserInformationSuccess,
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

function getAllUserInformationSuccess(data) {
    if (data["status"] == "success") {
        var str = "";
        var userList = data["user_list"];
        str += '<thead>';
        str += '<tr>';
        str += '<th>用户ID</th>';
        str += '<th>电话</th>';
        str += '<th>姓名</th>';
        str += '<th>性别</th>';
        str += '<th>生日</th>';
        str += '<th>头像</th>';
        str += '<th>操作</th>';
        str += '</tr>';
        str += '</thead>';
        str += '<tbody>';
        for (var i = 0; i < userList.length; i++) {
            var user = userList[i];
            var random = Math.random();
            str += '<tr>';
            str += '<th scope="row">' + user["user_id"] + '</th>';
            str += '<td id="phone_number_id_' + user["user_id"] + '">' + user["phone_number"] + '</td>';
            str += '<td id="name_id_' + user["user_id"] + '">' + user["name"] + '</td>';
            str += '<td id="gender_id_' + user["user_id"] + '">' + user["gender"] + '</td>';
            str += '<td id="birth_id_' + user["user_id"] + '">' + user["birth"] + '</td>';
            str += '<td><a href=\"' + user["head_portrait"] + '?random=' + random + '\" target="_blank">点击查看</a></td>';
            str += '<td>';
            str += "<button type=\"button\" class=\"btn btn-primary\" onclick=\"editUserHref('" + user["user_id"] + "');\">修改</button>&nbsp;&nbsp;";
            str += "<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteUser('" + user["user_id"] + "');\">删除</button>";
            str += '</td>';
            str += '</tr>';
        }
        str += '</tbody>';
        $("#user-information").html(str);
    } else {
        if (data["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            if (data["message"] == "没有用户信息！") {
                $("#user-information").html("暂时没有用户信息！");
            } else {
                alert(data["message"]);
            }
        }
    }
}

function addUserHref() {
    $("#addUserFormHref").click();
}

function addUser() {
    var phoneNumber = $("#add-user-phone-number").val();
    var password = $("#add-user-password").val();
    var repeatPassword = $(("#add-user-repeat-password")).val();
    var name = $("#add-user-name").val();
    var birth = $("#add-user-birth").val();
    if (phoneNumber == null || phoneNumber == "" || password == null || password == "" || repeatPassword == null || repeatPassword == "" || name == null || name == "" || birth == null || birth == "") {
        alert("所有字段不能为空！");
    } else {
        if (!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(phoneNumber))) {
            alert("不是完整的11位手机号或者正确的手机号前7位!");
        } else {
            if (password == repeatPassword) {
                if (name.length > 50) {
                    alert("姓名长度大于50字节！");
                } else {
                    if (birth.length > 15) {
                        alert("生日长度大于15字节！");
                    } else {
                        var formData = new FormData($('#addUserForm1')[0]);
                        $.ajax({
                            type: "POST",
                            url: "/user/register",
                            // async: false,//true表示同步，false表示异步
                            cache: false,//设置不缓存
                            data: formData,
                            dataType: "json",
                            contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
                            processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
                            success: addUserSuccess,
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
            } else {
                alert("两次密码不一致！");
            }
        }
    }
    return false;
}

function addUserSuccess(data) {
    if (data["status"] == "success") {
        alert("添加成功！");
        window.location.href = window.location.href;
    } else {
        alert(data["message"]);
    }
}

function editUserHref(userId) {
    $("#edit-user-user-id").val(userId);
    $("#editUserHref").click();
}

function editUserInformationHref() {
    $("#editUserClose").click();
    var userId = $("#edit-user-user-id").val();
    var phoneNumber = $("#phone_number_id_" + userId).html();
    var name = $("#name_id_" + userId).html();
    var gender = $("#gender_id_" + userId).html();
    var birth = $("#birth_id_" + userId).html();
    $("#edit-user-information-phone-number").val(phoneNumber);
    $("#edit-user-information-name").val(name);
    $("#edit-user-information-birth").val(birth);
    if (gender.indexOf("男") == 0) {
        $("#edit-user-information-gender-man").click();
    } else {
        $("#edit-user-information-gender-woman").click();
    }
    $("#editUserInformationHref").click();
}

function editUserInformation() {
    var userId = $("#edit-user-user-id").val();
    var phoneNumber = $("#edit-user-information-phone-number").val();
    var name = $("#edit-user-information-name").val();
    var gender;
    if ($("#edit-user-information-gender-man").is(":checked")) {
        gender = "男";
    } else {
        gender = "女";
    }
    var birth = $("#edit-user-information-birth").val();
    var obj = new Object();
    obj.phone_number = phoneNumber;
    obj.name = name;
    obj.gender = gender;
    obj.birth = birth;
    $.ajax({
        type: "POST",
        url: "/user/admin/update/user_information/user_id/" + userId,
        cache: false,//设置不缓存
        dataType: "json",
        data: obj,
        success: editUserInformationSuccess,
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
    return false;
}

function editUserInformationSuccess(data) {
    if (data["status"] == "success") {
        alert("修改成功！");
        window.location.href = window.location.href;
    } else {
        if (data["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(data["message"]);
        }
    }
}

function editUserHeadPortraitHref() {
    $("#editUserClose").click();
    $("#editUserHeadPortraitHref").click();
}

function editUserHeadPortrait() {
    var userId = $("#edit-user-user-id").val();
    var formData = new FormData($('#editUserHeadPortrait1')[0]);
    $.ajax({
        type: "POST",
        url: "/user/admin/update/head_portrait/user_id/" + userId,
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        data: formData,
        dataType: "json",
        contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
        processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
        success: editUserHeadPortraitSuccess,
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
    return false;
}

function editUserHeadPortraitSuccess(data) {
    if (data["status"] == "success") {
        alert("修改成功！");
        window.location.href = window.location.href;
    } else {
        if (data["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(data["message"]);
        }
    }
}


function deleteUser(userId) {
    if (confirm("确定删除该用户？")) {
        $.ajax({
            type: "POST",
            url: "/user/admin/delete/user_id/" + userId,
            cache: false,//设置不缓存
            dataType: "json",
            success: deleteUserSuccess,
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

function deleteUserSuccess(data) {
    if (data["status"] == "success") {
        alert("删除成功！");
        window.location.reload();
    } else {
        if (data["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(data["message"]);
        }
    }
}

$(document).ready(function () {
    getUserInformation();
    getAllUserInformation();
});