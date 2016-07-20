/**
 * 组件需要使用
 * id : year month date的select组件
 */
var birthday = new Birthday();
function Birthday(){
	var date =  new Date();
	var thisYear = date.getFullYear();
	var year = "";
	var mon = "";
	var day = "";
	this.init = function(){
		year = $("#year");
		for(var i=thisYear; i>=thisYear-50; i-- ){
			year.append("<option value="+i+">"+i+"</option>");
		}
		mon = $("#month");
		for(var i=1;i<13;i++){
			if(i<10) i="0"+i;
			mon.append("<option value="+i+">"+i+"</option>");
		}
		//在月份上绑定change事件
		mon.bind("change",birthday.monthChangeEvent);
		//在年份上绑定change事件
		year.bind("change",birthday.yearChangeEvent);
		day = $("#date");
		
		for(var i=1;i<31;i++){
			if(i<10){
				i="0"+i;
			}
			day.append("<option value="+i+">"+i+"</option>");
		}
	};
	
	this.monthChangeEvent = function(){
		day.empty();
	  if($(this).val()=="04" || $(this).val()=="06" ||$(this).val()=="09" || $(this).val()=="11"){
	  	for(var i=1;i<31;i++){
			if(i<10) i="0"+i;
			day.append("<option value="+i+">"+i+"</option>");
		}
	  }
	  else if($(this).val()=="02"){
	  	for(var i=1;i<29;i++){
			if(i<10) i="0"+i;
			day.append("<option value="+i+">"+i+"</option>");
		}
		if(birthday.isLeapYear(year.val())){
			day.append("<option value=29>29</option>");
		}
	  }else{
	  	for(var i=1;i<32;i++){
			if(i<10) i="0"+i;
			day.append("<option value="+i+">"+i+"</option>");
		}
	  }
	};
	
	this.yearChangeEvent = function(){
	  var monVal = $("#month").val();
	  if(monVal=="02"){
	  	day.empty();
	  	for(var i=1;i<29;i++){
			if(i<10) i="0"+i;
			day.append("<option value="+i+">"+i+"</option>");
		}
		if(birthday.isLeapYear(year.val())){
			day.append("<option value=29>29</option>");
		}
	  }
	};
	
	//是不是闰年
	this.isLeapYear = function(year){
		return (0==year%4&&((year%100!=0)||(year%400==0))); 
	}
}

function showBirthday(data_year,data_month,data_date){
	//判断年份
	  $("#year option").each(function () {
	 	if( $(this).val() == (data_year<10?"0"+data_year:""+data_year)){
	 		$(this).attr("selected",true);
	 		return;
	 	} 
	 }); 
	 //判断月份
	  $("#month option").each(function () {
	 	if( $(this).val() == (data_month<10?"0"+data_month:""+data_month)){
	 		$(this).attr("selected",true);
	 		return;
	 	} 
	 }); 
	 //判断天
	 $("#date option").each(function () {
	 	if( $(this).val() == (data_date<10?"0"+data_date:""+data_date)){
	 		$(this).attr("selected",true);
	 		return;
	 	} 
	 });
}