var baseUrl = new URL(window.location.href).origin;

function formatVND(n, currency) {
	return n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + currency;
}

function orderView(id) {
	var viewId = document.getElementById('order__view__id');
	var viewDay = document.getElementById('order__view__create__day');
	var viewPay = document.getElementById('order__view__pay');
	var viewStart=document.getElementById('order__view__start__date');
	var viewEnd = document.getElementById('order__view__end__date');
	var viewStatus = document.getElementById('order__view__status');
	var viewCar = document.getElementById('order__view__car');
	var viewBrand = document.getElementById('order__view__brand');
	var viewOwner = document.getElementById('order__view__owner');
	var viewAddress = document.getElementById('order__view__address');
	var viewEmail = document.getElementById('order__view__email');
	var viewName = document.getElementById('order__view__name');
	var viewPhone = document.getElementById('order__view__phone');
	var viewTotal = document.getElementById('order__view__total');

	axios({
		method: 'GET',
		contentType: "application/json",
		url: baseUrl + "/car/view/" + id
	})
		.then(function(response) {
			viewId.innerText = response.data.bookingId;
			viewDay.innerText = formatDate(response.data.dateOrder);

			if (response.data.paymentMethod === 'wallet') {
				viewPay.innerText = 'Thanh toán ví điện tử';
				viewPay.className = 'text-danger';
			} else if(response.data.paymentMethod === 'cash'){
				viewPay.innerText = 'Nhận tiền khi giao xe';
				viewPay.className = 'text-success';
			}
			else{
				viewPay.innerText = 'Chuyển khoản VNPAY';
				viewPay.className = 'text-info';
			}
			viewStart.innerText=formatDate(response.data.startDate);
			viewEnd.innerText=formatDate(response.data.endDate);


			if (response.data.status === 'PENDING_DEPOSIT' || response.data.status === null) {
				viewStatus.innerText = 'Đang chờ xác nhận cọc';
				viewStatus.className = 'text-danger';
			} else if (response.data.status === 'CONFIRM') {
				viewStatus.innerText = 'Đã xác nhận';
				viewStatus.className = 'text-info';
			} else if (response.data.status === 'CANCEL') {
				viewStatus.innerText = 'Đã hủy';
				viewStatus.className = 'text-warning';
			} else if (response.data.status === 'COMPLETED') {
				viewStatus.innerText = 'Đã hoàn thành';
				viewStatus.className = 'text-success';
			}
			else if (response.data.status === 'IN_PROGRESS') {
				viewStatus.innerText = 'Đang thuê';
				viewStatus.className = 'text-success';
			}else {
				viewStatus.innerText = 'Đang chờ xác nhận thanh toán';
				viewStatus.className = 'text-text-warning';
			}
			viewCar.innerText=response.data.carName;
			viewBrand.innerText=response.data.brand;
			viewOwner.innerText=response.data.carOwner;
			viewName.innerText=response.data.nameCustomer;


			viewAddress.innerText = response.data.addCustomer;
			viewEmail.innerText = response.data.emailCustomer;
			viewPhone.innerText = response.data.phoneNumber;

			var formattedTotal = new Intl.NumberFormat('de-DE').format(response.data.total) + ' VND';
			viewTotal.innerText = formattedTotal;
			viewTotal.style.color = 'red';

		})
}

function formatDate(dateString) {
	var date = new Date(dateString);
	return date.toLocaleString('sv-SE', {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit',
		hour12: false
	}).replace(' ', 'T').replace('T', ' ').slice(0, 16);
}

