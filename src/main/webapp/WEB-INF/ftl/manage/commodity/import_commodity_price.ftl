<script type="text/javascript" src="/static/js/jquery.form.js"></script>


	<div class="contentMain" style="border:none;">
        <div class="wms" style="border:none;">
            <div class="blank15"></div>
            <div class="wms-div" style="border:none;">
				<form action="/commodity/importPrice.sc" name="excelForm" id="excelForm" method="post" enctype="multipart/form-data">
			      <div class="yt-c-top">
			        <p>
			          <strong>导入区域：</strong>注意：导入的excel大小不能大于100K,建议一个excel商品数量不大于500个&nbsp;&nbsp;<input type="button" class="btn btn-sm btn-yougou" value="下载模板" onclick="location.href='${BasePath}/template/commodity_price_templet.xls'"  />
			        </p>
			        <p>
			          <input type="file" style="display:hidden;" name="excelBatchFile" id="excelBatchFile" value="浏览"><input type="submit" value="导入" class="btn btn-sm btn-yougou"  />
			        </p>
			      </div>
			    </form>
				<font id="exportMsg" color="red"></span>
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	 $('#excelForm').ajaxForm({
	   dataType:'json',
       success : function(data) {
       		$("#exportMsg").html(data.msg);
 	    }
	 });
 });
</script>
