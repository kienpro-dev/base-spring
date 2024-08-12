var baseUrl = new URL(window.location.href).origin;
var bodyQuickView = document.getElementById('bodyQuickView');


function quickView(id) {
	axios({
		method: 'GET',
		contentType: "application/json",
		url: baseUrl + "/car/quick-view/" + id
	})
		.then(function(response) {
			var wrapper = document.createElement('div');
			wrapper.innerHTML = ``;
			bodyQuickView.innerHTML = ``;
			var price = `<h3 class="product-price"><span class="new-price">`
						+ response.data.deposit +
						`</span><span class="old-price"><del>` + response.data.price + `<del></span></h3>`;

			wrapper.innerHTML = `
				 <input type="hidden" id="cart_id" name="cart_id" value="`+ id + `">
				<h2 class="product-title">`+ response.data.name + `</h2>` + price;
			bodyQuickView.append(wrapper);
		});
	viewImages(id);
}

let images;
const mainView = document.getElementById('main-view');
const thumbnails = document.getElementById('thumbnails');

async function viewImages(id) {
	images = [];
	const response = await axios.get(baseUrl + "/car/quick-view/images/" + id);
	images = response.data;

	for (let i = 0; i < images.length; i++) {
		let image = images[i];
		let img = document.createElement('img');
		img.src = images[i].url;
		img.setAttribute('width', 170);
		img.setAttribute('data-index', i);
		img.addEventListener('click', changeImage);
		thumbnails.appendChild(img);
	}

	initGallery();
	setTimeout(slideImage, 3000);
}

function initGallery() {
	loadImage(0);
};

function slideImage() {
	let currentIndex = parseInt(mainView.getAttribute('data-index'));
	currentIndex = currentIndex + 1 == images.length ? 1 : currentIndex + 1;
	loadImage(currentIndex);
	setTimeout(slideImage, 3000);

}

function changeImage(event) {
	let target = event.currentTarget;
	let index = target.getAttribute('data-index');
	loadImage(index);
}

function loadImage(index) {
	let image = images[index];
	mainView.src = image.url;
	mainView.setAttribute('data-index', index);
	mainView.setAttribute('id', 'image-' + index);
	mainView.style.opacity = 1;
}

function fullScreenImage() {
	toggleFullscreen(mainView);
}

function toggleFullscreen(el) {
	if (document.fullscreenElement || document.mozFullScreenElement
		|| document.webkitFullscreenElement || document.msFullscreenElement) {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		}
		else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		}
		else if (document.webkitExitFullscreen) {
			document.webkitExitFullscreen();
		}
		else if (document.msExitFullscreen) {
			document.msExitFullscreen();
		}
	}
	else {
		if (document.documentElement.requestFullscreen) {
			el.requestFullscreen();
		}
		else if (document.documentElement.mozRequestFullScreen) {
			el.mozRequestFullScreen();
		}
		else if (document.documentElement.webkitRequestFullscreen) {
			el.webkitRequestFullscreen();
		}
		else if (document.documentElement.msRequestFullscreen) {
			el.msRequestFullscreen();
		}
	}
}