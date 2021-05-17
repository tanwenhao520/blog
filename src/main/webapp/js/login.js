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