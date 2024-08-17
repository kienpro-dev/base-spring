var baseUrl = new URL(window.location.href).origin;

var registerName = document.getElementById('register_name');
var registerEmail = document.getElementById('register_email');
var registerPhone = document.getElementById('register_phone');
var registerPassword = document.getElementById('register_password');
var registerRetypePassword = document.getElementById('register_retype_password');
var roles = document.getElementsByName('role');
var formRegister = document.getElementById('register');

var loginEmail = document.getElementById('login_email');
var loginPassword = document.getElementById('login_password');
var loginForm = document.getElementById('signin');

var changePassword = document.getElementById('change_password');
var changeRetypePassword = document.getElementById('change_retype_password');
var changeRetypePassword2 = document.getElementById('change_retype_password2');
var changeForm = document.getElementById('change');

var forgotEmail = document.getElementById('forgot_email');
var forgotForm = document.getElementById('forgot');

function registerSubmit() {
	if (!registerName.value || !registerEmail.value || !registerPassword.value || !registerPhone.value || !registerRetypePassword.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	}
	let roleCheck = 'ROLE_USER';
	for(const role of roles) {
		if(role.checked) {
			roleCheck = role.value;
			break;
		}
	}
	axios.post(baseUrl + '/car/auth/register', {
		name: registerName.value,
		email: registerEmail.value,
		phoneNumber: registerPhone.value,
		password: registerPassword.value,
		repeatPassword: registerRetypePassword.value,
		role: roleCheck,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đăng ký',
				text: response.data.message,
				icon: 'success',
				showConfirmButton: false,
				timer: 700
			}).then(() => {
				location.reload(true);
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data.message,
			})
		});
}

formRegister.addEventListener('submit', (e) => {
	e.preventDefault();
	registerSubmit();
});

function loginSubmit() {
	if (!loginEmail.value || !loginPassword.value) {
		Swal.fire({
			icon: 'error',
			title: 'Thông báo lỗi',
			text: "Vui lòng nhập đầy đủ thông tin",
		})
		return;
	}
	axios.post(baseUrl + '/car/auth/login', {
		email: loginEmail.value,
		password: loginPassword.value,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đăng nhập',
				text: response.data.message,
				icon: 'success',
				showConfirmButton: false,
				timer: 700
			}).then(() => {
				location.reload(true);
			})
		})
		.catch(function(error) {
			Swal.fire({
				icon: 'error',
				title: 'Thông báo lỗi',
				text: error.response.data.message,
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
			axios.get(baseUrl + '/car/auth/logout')
				.then(function(response) {
					Swal.fire({
						title: 'Thông báo',
						text: 'Đã đăng xuất thành công',
						icon: 'success',
						showConfirmButton: false,
						timer: 700
					}).then(() => {
							window.location.href = baseUrl + '/car/home';
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
	axios.post(baseUrl + '/car/auth/change-password', {
		password: changePassword.value,
		newPassword: changeRetypePassword.value,
		repeatNewPassword: changeRetypePassword2.value,
	})
		.then(function(response) {
			Swal.fire({
				title: 'Thông báo đã đổi mật khẩu',
				text: response.data.message,
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
				text: error.response.data.message,
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
	axios.post(baseUrl + '/car/auth/forgot-password', {
		email: forgotEmail.value,
		url: baseUrl
	})
		.then(function(response) {
			Swal.fire({
				title: 'Đã gửi yêu cầu đổi mật khẩu',
				text: response.data.message,
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
				text: error.response.data.message,
			})
		});
}
forgotForm.addEventListener('submit', (e) => {
	e.preventDefault();
	forgotSubmit();
});