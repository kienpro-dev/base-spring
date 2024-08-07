var baseUrl = new URL(window.location.href).origin;

function formatVND(n, currency) {
	return n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + currency;
}

function updateValueDisplay(id) {
	const inputNumber = document.querySelector('#inputNumberCart' + id);
	const qty = inputNumber.value;

	axios.post(baseUrl + '/molla/cart/update?id=' + id + "&sst=" + qty)
		.then(function(response) {
			let price = 0;
			if (response.data.discount > 0) {
				price = (response.data.price - (response.data.price * response.data.discount * 0.01)) * response.data.quantity;
			} else {
				price = response.data.price * response.data.quantity;
			}
			document.getElementById('viewTotailPrice' + id).innerHTML = formatVND(price, ' VND');
		})

	axios.get(baseUrl + '/molla/cart/totail-cart')
		.then(function(response) {
			let totailCart = 0;
			totailCart = response.data;
			document.getElementById('totailViewCart').innerHTML = formatVND(totailCart, ' VND');
			const ship = document.querySelector('input[name="shipping"]:checked').value;
			if (ship === 1) {
				totailCart = totailCart + 10000;
			} else if (ship === 2) {
				totailCart = totailCart + 20000;
			} else {
				totailCart = totailCart + 0;
			}
			document.getElementById('totailViewOrder').innerHTML = formatVND(totailCart, ' VND');
		})
}

function handleShipingClick() {
	axios.get(baseUrl + '/molla/cart/totail-cart')
		.then(function(response) {
			let totailViewOrder = 0;
			totailViewOrder = response.data;
			const ship = document.querySelector('input[name="shipping"]:checked').value;
			let shipping = 0;
			if (ship === '1') {
				totailViewOrder = totailViewOrder + 10000;
				shipping = 1;
			} else if (ship === '2') {
				totailViewOrder = totailViewOrder + 20000;
				shipping = 2;
			} else {
				totailViewOrder = totailViewOrder + 0;
			}
			axios.post(baseUrl + '/molla/cart/shipping?ship=' + shipping);
			document.getElementById('totailViewOrder').innerHTML = formatVND(totailViewOrder, ' VND');
		});
}