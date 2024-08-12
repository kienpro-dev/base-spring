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
                method: 'PUT',
                url: baseUrl + "/admin/delete/" + id,
                responseType: 'stream'
            })
                .then(function (response) {
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
        .then(function (response) {
            var phoneNumber = response.data.phoneNumber || 'Chưa cập nhật ...';
            var dateOfBirth = response.data.dateOfBirth || 'Chưa cập nhật ...';
            var nationalId = response.data.nationalId || 'Chưa cập nhật ...';
            var drivingLicense = response.data.drivingLicense || 'Chưa cập nhật ...';
            var address = response.data.address || 'Chưa cập nhật ...';
            var role;
            if (response.data.role.name === 'ROLE_ADMIN') {
                role = 'Admin';
            } else if (response.data.role.name === 'ROLE_USER') {
                role = 'Người dùng';
            } else {
                role = "Chủ xe";
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
											<td id="viewUserId">` + response.data.id + `</td>
										</tr>
										<tr>
											<th>Họ tên</th>
											<td id="viewUserFullname">` + response.data.name + `</td>
										</tr>
										<tr>
											<th>Email</th>
											<td id="viewUserEmail">` + response.data.email + `</td>
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
											<td id="viewUserCreactDay">` + response.data.createdDate + `</td>
										</tr>
										<tr>
											<th>Cập nhật lần cuối</th>
											<td id="viewUserLastUpdate">` + response.data.lastModifiedDate + `</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
			`;
            bodyViewUser.append(wrapper);
        });

}

var bodyViewAdmin = document.getElementById('bodyViewAdmin');

function viewAdmin(id) {
    axios({
        method: 'GET',
        contentType: "application/json",

        url: baseUrl + "/admin/view-admin/" + id
    })
        .then(function (response) {
            var phoneNumber = response.data.phoneNumber || '';
            var dateOfBirth = response.data.dateOfBirth || '';
            var nationalId = response.data.nationalId || '';
            var address = response.data.address || '';
            const date = new Date(dateOfBirth);
            const formattedDate = date.toISOString().split('T')[0];

            var wrapper = document.createElement('div');
            wrapper.innerHTML = ``;
            bodyViewAdmin.innerHTML = ``;
            wrapper.innerHTML = `
        <div class="row">
            <form id="updateForm">
                <div class="col-sm-12 col-lg-12">
                    <input type="hidden" name="id" id="userId" value="${id}">
                    <div class="form-group row mb-2">
                        <label for="viewUserFullname" class="col-sm-3 col-form-label" >Họ tên</label>
                        <div class="col-sm-9">
                            <input type="text" name="name" class="form-control" id="viewUserFullname" value="${response.data.name}" >
                        </div>
                    </div>
                    <div class="form-group row mb-2">
                        <label for="viewUserEmail" class="col-sm-3 col-form-label">Email</label>
                        <div class="col-sm-9">
                            <input type="email" name="email" class="form-control" id="viewUserEmail" value="${response.data.email}" >
                        </div>
                    </div>
                    <div class="form-group row mb-2">
                        <label for="viewPhoneNumber" class="col-sm-3 col-form-label">Số điện thoại</label>
                        <div class="col-sm-9">
                            <input type="text" name="phoneNumber" class="form-control" id="viewPhoneNumber" value="${phoneNumber}" >
                        </div>
                    </div>
                    <div class="form-group row mb-2">
                        <label for="viewUserDateOfBirth" class="col-sm-3 col-form-label">Ngày sinh</label>
                        <div class="col-sm-9">
                            <input type="date" name="dateOfBirth" class="form-control" id="viewUserDateOfBirth" value="${formattedDate}"  >
                        </div>
                    </div>
                    <div class="form-group row mb-2">
                        <label for="viewUserNationalId" class="col-sm-3 col-form-label">Quốc gia</label>
                        <div class="col-sm-9">
                            <input type="text" name="nationalId" class="form-control" id="viewUserNationalId" value="${nationalId}" >
                        </div>
                    </div>
                    <div class="form-group row mb-2">
                        <label for="viewUserAddress" class="col-sm-3 col-form-label">Địa chỉ</label>
                        <div class="col-sm-9">
                            <input type="text" name="address" class="form-control" id="viewUserAddress" value="${address}" >
                        </div>
                    </div>
                </div>
            </form>
        </div>
        `;
            bodyViewAdmin.append(wrapper);
        });
}

document.getElementById('updateButton').addEventListener('click', function() {
    event.preventDefault();
    const form = document.getElementById('updateForm');
    const formData = new FormData(form);

    const userDto = {};
    formData.forEach((value, key) => {
        userDto[key] = value;
    });

    axios({
        method: 'PUT',
        url: baseUrl + "/admin/update",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: new URLSearchParams(userDto).toString()
    })
        .then(function(response) {
            Swal.fire({
                title: 'Đã cập nhật!',
                text: 'Thông tin người dùng đã được cập nhật thành công.',
                icon: 'success',
                timer: 1000,
                showConfirmButton: false
            }).then(() => {
                location.reload(true);
            });
        })
        .catch(function(error) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Cập nhật không thành công.',
                icon: 'error',
                timer: 1000,
                showConfirmButton: false
            });
        });
});

