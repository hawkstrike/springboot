
$(function () {
	
	$(document).on('click', '.btn_delete_test', function () {
		var arr = [1, 2, 3, 4];
		var ajaxData = {id: arr};
		
		$.ajax({
			url: '/board/delete'
			, type: 'post'
			, data: ajaxData
			, dataType: 'json'
			, success: function (data) {
				console.log('data :', data);
			}
			, error: function (e) {
				console.error(e);
			}
		})
	});
});