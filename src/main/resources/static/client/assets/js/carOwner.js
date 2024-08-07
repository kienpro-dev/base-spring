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

showStep(currentStep);



function updateStep4() {
    document.getElementById('carName').value = document.querySelector('input[name="name"]').value;
    document.getElementById('price').value = document.querySelector('input[name="basePrice"]').value;
    document.getElementById('location').value = document.querySelector('input[name="address"]').value;
    document.getElementById('status').value = 'Available'; // Placeholder status
}



