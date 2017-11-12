var http;
if (!http)
    http = {};


/**
 * post提交事件
 * @param url   地址
 * @param params    参数
 * @param msg   提示信息
 * @param success   成功回调
 */
http.post = function (url, params, msg, success, error) {
    if (msg == null)
        msg = '加载中...';
    $.blockUI(
        {
            message: msg,
            baseZ: 2000,
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: 'white',
                width: "300px",
                opacity: .5,
                color: 'black'
            }
        });
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        contentType: "application/json",
        success: function (data) {
            $.unblockUI();
            if (success != null) {
                success(data);
            }
        }, error: function (data) {
            $.unblockUI();
            if (error != null) {
                error(data);
            }
        }
    });
}


http.get = function (url, msg, success, error) {
    if (msg == null)
        msg = '加载中...';
    $.blockUI(
        {
            message: msg,
            baseZ: 2000,
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: 'white',
                width: "300px",
                opacity: .5,
                color: 'black'
            }
        });
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json",
        success: function (data) {
            $.unblockUI();
            if (success != null) {
                success(data);
            }
        },
        error: function (data) {
            $.unblockUI();
            if (error != null) {
                error(data);
            }
        }
    });
}