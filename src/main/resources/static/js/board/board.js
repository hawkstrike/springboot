
$(function () {
	
	// 상세보기 click event
	/*$(document).on('click', '.board_detail', function () {
		const id = $(this).data('id').trim();
		
	});*/
	
	// 글 등록 button click event
	$(document).on('click', '.btn_board_save', async () => {
		$(this).addClass('disabled');
		const type = $('.board_detail_area').data('type');
		const id = (type === 'r') ? $('.board_id_area').data('id') : -1;
		const title = $('.board_title').val().trim();
		const userName = $('.board_user_name').val().trim();
		const content = $('.board_content').val().trim();
		
		if (title === '') {
			alert('제목을 입력해주세요!');
			// $('.board_title').on('focus');
			
			return;
		}
		
		if (userName === '') {
			alert('작성자를 입력해주세요!');
			
			return;
		}
		
		if (content === '') {
			alert('내용을 입력해주세요!');
			
			return;
		}
		
		const bodyData = new URLSearchParams();
		
		bodyData.append('id', id);
		bodyData.append('title', title);
		bodyData.append('userName', userName);
		bodyData.append('content', content);
		
		const save = async (bodyData) => {
			const url = location.origin + '/board/save';
			const init = {
				method: 'post'
				, body: bodyData
			};
			
			const response = await fetch(url, init);
			
			return (response.ok) ? response.json() : null;
		};
		
		const result = save(bodyData);
		
		if (result !== null) {
			const responseId = await result.then((data) => {
				return data.id;
			});
			
			if (responseId !== -1) {
				if (type === 'i') {
					$('.btn_board_list').get(0).click();
				} else {
					location.reload();
				}
			}
		}
	});
	
});