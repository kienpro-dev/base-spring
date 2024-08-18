function cancelSubmit(id) {
    Swal.fire({
        title: 'Huỷ đơn?',
        text: "Bạn có chắc muốn hủy đơn hàng này?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xác nhận!',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            axios.get(baseUrl + '/car/booking/cancel/' + id)
                .then(function(response) {
                    Swal.fire({
                        title: 'Thông báo',
                        text: 'Hủy đơn thành công',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 700
                    }).then(() => {
                        window.location.href = baseUrl + '/car/booking-list';
                    });
                })
                .catch(function(error) {
                    Swal.fire({
                        title: 'Lỗi',
                        text: 'Có lỗi xảy ra khi hủy đơn',
                        icon: 'error',
                        confirmButtonColor: '#3085d6'
                    });
                });
        }
    });
}
