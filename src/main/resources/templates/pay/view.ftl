<html>
<head><title>订单详情</title></head>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
    <title>卖家后端管理系统</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.bootcss.com/weui/1.1.2/style/weui.min.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.0/css/jquery-weui.min.css">
    <link rel="stylesheet" href="/sell/css/style.css">
    <link rel="stylesheet" href="/sell/css/my-style.css">
</head>
<body id="main-style">

<div class="weui-loadmore weui-loadmore_line">
    <span class="weui-loadmore__tips">${orderDTO.groupNo}</span>
</div>

<div class="weui-cells__title" style="word-wrap: break-word">
    订单编号:${orderDTO.orderId}
    <p>创建时间:${orderDTO.updateTime} </p>
</div>

<div class="weui-cells" id="list-items">
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <p style="color: darkseagreen"></p>
        </div>
        <div class="weui-cell__ft" style="color: darksalmon">${orderDTO.getOrderStatusEnum().message}</div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__bd">
            <p style="color: darkseagreen"></p>
        </div>
        <div class="weui-cell__ft" style="color: darksalmon">${orderDTO.getPayStatusEnum().message}</div>
    </div>
</div>

<#--等待支付 且为新订单时显示-->
<#if orderDTO.getOrderStatusEnum().message == "新订单">
    <#if orderDTO.getPayStatusEnum().message == "等待支付">
    <div class="weui-cell">
        <div class="weui-cell__bd">
        </div>
        <div class="weui-cell__ft" id="to-pay" onclick="show_actions({orderId: '${orderDTO.orderId}'})">
            <button class="weui-btn weui-btn_mini weui-btn_warn">
                去支付
            </button>
        </div>
    </div>
    </#if>
</#if>

<#--订单列表-->
<div class="weui-cells__title">付款合计: ￥${orderDTO.orderAmount}</div>
<div class="weui-cells" id="list-items">
    <#list orderDTO.orderDetailList as orderDetail>
        <div class="weui-cell">
            <div class="weui-cell__hd">
                <img src="${orderDetail.productIcon}" alt="" style="width:64px;margin-right:5px;display:block">
            </div>
            <div class="weui-cell__bd">
                <h5 class="weui-media-box__title">${orderDetail.productName}</h5>
                <p class="weui-media-box__desc">￥${orderDetail.productPrice}</p>
            </div>
            <div class="weui-cell__ft">
                ×${orderDetail.productQuantity}
            </div>
        </div>
    </#list>
</div>

<#--底部链接-->
<div class='demos-content-padded'>
    <div class="weui-footer" style="margin-top:64px">
        <p class="weui-footer__links">
        <div class="weui-footer__link" style="color: burlywood">
        <a href="http://yzsell.s1.natapp.cc/#/authorize" class="weui-footer__link">奕姿科技</a>
        </div>
        </p>
        <p class="weui-footer__text">Copyright © 2018 </p>
    </div>
</div>
<div class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
    <span class="weui-loadmore__tips"></span>
</div>

<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.0/js/jquery-weui.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.0/js/swiper.min.js"></script>

<script>
    function show_actions(param) {
        var payUrl = "http://wxsell.nat200.top/sell/pay/create?orderId=" + param.orderId +
                "&returnUrl=http://wxsell.nat200.top/sell/pay/view?orderId="+param.orderId;
        $.confirm(param.orderId, "确认支付?", function () {
            window.location.href = payUrl;
        }, function () {
            $.toast("取消支付!", 'cancel');
        });
    }
</script>

</body>
</html>

