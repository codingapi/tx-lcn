var init = function () {

    http.get('/admin/setting', '加载中', function (res) {
        for (var key in res) {
            var value = res[key];
            $("#" + key).text(value);
        }
    });

    http.get('/admin/json', '加载中', function (res) {
        $("#redisState").text("正常");
    }, function (res) {
        $("#redisState").text("异常");
    });

    http.get('/admin/hasCompensate', '加载中', function (res) {
        if (res) {
            $("#compensate").text("有");
        } else {
            $("#compensate").text("无");
        }
    }, function (res) {
        $("#compensate").text("异常");
    });

}

init();

