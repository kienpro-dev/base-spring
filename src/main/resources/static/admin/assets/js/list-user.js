var baseUrl = new URL(window.location.href).origin;

function deleteUser(id, name) {
	Swal.fire({
		title: 'Bạn có muốn xoá?',
		text: "Xoá người dùng " + name + " sẽ không khôi phục được!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Xác nhận!'
	}).then((result) => {
		if (result.isConfirmed) {
			axios({
				method: 'DELETE',
				url: baseUrl + "/admin/delete/" + id,
				responseType: 'stream'
			})
				.then(function(response) {
					Swal.fire(
						'Đã xoá!',
						'Đã xoá người dùng thành công.',
						'success'
					)
					location.reload(true);
				});
		}
	})
}

var bodyViewUser = document.getElementById('bodyViewUser');

function viewUser(id) {
	axios({
		method: 'GET',
		contentType: "application/json",
		url: baseUrl + "/admin/view/" + id
	})
		.then(function(response) {
            var phoneNumber = response.data.phoneNumber || 'Chưa cập nhật ...';
            var dateOfBirth = response.data.dateOfBirth || 'Chưa cập nhật ...';
            var nationalId = response.data.nationalId || 'Chưa cập nhật ...';
            var drivingLicense = response.data.drivingLicense || 'Chưa cập nhật ...';
            var address = response.data.address || 'Chưa cập nhật ...';
            var role ;
            if(response.data.role.name==='ROLE_ADMIN'){
                role='Admin';
            }
            else if (response.data.role.name==='ROLE_USER'){
                role='Người dùng';
            }
            else{
                role="Chủ xe";
            }


			var wrapper = document.createElement('div');
			wrapper.innerHTML = ``;
			bodyViewUser.innerHTML = ``;
			wrapper.innerHTML = `
			<div class="row">
							<div class="col-sm-12 col-lg-12">
								<table class="table table-borderless">
									<tbody>
										<tr>
											<th>Id</th>
											<td id="viewUserId">`+ response.data.id + `</td>
										</tr>
										<tr>
											<th>Họ tên</th>
											<td id="viewUserFullname">`+ response.data.name + `</td>
										</tr>
										<tr>
											<th>Email</th>
											<td id="viewUserEmail">`+ response.data.email + `</td>
										</tr>
										<tr>
											<th>Số điện thoại</th>
											<td id="viewPhoneNumber">${phoneNumber}</td>
										</tr>

										<tr>
											<th>Ngày sinh</th>
											<td id="viewUserDateOfBirth">${dateOfBirth}</td>
										</tr>
										<tr>
                                        	<th>Quốc gia</th>
                                        	<td id="viewUserNationalId">${nationalId}</td>
                                        </tr>
										<tr>
											<th>Bằng lái</th>
											<td id="viewUserDrivingLicense">${drivingLicense}</td>
										</tr>
										<tr>
											<th>Địa chỉ</th>
											<td id="viewUserAddress">${address}</td>
										</tr>

										<tr>
											<th>Chức vụ</th>
											<td id="viewUserRole">${role}</td>
										</tr>
										<tr>
											<th>Ngày tạo</th>
											<td id="viewUserCreactDay">`+ response.data.createdDate + `</td>
										</tr>
										<tr>
											<th>Cập nhật lần cuối</th>
											<td id="viewUserLastUpdate">`+ response.data.lastModifiedDate + `</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
			`;
			bodyViewUser.append(wrapper);
		});
}