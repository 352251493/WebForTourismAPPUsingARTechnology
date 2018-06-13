/**
 * Created by 郭欣光 on 2018/6/12.
 */

function getScenicAreaInformation() {
    $.ajax({
        type: "GET",
        url: "/scenic_area/get_all",
        cache: false,//设置不缓存
        dataType: "json",
        success: getScenicAreaInformationSuccess,
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

function getScenicAreaInformationSuccess(data) {
    if (data["status"] == "success") {
        var random = Math.random();
        var str = "";
        str += '<thead>';
        str += '<tr>';
        str += '<th>景点ID</th>';
        str += '<th>所在城市ID</th>';
        str += '<th>景点图片</th>';
        str += '<th>景点AR文件</th>';
        str += '<th>景点简介</th>';
        str += '<th>操作</th>';
        str += '</tr>';
        str += '</thead>';
        str += '<tbody>';
        var scenicAreaList = data["scenic_area_list"];
        for (var i = 0; i < scenicAreaList.length; i++) {
            var scenicArea = scenicAreaList[i];
            str += '<tr>';
            str += '<th scope="row">' + scenicArea["sa_id"] + '</th>';
            str += '<td>' + scenicArea["city_id"] + '</td>';
            str += '<td><a href="' + scenicArea["sa_img"] + '?random=' + random + '" target="_blank">' + '查看' + '</a></td>';
            str += '<td><a href="' + scenicArea["sa_ar"] + '?random=' + random + '" target="_blank">' + '查看' + '</a></td>';
            str += '<td id="scenic_area_intro_id_' + scenicArea["sa_id"] + '" style="word-break: break-all; word-wrap: break-word;">' + scenicArea["sa_intro"] + '</td>';
            str += '<td>';
            str += "<button type=\"button\" class=\"btn btn-primary\" onclick=\"editScenicAreaHref('" + scenicArea["sa_id"] + "');\">修改</button>&nbsp;&nbsp;";
            str += "<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteScenicArea('" + scenicArea["sa_id"] + "');\">删除</button>";
            str += '</td>';
            str += '</tr>';
        }
        str += '</tbody>';
        $("#scenic-information").html(str);
    } else {
        if (data["message"] == "没有景点信息！") {
            $("#scenic-information").html("暂时没有景点信息！");
        } else {
            alert(data["message"]);
        }
    }
}

function getAllCityInformation() {
    $.ajax({
        type: "GET",
        url: "/city/get_all",
        cache: false,//设置不缓存
        dataType: "json",
        success: getAllCityInformationSuccess,
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

function getAllCityInformationSuccess(data) {
    if (data["status"] == "success") {
        var cityList = data["city_list"];
        var str = "";
        for (var i = 0; i < cityList.length; i++) {
            var city = cityList[i];
            str += '<option value="' + city["city_id"] + '">' + city["city_name"] + '</option>';
        }
        $("#add-scenic-area-city-list").html(str);
    } else {
        $("#add-scenic-area-city-list").html("<option value='' disabled='disabled'>无</option>");
    }
    $("#addScenicAreaFormHref").click();
}

function addScenicAreaHref() {
    getAllCityInformation();
}

function addScenicArea() {
    var formData = new FormData($('#addScenicAreaForm1')[0]);
    $.ajax({
        type: "POST",
        url: "/scenic_area/admin/add_scenic_area",
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        data: formData,
        dataType: "json",
        contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
        processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
        success: addScenicAreaSuccess,
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

function addScenicAreaSuccess(data) {
    if (data["status"] == "success") {
        alert("添加成功！");
        window.location.href = window.location.href;
    } else {
        if (data["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(data["message"]);
        }
    }
}

function editScenicAreaHref(scenicAreaId) {
    $("#edit-scenic-area-sa-id").val(scenicAreaId);
    $("#editScenicAreaHref").click();
}

function editScenicAreaImgHref() {
    $("#editScenicAreaClose").click();
    $("#editScenicAreaImgHref").click();
}

function editScenicAreaImg() {
    var saId = $("#edit-scenic-area-sa-id").val();
    var formData = new FormData($('#editScenicAreaImg1')[0]);
    $.ajax({
        type: "POST",
        url: "/scenic_area/admin/update/sa_img/sa_id/" + saId,
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        data: formData,
        dataType: "json",
        contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
        processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
        success: editScenicAreaImgSuccess,
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

function editScenicAreaImgSuccess(data) {
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

function editAcenicAreaArHref() {
    $("#editScenicAreaClose").click();
    $("#editScenicAreaArHref").click();
}

function editScenicAreaAr() {
    var saId = $("#edit-scenic-area-sa-id").val();
    var formData = new FormData($('#editScenicAreaAr1')[0]);
    $.ajax({
        type: "POST",
        url: "/scenic_area/admin/update/sa_ar/sa_id/" + saId,
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        data: formData,
        dataType: "json",
        contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
        processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
        success: editScenicAreaArSuccess,
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

function editScenicAreaArSuccess(data) {
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

function editScenicAreaIntroHref() {
    $("#editScenicAreaClose").click();
    var saId = $("#edit-scenic-area-sa-id").val();
    var scenicAreaIntro = $("#scenic_area_intro_id_" + saId).html();
    $("#edit-scenic-area-intro").val(scenicAreaIntro);
    $("#editScenicAreaIntroHref").click();
}

function editScenicAreaIntro() {
    var saId = $("#edit-scenic-area-sa-id").val();
    var saIntro = $("#edit-scenic-area-intro").val();
    var obj = new Object();
    obj.sa_intro = saIntro;
    $.ajax({
        type: "POST",
        url: "/scenic_area/admin/update/sa_intro/sa_id/" + saId,
        cache: false,//设置不缓存
        dataType: "json",
        data: obj,
        success: editScenicAreaIntroSuccess,
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

function editScenicAreaIntroSuccess(data) {
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

function deleteScenicArea(scenicAreaId) {
    if (confirm("确定删除该景点？用户基于该景点的旅行日计划也将被删除！")) {
        $.ajax({
            type: "POST",
            url: "/scenic_area/admin/delete/sa_id/" + scenicAreaId,
            cache: false,//设置不缓存
            dataType: "json",
            success: deleteScenicAreaSuccess,
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

function deleteScenicAreaSuccess(data) {
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
    getScenicAreaInformation();
});