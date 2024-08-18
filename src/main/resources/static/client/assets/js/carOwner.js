let currentStep = 0;
const steps = document.querySelectorAll('.step');
const navLinks = document.querySelectorAll('.nav-steps .nav-link');

function showStep(step) {
    steps.forEach((stepElement, index) => {
        stepElement.classList.toggle('active', index === step);
    });
    navLinks.forEach((navLink, index) => {
        navLink.classList.toggle('active', index === step);
    });
    document.getElementById('prevBtn').style.display = step === 0 ? 'none' : 'inline';
    document.getElementById('nextBtn').style.display = step === steps.length - 1 ? 'none' : 'inline';
    currentStep = step;
    if(step === 3){
        updateStep4();
    }
}

function nextStep() {
    if (currentStep < steps.length - 1) {
        showStep(currentStep + 1);
    }
}

function prevStep() {
    if (currentStep > 0) {
        showStep(currentStep - 1);
    }
}

function cancelForm() {
    window.location.href = '/car-owner/car';
}

function submitForm() {
    const formData = new FormData(document.getElementById('carForm'));
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    fetch('/car-owner/car/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            alert('Car added successfully!');
            window.location.href = '/car-owner/car';
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateStep4() {
    let carName = document.getElementById('nameOfCar').value;
    document.getElementById('carName').innerText = carName;
    let carPrice = document.getElementById('basePrice').value;
    document.getElementById('price').innerText = carPrice;
    let address = document.getElementById('address').value;
    document.getElementById('location').innerText = address;
    document.getElementById('status').innerText = 'Available';
    let deposit = document.getElementById('deposit').value;
    document.getElementById('depositAmount').innerText = deposit;
}

showStep(currentStep);







