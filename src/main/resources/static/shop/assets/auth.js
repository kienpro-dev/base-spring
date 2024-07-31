var baseUrl = new URL(window.location.href).origin;

var registerUsername = document.getElementById('register_username');
var registerEmail = document.getElementById('register_email');
var registerPassword = document.getElementById('register_password');
var formRegister = document.getElementById('register');

var loginUsername = document.getElementById('login_username');
var loginPassword = document.getElementById('login_password');;
var loginForm = document.getElementById('signin');

var changePassword = document.getElementById('change_password');
var changeRetypePassword = document.getElementById('change_retype_password');
var changeRetypePassword2 = document.getElementById('change_retype_password2');
var changeForm = document.getElementById('change');

var forgotEmail = document.getElementById('forgot_email');
var forgotForm = document.getElementById('forgot');

function registerSubmit() {
	if (!registerUsername.value || !registerEmail.value || !registerPassword.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	}
	axios.post(baseUrl + '/molla/auth/register', {
		username: registerUsername.value,
		email: registerEmail.value,
		password: registerPassword.value,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đăng ký',
				text: response.data,
				icon: 'success',
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'Xác nhận'
			}).then((result) => {
				if (result.isConfirmed) {
					location.reload(true);
				}
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data,
			})
		});
}

formRegister.addEventListener('submit', (e) => {
	e.preventDefault();
	registerSubmit();
});

function loginSubmit() {
	if (!loginUsername.value || !loginPassword.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	}
	axios.post(baseUrl + '/molla/auth/login', {
		username: loginUsername.value,
		password: loginPassword.value,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đăng nhập',
				text: response.data,
				icon: 'success',
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'Xác nhận'
			}).then((result) => {
				if (result.isConfirmed) {
					location.reload(true);
				}
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data,
			})
		});
}

loginForm.addEventListener('submit', (e) => {
	e.preventDefault();
	loginSubmit();
});

function logout() {
	Swal.fire({
		title: 'Đăng xuất?',
		text: "Bạn có muốn đăng xuất!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Xác nhận!'
	}).then((result) => {
		if (result.isConfirmed) {
			axios.get(baseUrl + '/molla/auth/logout')
				.then(function(response) {
					Swal.fire({
						title: 'Thông báo',
						text: 'Đã đăng xuất thành công',
						icon: 'success',
						confirmButtonColor: '#3085d6',
						confirmButtonText: 'Xác nhận'
					}).then((result) => {
						if (result.isConfirmed) {
							location.reload(true);
						}
					})
				})
		}
	})
}

function changeSubmit() {
	if (!changePassword.value || !changeRetypePassword.value || !changeRetypePassword2.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	} else if (changeRetypePassword.value !== changeRetypePassword2.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Mật khẩu không khớp",
		})
		return;
	}
	axios.post(baseUrl + '/molla/auth/change-password', {
		password: changePassword.value,
		retype_password: changeRetypePassword.value,
		retype_password2: changeRetypePassword2.value,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đổi mật khẩu',
				text: response.data,
				icon: 'success',
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'Xác nhận'
			}).then((result) => {
				if (result.isConfirmed) {
					location.reload(true);
				}
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data,
			})
		});
}
changeForm.addEventListener('submit', (e) => {
	e.preventDefault();
	changeSubmit();
});

function forgotSubmit() {
	if (!forgotEmail.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	}
	axios.post(baseUrl + '/molla/auth/forgot-password', {
		email: forgotEmail.value,
		url: baseUrl
	})
		.then(function(response) {
			Swal.fire({
				title: 'Đã gửi yêu cầu đổi mật khẩu',
				text: response.data,
				icon: 'success',
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'Xác nhận'
			}).then((result) => {
				if (result.isConfirmed) {
					location.reload(true);
				}
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data,
			})
		});
}
forgotForm.addEventListener('submit', (e) => {
	e.preventDefault();
	forgotSubmit();
});