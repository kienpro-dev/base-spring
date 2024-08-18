function readImageUser(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#change__info__avatar').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
	}
}

 function setType(actionType) {
        document.getElementById('type').value = actionType;
    }

    document.getElementById('balanceForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Ngăn chặn hành động gửi form mặc định

        const form = event.target;
        const formData = new FormData(form);
        const xhr = new XMLHttpRequest();
        xhr.open(form.method, form.action, true);
        xhr.setRequestHeader('Accept', 'application/json');
        xhr.onload = function() {
            const response = JSON.parse(xhr.responseText);

            if (xhr.status === 200) {
                Swal.fire({
                    icon: 'success',
                    title: response.success,
                    timer: 700,
                    timerProgressBar: true,
                    didClose: () => {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: response.error,
                    timer: 700,
                    timerProgressBar: true
                });
            }
        };
        xhr.send(formData);
    });