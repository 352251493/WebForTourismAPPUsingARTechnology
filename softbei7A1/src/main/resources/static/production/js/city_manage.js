/**
 * Created by 郭欣光 on 2018/6/12.
 */

function getCityInformation() {
    $.ajax({
        type: "GET",
        url: "/city/get_all",
        cache: false,//设置不缓存
        dataType: "json",
        success: getCityInformationSuccess,
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

function getCityInformationSuccess(data) {
    if (data["status"] == "success") {
        var cityList = data["city_list"];
        var str = "";
        str += '<thead>';
        str += '<tr>';
        str += '<th>城市ID</th>';
        str += '<th>城市名称</th>';
        str += '<th>操作</th>';
        str += '</tr>';
        str += '</thead>';
        str += '<tbody>';
        for (var i = 0; i < cityList.length; i++) {
            var city = cityList[i];
            str += '<tr>';
            str += '<th scope="row">' + city["city_id"] + '</th>';
            str += '<td>' + city["city_name"] + '</td>';
            str += '<td>';
            str += "<button type=\"button\" class=\"btn btn-primary\" onclick=\"editCityHref('" + city["city_id"] + "', '" + city["city_name"] + "')\">修改</button>&nbsp;&nbsp;";
            str += "<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteCity('" + city["city_id"] + "')\">删除</button>";
            str += '</td>';
            str += '</tr>';
        }
        str += '</tbody>';
        $("#city-information").html(str);
    } else {
        $("#city-information").html("暂时没有城市信息！");
    }
}

function addCityHref() {
    $("#addCityFormHref").click();
}

function addCity() {
    var cityName = $("#city-name").val();
    if (cityName == null || cityName == "") {
        alert("城市名称不能为空！");
    } else {
        if (cityName.length > 100) {
            alert("城市名称长度不能大于100！");
        } else {
            var obj = new Object();
            obj.city_name = cityName;
            $.ajax({
                type: "POST",
                url: "/city/admin/add_city",
                cache: false,//设置不缓存
                dataType: "json",
                data: obj,
                success: addCitySuccess,
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

function addCitySuccess(data) {
    if (data["status"] == "success") {
        alert("添加成功！");
        window.location.href = window.location.href;
    } else {
        if (city["message"] == "管理员未登录！") {
            window.location.href = "/production/login.html";
        } else {
            alert(city["message"]);
        }
    }
}

function editCityHref(cityId, cityName) {
    $("#edit-city-city-name").val(cityName);
    $("#edit-city-city-id").val(cityId);
    $("#editCityFormHref").click();
}

function editCity() {
    var cityId = $("#edit-city-city-id").val();
    var cityName = $("#edit-city-city-name").val();
    if (cityName == null || cityName == "") {
        alert("城市名称不能为空！");
    } else {
        if (cityName.length > 100) {
            alert("城市名称长度大于100！");
        } else {
            var obj = new Object();
            obj.city_name = cityName;
            obj.city_id = cityId;
            $.ajax({
                type: "POST",
                url: "/city/admin/update",
                cache: false,//设置不缓存
                dataType: "json",
                data: obj,
                success: editCitySuccess,
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

function editCitySuccess(data) {
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

function deleteCity(cityId) {
    if (confirm("确定删除该城市？用户基于该城市的旅行计划、明信片、以及基于该城市的景点信息都会被删除！")) {
        $.ajax({
            type: "POST",
            url: "/city/admin/delete/city_id/" + cityId,
            cache: false,//设置不缓存
            dataType: "json",
            success: deleteCitySuccess,
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

function deleteCitySuccess(data) {
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
    getCityInformation();
});