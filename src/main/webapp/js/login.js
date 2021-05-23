$(".send").on("click", function() {
			$(".receive > .newLabel").remove();
			$(".receive").append("<label class='block clearfix newLabel'><span class='block input-icon input-icon-right'>"+
			"<input type = 'code' class = 'form-control' placeholder = '验证码'/><i class = 'ace-icon fa fa-codepen'></i></span></label>")
			$(".receive").append("<label class='block clearfix newLabel'><span class='block input-icon input-icon-right'>"+
			"<input type = 'password'class ='form-control' placeholder = '密码'/><i class = 'ace-icon fa fa-lock'></i></span></label>")
			$(".receive").append("<label class='block clearfix newLabel'><span class='block input-icon input-icon-right'>"+
			"<input type = 'qpassword' class = 'form-control' placeholder = '确认密码''/><i class = 'ace-icon fa fa-lock''></i></span></label>")
			$(".receive").append("<div class='clearfix reset newLabel'><button type='button' class='width-35 pull-right btn btn-sm btn-danger'>"+
			"<i class='fa fa-lightbulb-o'></i><span class='bigger-110'>重置</span></button></div>")
			
			})
//登录按钮点击事件
$(".loginUser").on("click",function () {
	alert(1)
	var userName= $("#userName").val();
	var passWord = $("#passWord").val();
	$(".uMessage").html("");
	$(".pMessage").html("");
	if(userName!= "" && passWord!=""){
		function findUser() {
			$.ajax({
				type:"get",
				dataType: "json",
				//contentType: "application/json;charset=utf-8",
				data:{"userName":userName,"passWord":passWord},
				url: "http://localhost:8081/blog/login/loginUser",
				async: true,
				success: function success(data) {
					if (data.success == true) {
						alert(data.message);
						window.location.replace("http://localhost:8081/blog") ;
					}else{
						$(".eMessage").html(data.message);
					}
				},
				error: function error(data) {
					alert(data.message);
				}
			});
		}
		findUser();
	}else if (userName =="") {
		$(".uMessage").html("请输入用户名");
	}else if (passWord ==""){
		$(".pMessage").html("请输入密码");
	}
})

$(".loginUser").click(function(){

})

